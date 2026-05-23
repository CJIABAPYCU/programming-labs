package server.network;

import common.network.MessageFragment;
import common.network.ProtocolConstants;
import common.network.Request;
import common.network.Response;
import common.network.util.FragmentAssembler;
import server.console.ServerConsole;
import server.core.ServerContext;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Single-threaded non-blocking UDP server.
 */
public final class UdpServer implements AutoCloseable {

    private static final Logger LOG = Logger.getLogger(UdpServer.class.getName());

    private final int port;
    private final ServerContext context;
    private final ServerConsole console;
    private final RequestReader reader = new RequestReader();
    private final ResponseSender responseSender = new ResponseSender();
    private final ConnectionAcceptor acceptor = new ConnectionAcceptor();
    private final Map<SocketAddress, FragmentAssembler> assemblers = new HashMap<>();
    private final Set<SocketAddress> knownClients = new HashSet<>();

    private DatagramChannel channel;
    private Selector selector;

    /**
     * @param port UDP port
     * @param context server context
     * @param console server console
     */
    public UdpServer(int port, ServerContext context, ServerConsole console) {
        this.port = port;
        this.context = context;
        this.console = console;
    }

    /** Starts the server loop. */
    public void run() {
        LOG.info(() -> "Server loop initialization started for UDP port " + port);
        try (this) {
            selector = Selector.open();
            LOG.info("Selector opened");
            channel = acceptor.openAndRegister(port, selector);
            LOG.info(() -> "UDP server started on port " + port);
            while (!context.isShuttingDown()) {
                selector.select(ProtocolConstants.SELECT_TIMEOUT_MS);
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isReadable()) {
                        handleRead();
                    }
                }
                assemblers.values().forEach(FragmentAssembler::cleanupExpired);
                console.poll();
            }
            LOG.info("Shutdown flag received, leaving server loop");
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Server failure", e);
        }
    }

    private void handleRead() {
        ByteBuffer buffer = ByteBuffer.allocate(ProtocolConstants.BUFFER_SIZE);
        SocketAddress remote;
        try {
            remote = channel.receive(buffer);
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Receive failed", e);
            return;
        }
        if (remote == null) {
            return;
        }
        if (knownClients.add(remote)) {
            LOG.info(() -> "New UDP client endpoint registered: " + remote);
        }
        buffer.flip();
        LOG.info(() -> "Received UDP datagram from " + remote
                + ", bytes=" + buffer.remaining());
        try {
            MessageFragment fragment = reader.parseFragment(buffer);
            LOG.info(() -> "Received request fragment "
                    + (fragment.getIndex() + 1) + "/" + fragment.getTotal()
                    + " from " + remote);
            FragmentAssembler assembler = assemblers.computeIfAbsent(remote, ignored -> new FragmentAssembler());
            var completed = assembler.accept(fragment);
            if (completed.isEmpty()) {
                LOG.info(() -> "Request from " + remote + " is waiting for more fragments");
                return;
            }
            LOG.info(() -> "Request bytes reassembled from " + remote
                    + ", bytes=" + completed.get().length);
            Request request = reader.parseMessage(completed.get());
            LOG.info(() -> "Got request " + request.getType()
                    + " from " + remote
                    + ", requestId=" + request.getRequestId());
            long startedAt = System.nanoTime();
            Response response = context.cachedResponse(remote, request.getRequestId());
            if (response == null) {
                LOG.info(() -> "Processing request " + request.getRequestId()
                        + " of type " + request.getType());
                response = context.registry().execute(request, context);
                context.cacheResponse(remote, response);
            } else {
                LOG.info(() -> "Using cached response for request " + request.getRequestId()
                        + " from " + remote);
            }
            responseSender.send(channel, response, remote);
            long elapsedMs = (System.nanoTime() - startedAt) / 1_000_000;
            LOG.info("Sent " + response.getStatus()
                    + " for request " + request.getRequestId()
                    + " in " + elapsedMs + " ms");
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Bad packet from " + remote, e);
            try {
                responseSender.send(channel, Response.error(0, "Некорректный пакет."), remote);
            } catch (IOException ignored) {
                LOG.warning("Failed to send bad-packet response.");
            }
        }
    }

    @Override
    public void close() throws IOException {
        if (selector != null) {
            selector.close();
        }
        if (channel != null) {
            channel.close();
        }
        LOG.info("UDP server stopped");
    }
}

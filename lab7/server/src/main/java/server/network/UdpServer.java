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
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class UdpServer implements AutoCloseable {

    private static final Logger LOG = Logger.getLogger(UdpServer.class.getName());

    private final int port;
    private final ServerContext context;
    private final ServerConsole console;
    private final RequestReader reader = new RequestReader();
    private final ResponseSender responseSender = new ResponseSender();
    private final ConnectionAcceptor acceptor = new ConnectionAcceptor();
    private final Map<SocketAddress, FragmentAssembler> assemblers = new ConcurrentHashMap<>();

    private final ForkJoinPool readPool = new ForkJoinPool();
    private final ExecutorService processingPool = Executors.newFixedThreadPool(
            Math.max(2, Runtime.getRuntime().availableProcessors()));
    private final ForkJoinPool sendPool = new ForkJoinPool();
    private final Object sendLock = new Object();

    private DatagramChannel channel;
    private Selector selector;

    public UdpServer(int port, ServerContext context, ServerConsole console) {
        this.port = port;
        this.context = context;
        this.console = console;
    }

    public void run() {
        LOG.info(() -> "Server loop initialization started for UDP port " + port);
        try (this) {
            selector = Selector.open();
            channel = acceptor.openAndRegister(port, selector);
            LOG.info(() -> "UDP server started on port " + port);
            while (!context.isShuttingDown()) {
                selector.select(ProtocolConstants.SELECT_TIMEOUT_MS);
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isReadable()) {
                        readPool.execute(this::readDatagram);
                    }
                }
                cleanupAssemblers();
                console.poll();
            }
            LOG.info("Shutdown flag received, leaving server loop");
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Server failure", e);
        }
    }

    private void readDatagram() {
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
        buffer.flip();
        try {
            MessageFragment fragment = reader.parseFragment(buffer);
            FragmentAssembler assembler = assemblers.computeIfAbsent(remote, ignored -> new FragmentAssembler());
            java.util.Optional<byte[]> completed;
            synchronized (assembler) {
                completed = assembler.accept(fragment);
            }
            if (completed.isEmpty()) {
                return;
            }
            Request request = reader.parseMessage(completed.get());
            LOG.info(() -> "Got request " + request.getType() + " from " + remote);
            processingPool.execute(() -> processRequest(remote, request));
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Bad packet from " + remote, e);
            sendResponse(Response.error(0, "Некорректный пакет."), remote);
        }
    }

    private void processRequest(SocketAddress remote, Request request) {
        Response response = context.registry().execute(request, context);
        sendResponse(response, remote);
    }

    private void sendResponse(Response response, SocketAddress remote) {
        sendPool.execute(() -> {
            try {
                synchronized (sendLock) {
                    responseSender.send(channel, response, remote);
                }
            } catch (IOException e) {
                LOG.log(Level.WARNING, "Failed to send response to " + remote, e);
            }
        });
    }

    private void cleanupAssemblers() {
        assemblers.values().forEach(assembler -> {
            synchronized (assembler) {
                assembler.cleanupExpired();
            }
        });
    }

    @Override
    public void close() throws IOException {
        readPool.shutdownNow();
        processingPool.shutdownNow();
        sendPool.shutdownNow();
        if (selector != null) {
            selector.close();
        }
        if (channel != null) {
            channel.close();
        }
        LOG.info("UDP server stopped");
    }
}

package client.network;

import common.network.MessageFragment;
import common.network.ProtocolConstants;
import common.network.Request;
import common.network.Response;
import common.network.util.FragmentAssembler;
import common.network.util.FragmentUtil;
import common.network.util.SerializationUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

/**
 * UDP client based on DatagramSocket and DatagramPacket.
 */
public final class UdpClient implements AutoCloseable {

    private final ServerEndpoint endpoint;
    private final DatagramSocket socket;

    /**
     * @param endpoint server endpoint
     * @throws IOException if socket cannot be opened
     */
    public UdpClient(ServerEndpoint endpoint) throws IOException {
        this.endpoint = endpoint;
        this.socket = new DatagramSocket();
        this.socket.setSoTimeout(ProtocolConstants.CLIENT_SO_TIMEOUT_MS);
    }

    /**
     * Sends a request and waits for a matching response. Requests and responses
     * are split into application fragments, so they are not limited by one UDP
     * datagram.
     *
     * @param request request
     * @return response with the same request id
     * @throws ServerUnavailableException if all attempts timed out
     * @throws IOException on network or serialization errors
     * @throws ClassNotFoundException if response classes are unavailable
     */
    public Response sendAndReceive(Request request)
            throws ServerUnavailableException, IOException, ClassNotFoundException {
        IOException lastError = null;
        for (int attempt = 1; attempt <= ProtocolConstants.CLIENT_MAX_RETRIES; attempt++) {
            sendFragments(request);
            FragmentAssembler assembler = new FragmentAssembler();
            try {
                while (true) {
                    byte[] buffer = new byte[ProtocolConstants.BUFFER_SIZE];
                    DatagramPacket in = new DatagramPacket(buffer, buffer.length);
                    socket.receive(in);
                    if (!endpoint.address().equals(in.getAddress()) || endpoint.port() != in.getPort()) {
                        continue;
                    }
                    MessageFragment fragment = SerializationUtil.fromBytes(in.getData(), in.getLength());
                    var complete = assembler.accept(fragment);
                    if (complete.isEmpty()) {
                        continue;
                    }
                    Response response = SerializationUtil.fromBytes(complete.get());
                    if (response.getRequestId() == request.getRequestId() || response.getRequestId() == 0) {
                        return response;
                    }
                    assembler.clear();
                }
            } catch (SocketTimeoutException e) {
                lastError = e;
                sleepBeforeRetry();
            }
        }
        throw new ServerUnavailableException(
                "Сервер не отвечает после " + ProtocolConstants.CLIENT_MAX_RETRIES + " попыток.",
                lastError);
    }

    private void sendFragments(Request request) throws IOException {
        for (MessageFragment fragment : FragmentUtil.split(request)) {
            byte[] bytes = SerializationUtil.toBytes(fragment);
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, endpoint.address(), endpoint.port());
            socket.send(packet);
        }
    }

    private void sleepBeforeRetry() {
        try {
            Thread.sleep(ProtocolConstants.CLIENT_RETRY_DELAY_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void close() {
        socket.close();
    }
}

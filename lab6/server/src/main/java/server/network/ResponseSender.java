package server.network;

import common.network.MessageFragment;
import common.network.Response;
import common.network.util.FragmentUtil;
import common.network.util.SerializationUtil;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.List;
import java.util.logging.Logger;

/**
 * Sends server responses as one or more UDP datagrams.
 */
public final class ResponseSender {

    private static final Logger LOG = Logger.getLogger(ResponseSender.class.getName());

    /**
     * Serializes, fragments and sends a response.
     *
     * @param channel server datagram channel
     * @param response response to send
     * @param clientAddress client address
     * @throws IOException if serialization or sending fails
     */
    public void send(DatagramChannel channel, Response response, SocketAddress clientAddress)
            throws IOException {
        List<MessageFragment> fragments = FragmentUtil.split(response);
        LOG.info(() -> "Sending response " + response.getStatus()
                + " for request " + response.getRequestId()
                + " to " + clientAddress
                + " in " + fragments.size() + " UDP fragment(s)");
        for (MessageFragment fragment : fragments) {
            byte[] data = SerializationUtil.toBytes(fragment);
            ByteBuffer buffer = ByteBuffer.wrap(data);
            int emptySends = 0;
            while (buffer.hasRemaining()) {
                int sent = channel.send(buffer, clientAddress);
                if (sent == 0) {
                    emptySends++;
                    if (emptySends > 1_000) {
                        throw new IOException("UDP send buffer is full");
                    }
                    Thread.onSpinWait();
                }
            }
            LOG.fine(() -> "Sent response fragment "
                    + (fragment.getIndex() + 1) + "/" + fragment.getTotal()
                    + " to " + clientAddress);
        }
    }
}

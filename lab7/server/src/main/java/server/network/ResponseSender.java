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
 * Отправляет ответы сервера в виде одной или нескольких UDP-датаграмм.
 */
public final class ResponseSender {

    private static final Logger LOG = Logger.getLogger(ResponseSender.class.getName());

    /**
     * Сериализует, фрагментирует и отправляет ответ.
     *
     * @param channel серверный датаграммный канал
     * @param response ответ для отправки
     * @param clientAddress адрес клиента
     * @throws IOException если сериализация или отправка завершились ошибкой
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
            while (buffer.hasRemaining()) {
                channel.send(buffer, clientAddress);
            }
            LOG.fine(() -> "Sent response fragment "
                    + (fragment.getIndex() + 1) + "/" + fragment.getTotal()
                    + " to " + clientAddress);
        }
    }
}

package server.network;

import common.network.MessageFragment;
import common.network.Request;
import common.network.util.SerializationUtil;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Читает датаграммы запроса и десериализует полные сообщения.
 */
public final class RequestReader {

    /**
     * Десериализует одну датаграмму в фрагмент сообщения.
     *
     * @param buffer буфер, готовый к чтению
     * @return фрагмент сообщения
     * @throws IOException если байты не удалось прочитать
     * @throws ClassNotFoundException если не найден нужный класс
     */
    public MessageFragment parseFragment(ByteBuffer buffer) throws IOException, ClassNotFoundException {
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        return SerializationUtil.fromBytes(data);
    }

    /**
     * Десериализует полностью собранный запрос.
     *
     * @param data байты полного сериализованного запроса
     * @return запрос
     * @throws IOException если десериализация завершилась ошибкой
     * @throws ClassNotFoundException если не найден нужный класс
     */
    public Request parseMessage(byte[] data) throws IOException, ClassNotFoundException {
        return SerializationUtil.fromBytes(data);
    }
}

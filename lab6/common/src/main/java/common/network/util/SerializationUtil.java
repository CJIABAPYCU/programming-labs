package common.network.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Сериализация объектов в byte[] и обратно.
 *
 * <p>Используется и клиентом, и сервером:
 * <ul>
 *   <li>клиент: {@code byte[] data = SerializationUtil.toBytes(request);}
 *       → {@code DatagramPacket(data, data.length, addr, port)};</li>
 *   <li>сервер: {@code Object obj = SerializationUtil.fromBytes(buffer);}
 *       → каст к {@code Request}.</li>
 * </ul>
 *
 * <p><b>Важно:</b> это <i>единственное</i> место в проекте, где допустимо
 * напрямую вызывать {@link ObjectOutputStream} / {@link ObjectInputStream}.
 * Это держит сериализацию отделённой от транспорта.
 */
public final class SerializationUtil {

    private SerializationUtil() {
        // utility-класс
    }

    /**
     * Сериализует объект в массив байт.
     *
     * @param object сериализуемый объект (не {@code null})
     * @return сериализованное представление
     * @throws IOException при ошибке записи в поток
     */
    public static byte[] toBytes(Serializable object) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(object);
            oos.flush();
            return baos.toByteArray();
        }
    }

    /**
     * Десериализует объект из массива байт.
     *
     * @param data    массив байт (не {@code null})
     * @param length  фактическая длина (для буферов на сервере)
     * @param <T>     тип ожидаемого объекта
     * @return десериализованный объект
     * @throws IOException            при ошибке чтения
     * @throws ClassNotFoundException если класс не найден
     * @throws ClassCastException     если фактический тип отличается
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromBytes(byte[] data, int length) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data, 0, length);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (T) ois.readObject();
        }
    }

    /**
     * Удобная перегрузка с длиной = {@code data.length}.
     *
     * @param data массив байт (не {@code null})
     * @param <T>  тип ожидаемого объекта
     * @return десериализованный объект
     * @throws IOException            при ошибке чтения
     * @throws ClassNotFoundException если класс не найден
     */
    public static <T> T fromBytes(byte[] data) throws IOException, ClassNotFoundException {
        return fromBytes(data, data.length);
    }
}

package common.network.util;

import common.network.MessageFragment;
import common.network.ProtocolConstants;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Разбивает сериализованные объекты на прикладные фрагменты, подходящие для UDP.
 */
public final class FragmentUtil {

    private FragmentUtil() {
    }

    /**
     * Сериализует объект и разбивает его на фрагменты.
     *
     * @param object сериализуемый запрос или ответ
     * @return фрагменты, готовые к сериализации в датаграммы
     * @throws IOException если сериализация завершилась ошибкой
     */
    public static List<MessageFragment> split(Serializable object) throws IOException {
        byte[] bytes = SerializationUtil.toBytes(object);
        int chunkSize = ProtocolConstants.FRAGMENT_PAYLOAD_SIZE;
        int total = Math.max(1, (bytes.length + chunkSize - 1) / chunkSize);
        long messageId = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
        List<MessageFragment> result = new ArrayList<>(total);
        for (int i = 0; i < total; i++) {
            int from = i * chunkSize;
            int to = Math.min(bytes.length, from + chunkSize);
            byte[] chunk = java.util.Arrays.copyOfRange(bytes, from, to);
            result.add(new MessageFragment(messageId, i, total, chunk));
        }
        return result;
    }
}

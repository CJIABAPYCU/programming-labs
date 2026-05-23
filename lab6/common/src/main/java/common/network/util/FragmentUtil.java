package common.network.util;

import common.network.MessageFragment;
import common.network.ProtocolConstants;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Splits serialized objects into UDP-sized application fragments.
 */
public final class FragmentUtil {

    private FragmentUtil() {
    }

    /**
     * Serializes and splits an object into fragments.
     *
     * @param object serializable request or response
     * @return fragments ready to be serialized into datagrams
     * @throws IOException if serialization fails
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

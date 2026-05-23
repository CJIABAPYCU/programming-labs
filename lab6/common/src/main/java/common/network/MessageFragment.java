package common.network;

import java.io.Serializable;
import java.util.Arrays;

/**
 * One datagram-sized fragment of a serialized request or response.
 */
public final class MessageFragment implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long messageId;
    private final int index;
    private final int total;
    private final byte[] data;

    /**
     * @param messageId id shared by all fragments of one message
     * @param index zero-based fragment index
     * @param total total fragment count
     * @param data fragment bytes
     */
    public MessageFragment(long messageId, int index, int total, byte[] data) {
        if (total <= 0 || index < 0 || index >= total) {
            throw new IllegalArgumentException("Некорректный номер UDP-фрагмента.");
        }
        this.messageId = messageId;
        this.index = index;
        this.total = total;
        this.data = data == null ? new byte[0] : Arrays.copyOf(data, data.length);
    }

    public long getMessageId() { return messageId; }
    public int getIndex() { return index; }
    public int getTotal() { return total; }

    /**
     * @return defensive copy of fragment bytes
     */
    public byte[] getData() {
        return Arrays.copyOf(data, data.length);
    }
}

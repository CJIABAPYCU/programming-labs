package common.network.util;

import common.network.MessageFragment;
import common.network.ProtocolConstants;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Собирает прикладные фрагменты обратно в исходный сериализованный массив байтов.
 */
public final class FragmentAssembler {

    private final Map<Long, PartialMessage> partials = new HashMap<>();

    /**
     * Добавляет один фрагмент.
     *
     * @param fragment входящий фрагмент
     * @return готовые байты сообщения, когда приходит последний недостающий фрагмент
     */
    public Optional<byte[]> accept(MessageFragment fragment) {
        cleanupExpired();
        PartialMessage partial = partials.computeIfAbsent(fragment.getMessageId(),
                id -> new PartialMessage(fragment.getTotal()));
        if (partial.total != fragment.getTotal()) {
            partials.remove(fragment.getMessageId());
            return Optional.empty();
        }
        Optional<byte[]> completed = partial.add(fragment);
        completed.ifPresent(bytes -> partials.remove(fragment.getMessageId()));
        return completed;
    }

    /** Очищает все незавершённые сообщения. */
    public void clear() {
        partials.clear();
    }

    /** Удаляет просроченные незавершённые сообщения. */
    public void cleanupExpired() {
        long now = System.currentTimeMillis();
        partials.entrySet().removeIf(e -> now - e.getValue().createdAt > ProtocolConstants.FRAGMENT_TTL_MS);
    }

    private static final class PartialMessage {
        private final long createdAt = System.currentTimeMillis();
        private final int total;
        private final Map<Integer, byte[]> chunks = new HashMap<>();
        private int received;

        private PartialMessage(int total) {
            this.total = total;
        }

        private Optional<byte[]> add(MessageFragment fragment) {
            int index = fragment.getIndex();
            if (!chunks.containsKey(index)) {
                chunks.put(index, fragment.getData());
                received++;
            }
            if (received != total) {
                return Optional.empty();
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                for (int i = 0; i < total; i++) {
                    byte[] chunk = chunks.get(i);
                    if (chunk == null) {
                        return Optional.empty();
                    }
                    out.write(chunk);
                }
            } catch (java.io.IOException e) {
                throw new IllegalStateException(e);
            }
            return Optional.of(out.toByteArray());
        }
    }
}

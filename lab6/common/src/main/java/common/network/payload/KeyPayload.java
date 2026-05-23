package common.network.payload;

/**
 * Payload containing a map key.
 */
public final class KeyPayload implements RequestPayload {

    private static final long serialVersionUID = 1L;

    private final int key;

    /**
     * @param key LinkedHashMap key
     */
    public KeyPayload(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}

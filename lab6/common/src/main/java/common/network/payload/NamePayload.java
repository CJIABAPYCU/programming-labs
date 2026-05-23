package common.network.payload;

/**
 * Payload со строкой-именем.
 *
 * <p>Используется в {@code FILTER_CONTAINS_NAME}.
 */
public final class NamePayload implements RequestPayload {

    private static final long serialVersionUID = 1L;

    private final String name;

    /**
     * @param name искомая подстрока (не {@code null})
     */
    public NamePayload(String name) {
        this.name = name;
    }

    /** @return искомая подстрока */
    public String getName() {
        return name;
    }
}

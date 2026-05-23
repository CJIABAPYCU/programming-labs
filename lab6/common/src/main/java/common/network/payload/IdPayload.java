package common.network.payload;

/**
 * Payload with one generated id.
 */
public final class IdPayload implements RequestPayload, ResponsePayload {

    private static final long serialVersionUID = 1L;

    private final long id;

    /**
     * @param id идентификатор
     */
    public IdPayload(long id) {
        this.id = id;
    }

    /** @return идентификатор */
    public long getId() {
        return id;
    }
}

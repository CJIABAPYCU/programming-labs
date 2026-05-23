package common.network.payload;

/**
 * Пустой payload для команд / ответов без данных.
 *
 * <p>Реализован как singleton: {@link #INSTANCE}. Это экономит память
 * и упрощает сравнение.
 */
public final class EmptyPayload implements RequestPayload, ResponsePayload {

    private static final long serialVersionUID = 1L;

    /** Единственный экземпляр. */
    public static final EmptyPayload INSTANCE = new EmptyPayload();

    private EmptyPayload() {
        // singleton
    }

    /**
     * Сохраняет singleton-семантику при десериализации.
     *
     * @return {@link #INSTANCE}
     */
    private Object readResolve() {
        return INSTANCE;
    }
}

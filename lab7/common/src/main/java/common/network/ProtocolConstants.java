package common.network;

/**
 * Константы сетевого протокола.
 *
 * <p><b>Менять значения можно</b>, но осмысленно. Любое изменение
 * {@link #BUFFER_SIZE} требует согласования между клиентом и сервером.
 */
public final class ProtocolConstants {

    /** Максимальный полезный размер UDP-датаграммы. */
    public static final int MAX_PACKET_SIZE = 65_507;

    /** Размер принимающего буфера. */
    public static final int BUFFER_SIZE = MAX_PACKET_SIZE;

    /** Размер полезной нагрузки одного прикладного фрагмента. */
    public static final int FRAGMENT_PAYLOAD_SIZE = 60_000;

    /** Таймаут чтения ответа на клиенте, мс. */
    public static final int CLIENT_SO_TIMEOUT_MS = 5_000;

    /** Максимум повторных отправок на клиенте при таймауте. */
    public static final int CLIENT_MAX_RETRIES = 3;

    /** Пауза между повторными отправками на клиенте, мс. */
    public static final int CLIENT_RETRY_DELAY_MS = 500;

    /** Таймаут селектора на сервере, мс. */
    public static final int SELECT_TIMEOUT_MS = 100;

    /** Срок жизни незавершённой сборки фрагментов. */
    public static final int FRAGMENT_TTL_MS = 30_000;

    private ProtocolConstants() {
        // utility-класс — экземпляры не нужны.
    }
}

package common.network;

import common.network.payload.RequestPayload;

import java.io.Serializable;
import java.util.UUID;

/**
 * Запрос клиента к серверу.
 *
 * <p>Сериализуется целиком, а сетевой слой при необходимости делит
 * сериализованные байты на UDP-фрагменты.
 *
 * <p>Жизненный цикл:
 * <ol>
 *   <li>{@code ClientCommand.parse(...)} собирает запрос;</li>
 *   <li>{@code UdpClient} сериализует и отсылает;</li>
 *   <li>сервер десериализует, сохраняет {@code requestId} в логе и в ответе.</li>
 * </ol>
 */
public final class Request implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long requestId;
    private final CommandType type;
    private final RequestPayload payload;

    /**
     * Создаёт запрос с автоматически сгенерированным {@code requestId}.
     *
     * @param type    тип команды (не {@code null})
     * @param payload payload (не {@code null}; если параметров нет —
     *                {@code EmptyPayload.INSTANCE})
     */
    public Request(CommandType type, RequestPayload payload) {
        this(generateId(), type, payload);
    }

    /**
     * Полный конструктор. Используется при ручной сборке (тесты, ретрансляция).
     *
     * @param requestId уникальный идентификатор запроса
     * @param type      тип команды
     * @param payload   payload
     */
    public Request(long requestId, CommandType type, RequestPayload payload) {
        this.requestId = requestId;
        this.type = type;
        this.payload = payload;
    }

    /** @return идентификатор запроса (для логирования / эха в ответе) */
    public long getRequestId() { return requestId; }

    /** @return тип команды */
    public CommandType getType() { return type; }

    /** @return payload запроса */
    public RequestPayload getPayload() { return payload; }

    private static long generateId() {
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }
}

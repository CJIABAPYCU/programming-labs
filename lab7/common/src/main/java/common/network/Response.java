package common.network;

import common.network.payload.EmptyPayload;
import common.network.payload.ResponsePayload;

import java.io.Serializable;

/**
 * Ответ сервера на запрос клиента.
 *
 * <p>Сериализуется целиком, а сетевой слой при необходимости делит
 * сериализованные байты на UDP-фрагменты.
 *
 * <p>{@code requestId} равен {@code Request.getRequestId()} для удобства
 * сопоставления и логирования.
 */
public final class Response implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long requestId;
    private final ResponseStatus status;
    private final String message;
    private final ResponsePayload payload;

    /**
     * Полный конструктор.
     *
     * @param requestId эхо id запроса
     * @param status    статус
     * @param message   человекочитаемое сообщение (может быть пустым,
     *                  но не {@code null})
     * @param payload   payload (если данных нет — {@link EmptyPayload#INSTANCE})
     */
    public Response(long requestId, ResponseStatus status, String message, ResponsePayload payload) {
        this.requestId = requestId;
        this.status = status;
        this.message = message == null ? "" : message;
        this.payload = payload == null ? EmptyPayload.INSTANCE : payload;
    }

    /**
     * Удобный конструктор для ответа без payload.
     *
     * @param requestId эхо id запроса
     * @param status    статус
     * @param message   сообщение
     */
    public Response(long requestId, ResponseStatus status, String message) {
        this(requestId, status, message, EmptyPayload.INSTANCE);
    }

    /** Удобная фабрика OK-ответа. */
    public static Response ok(long requestId, String message, ResponsePayload payload) {
        return new Response(requestId, ResponseStatus.OK, message, payload);
    }

    /** Удобная фабрика OK-ответа без payload. */
    public static Response ok(long requestId, String message) {
        return new Response(requestId, ResponseStatus.OK, message);
    }

    /** Удобная фабрика ERROR-ответа. */
    public static Response error(long requestId, String message) {
        return new Response(requestId, ResponseStatus.ERROR, message);
    }

    /** Удобная фабрика FORBIDDEN-ответа. */
    public static Response forbidden(long requestId, String message) {
        return new Response(requestId, ResponseStatus.FORBIDDEN, message);
    }

    public long getRequestId() { return requestId; }
    public ResponseStatus getStatus() { return status; }
    public String getMessage() { return message; }
    public ResponsePayload getPayload() { return payload; }

    /** @return {@code true}, если статус {@link ResponseStatus#OK}. */
    public boolean isOk() {
        return status == ResponseStatus.OK;
    }
}

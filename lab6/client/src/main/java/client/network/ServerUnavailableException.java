package client.network;

/**
 * Сервер не отвечает после всех {@link common.network.ProtocolConstants#CLIENT_MAX_RETRIES}
 * попыток.
 *
 * <p>Клиент должен перехватывать это исключение в {@code CliRunner.runLine},
 * печатать пользователю «Сервер недоступен. Попробуйте позже.» и
 * продолжать работу (не завершаться).
 */
public class ServerUnavailableException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * @param message сообщение
     */
    public ServerUnavailableException(String message) {
        super(message);
    }

    /**
     * @param message сообщение
     * @param cause   причина
     */
    public ServerUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}

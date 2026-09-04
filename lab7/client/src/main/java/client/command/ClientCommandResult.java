package client.command;

/**
 * Результат выполнения клиентской команды.
 *
 * <p>Сообщает {@link client.runner.CliRunner}:
 * <ul>
 *   <li>надо ли выйти из основного цикла ({@code shouldExit});</li>
 *   <li>была ли команда успешной ({@code ok}) — для прерывания скрипта
 *       при ошибке;</li>
 *   <li>сообщение для печати (может быть пустым).</li>
 * </ul>
 */
public final class ClientCommandResult {

    private final boolean ok;
    private final String message;
    private final boolean shouldExit;

    /**
     * @param ok          успех
     * @param message     сообщение
     * @param shouldExit  завершить цикл
     */
    public ClientCommandResult(boolean ok, String message, boolean shouldExit) {
        this.ok = ok;
        this.message = message == null ? "" : message;
        this.shouldExit = shouldExit;
    }

    /** Успех без сообщения. */
    public static ClientCommandResult ok() {
        return new ClientCommandResult(true, "", false);
    }

    /** Успех с сообщением. */
    public static ClientCommandResult ok(String message) {
        return new ClientCommandResult(true, message, false);
    }

    /** Ошибка с сообщением. */
    public static ClientCommandResult error(String message) {
        return new ClientCommandResult(false, message, false);
    }

    /** Команда {@code exit}. */
    public static ClientCommandResult exit() {
        return new ClientCommandResult(true, "", true);
    }

    public boolean isOk()        { return ok; }
    public String message()      { return message; }
    public boolean shouldExit()  { return shouldExit; }
}

package commands;

/**
 * Результат выполнения команды.
 */
public class CommandOutcome {
    private final boolean success;
    private final String message;
    private final boolean shouldExit;

    /**
     * Создает результат.
     *
     * @param success флаг успешности
     * @param message сообщение
     * @param shouldExit флаг завершения
     */
    public CommandOutcome(boolean success, String message, boolean shouldExit) {
        this.success = success;
        this.message = message;
        this.shouldExit = shouldExit;
    }

    /**
     * Показывает, успешно ли выполнена команда.
     *
     * @return флаг успешности
     */
    public boolean isOk() {
        return success;
    }

    /**
     * Возвращает текст результата выполнения команды.
     *
     * @return сообщение
     */
    public String message() {
        return message;
    }

    /**
     * Показывает, нужно ли завершить программу.
     *
     * @return флаг завершения
     */
    public boolean shouldExit() {
        return shouldExit;
    }
}

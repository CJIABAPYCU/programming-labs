package console;

/**
 * Сигнализирует о прерывании ввода (конец ввода).
 */
public class InputAbortException extends Exception {
    /**
     * Создает исключение с сообщением.
     *
     * @param message сообщение об ошибке
     */
    public InputAbortException(String message) {
        super(message);
    }
}

package client.console;

/**
 * Возникает, когда пользователь прервал ввод (EOF / Ctrl-D / закрытие скрипта)
 * во время чтения составных типов.
 *
 * <p>Контракт: команда (например, {@code insert}) ловит это исключение и
 * возвращает осмысленное сообщение, без падения клиента.
 */
public class InputAbortException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * @param message сообщение
     */
    public InputAbortException(String message) {
        super(message);
    }
}

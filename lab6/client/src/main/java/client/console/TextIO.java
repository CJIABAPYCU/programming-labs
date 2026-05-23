package client.console;

import java.util.Scanner;

/**
 * Абстракция текстового ввода-вывода клиента.
 *
 * <p>Используется интерактивным режимом и исполнителем скриптов, чтобы
 * команды одинаково работали с консольным вводом и вводом из файла.
 */
public interface TextIO {

    /** @param obj вывод без перевода строки */
    void print(Object obj);

    /** @param obj вывод с переводом строки */
    void println(Object obj);

    /** @param obj сообщение об ошибке (в System.err) */
    void printError(Object obj);

    /** @return следующая прочитанная строка */
    String readLine();

    /** @return {@code true}, если есть ещё строка */
    boolean hasNextLine();

    /** Печатает приглашение интерактивного режима. */
    void prompt();

    /**
     * Переключить ввод на указанный {@link Scanner} (для скриптов).
     *
     * @param scanner новый источник
     */
    void pushInput(Scanner scanner);

    /** Восстановить предыдущий источник ввода. */
    void popInput();
}

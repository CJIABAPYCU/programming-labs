package console;

import java.util.Scanner;

/**
 * Абстракция TextIO для ввода и вывода.
 */
public interface TextIO {
    /**
     * Выводит объект без новой строки.
     *
     * @param obj объект для вывода
     */
    void print(Object obj);

    /**
     * Выводит объект с новой строкой.
     *
     * @param obj объект для вывода
     */
    void println(Object obj);

    /**
     * Выводит сообщение об ошибке.
     *
     * @param obj объект ошибки
     */
    void printError(Object obj);

    /**
     * Считывает строку из активного источника ввода.
     *
     * @return строка
     */
    String readLine();

    /**
     * Проверяет, есть ли следующая строка в активном вводе.
     *
     * @return true, если есть следующая строка
     */
    boolean hasNextLine();

    /**
     * Выводит приглашение для интерактивного режима.
     */
    void prompt();

    /**
     * Переключает ввод на переданный scanner (с сохранением стека).
     *
     * @param scanner используемый scanner
     */
    void pushInput(Scanner scanner);

    /**
     * Восстанавливает предыдущий scanner ввода.
     */
    void popInput();
}

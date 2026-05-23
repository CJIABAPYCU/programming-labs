package client.console;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

/**
 * Реализация {@link TextIO} поверх {@link System#in}/{@link System#out}.
 *
 * <p>Стек сканеров нужен для команды {@code execute_script}: во время
 * выполнения скрипта активный источник ввода временно заменяется файлом,
 * а после завершения восстанавливается предыдущий источник.
 */
public class TerminalConsole implements TextIO {

    private static final String PROMPT = "> ";

    private final Scanner defaultScanner = new Scanner(System.in);
    private final Deque<Scanner> stack = new ArrayDeque<>();
    private Scanner active = defaultScanner;

    /** Создаёт консоль терминала. */
    public TerminalConsole() {
    }

    @Override public void print(Object obj)        { System.out.print(obj); }
    @Override public void println(Object obj)      { System.out.println(obj); }
    @Override public void printError(Object obj)   { System.err.println("Ошибка: " + obj); }
    @Override public void prompt()                 { print(PROMPT); }
    @Override public String readLine()             { return active.nextLine(); }
    @Override public boolean hasNextLine()         { return active.hasNextLine(); }

    @Override
    public void pushInput(Scanner scanner) {
        stack.push(active);
        active = scanner;
    }

    @Override
    public void popInput() {
        active = stack.isEmpty() ? defaultScanner : stack.pop();
    }
}

package console;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Реализация консоли терминала для интерактивного и скриптового ввода.
 */
public class TerminalConsole implements TextIO {
    private static final String PROMPT = "> ";
    private final Scanner defaultScanner = new Scanner(System.in);
    private final Deque<Scanner> scannerStack = new ArrayDeque<>();
    private Scanner activeScanner = defaultScanner;

    /**
     * Создает консоль для интерактивного и скриптового ввода.
     */
    public TerminalConsole() {
    }

    @Override
    public void print(Object obj) {
        System.out.print(obj);
    }

    @Override
    public void println(Object obj) {
        System.out.println(obj);
    }

    @Override
    public void printError(Object obj) {
        System.err.println("Ошибка: " + obj);
    }

    @Override
    public String readLine() {
        try {
            return activeScanner.nextLine();
        } catch (NoSuchElementException | IllegalStateException e) {
            throw e;
        }
    }

    @Override
    public boolean hasNextLine() {
        try {
            return activeScanner.hasNextLine();
        } catch (IllegalStateException e) {
            throw e;
        }
    }

    @Override
    public void prompt() {
        print(PROMPT);
    }

    @Override
    public void pushInput(Scanner scanner) {
        scannerStack.push(activeScanner);
        activeScanner = scanner;
    }

    @Override
    public void popInput() {
        if (!scannerStack.isEmpty()) {
            activeScanner = scannerStack.pop();
        } else {
            activeScanner = defaultScanner;
        }
    }
}

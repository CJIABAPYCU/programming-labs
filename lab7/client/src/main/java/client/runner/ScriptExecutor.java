package client.runner;

import client.command.ClientCommandResult;
import client.console.TextIO;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

/**
 * Исполнитель скриптов команд (команда {@code execute_script}).
 *
 * <p>Особенности:
 * <ul>
 *   <li>каждая строка скрипта проходит через {@link CliRunner#runLine(String)};</li>
 *   <li>поля составных типов (для insert/update) читаются из того же скрипта,
 *       пока активен {@code Scanner} скрипта (push/pop через
 *       {@link TextIO#pushInput(Scanner)} / {@link TextIO#popInput()});</li>
 *   <li>защита от рекурсии: если скрипт вызывает другой скрипт, который
 *       уже находится в стеке, выполнение останавливается.</li>
 * </ul>
 */
public final class ScriptExecutor {

    private final TextIO console;
    private final CliRunner runner;
    private final Deque<String> stack = new ArrayDeque<>();

    /**
     * @param console консоль
     * @param runner  CLI-раннер
     */
    public ScriptExecutor(TextIO console, CliRunner runner) {
        this.console = console;
        this.runner = runner;
    }

    /**
     * Исполняет файл скрипта.
     *
     * @param fileName имя файла
     * @return результат (ok, либо ошибка с описанием)
     */
    public ClientCommandResult execute(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return ClientCommandResult.error("Имя файла скрипта не задано.");
        }

        File file = resolveFile(fileName);
        if (!file.exists()) {
            return ClientCommandResult.error("Файл скрипта не найден: " + fileName);
        }
        if (!file.canRead()) {
            return ClientCommandResult.error("Нет прав на чтение скрипта: " + fileName);
        }

        String canonicalPath;
        try {
            canonicalPath = file.getCanonicalPath();
        } catch (IOException e) {
            return ClientCommandResult.error("Ошибка доступа к скрипту: " + e.getMessage());
        }

        if (stack.contains(canonicalPath)) {
            return ClientCommandResult.error("Обнаружена рекурсия скриптов: " + canonicalPath);
        }

        stack.push(canonicalPath);
        ClientCommandResult last = ClientCommandResult.ok();
        boolean inputPushed = false;
        try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
            console.pushInput(scanner);
            inputPushed = true;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                last = runner.runLine(line);
                if (last.isOk() && !last.message().isEmpty()) {
                    console.println(last.message());
                }
                if (!last.isOk() || last.shouldExit()) {
                    break;
                }
            }
        } catch (IOException e) {
            return ClientCommandResult.error("Ошибка чтения скрипта: " + e.getMessage());
        } finally {
            if (inputPushed) {
                console.popInput();
            }
            stack.pop();
        }
        if (last.shouldExit()) {
            return ClientCommandResult.exit();
        }
        if (last.isOk()) {
            return ClientCommandResult.ok("Скрипт выполнен.");
        }
        if (last.message().startsWith("Скрипт остановлен:")) {
            return last;
        }
        return ClientCommandResult.error("Скрипт остановлен: " + last.message());
    }

    private File resolveFile(String fileName) {
        File file = new File(fileName);
        if (file.isAbsolute() || stack.isEmpty()) {
            return file;
        }
        File currentScript = new File(stack.peek());
        return new File(currentScript.getParentFile(), fileName);
    }
}

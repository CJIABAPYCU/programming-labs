package client.runner;

import client.command.ClientCommandResult;
import client.console.TextIO;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
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
 *       уже находится в стеке — ошибка.</li>
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
        Path path = Path.of(fileName).toAbsolutePath().normalize();
        if (!Files.exists(path)) {
            return ClientCommandResult.error("Файл скрипта не найден: " + fileName);
        }
        if (!Files.isReadable(path)) {
            return ClientCommandResult.error("Нет прав на чтение скрипта: " + fileName);
        }
        String key = path.toString();
        if (stack.contains(key)) {
            return ClientCommandResult.error("Обнаружена рекурсия скриптов: " + key);
        }
        stack.push(key);
        ClientCommandResult last = ClientCommandResult.ok();
        boolean inputPushed = false;
        try (Scanner scanner = new Scanner(new File(fileName))) {
            console.pushInput(scanner);
            inputPushed = true;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                last = runner.runLine(line);
                if (!last.message().isEmpty()) {
                    if (last.isOk()) {
                        console.println(last.message());
                    } else {
                        console.printError(last.message());
                    }
                }
                if (!last.isOk() || last.shouldExit()) {
                    break;
                }
            }
        } catch (Exception e) {
            return ClientCommandResult.error("Ошибка выполнения скрипта: " + e.getMessage());
        } finally {
            if (inputPushed) {
                console.popInput();
            }
            stack.pop();
        }
        if (last.shouldExit()) {
            return ClientCommandResult.exit();
        }
        return last.isOk()
                ? ClientCommandResult.ok("Скрипт выполнен.")
                : ClientCommandResult.error("Скрипт остановлен: " + last.message());
    }
}

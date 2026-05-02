package runner;

import commands.CommandOutcome;
import console.TextIO;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Выполняет команды из файлов скриптов.
 */
public class ScriptExecutor {
    private final TextIO console;
    private final CliRunner runner;

    /**
     * Создает исполнитель скриптов.
     *
     * @param console консоль
     * @param runner раннер для выполнения команд
     */
    public ScriptExecutor(TextIO console, CliRunner runner) {
        this.console = console;
        this.runner = runner;
    }

    /**
     * Выполняет файл скрипта.
     *
     * @param fileName имя файла скрипта
     * @return результат
     */
    public CommandOutcome execute(String fileName) {
        Path path = Path.of(fileName).toAbsolutePath().normalize();
        if (!Files.exists(path)) {
            return new CommandOutcome(false, "Файл скрипта не найден: " + fileName, false);
        }
        if (!Files.isReadable(path)) {
            return new CommandOutcome(false, "Нет прав на чтение скрипта: " + fileName, false);
        }
        CommandOutcome lastResult = new CommandOutcome(true, "", false);
        try (Scanner scanner = new Scanner(new File(fileName))) {
            console.pushInput(scanner);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                lastResult = runner.runLine(line);
                if (lastResult.message() != null && !lastResult.message().isEmpty()) {
                    console.println(lastResult.message());
                }
                if (!lastResult.isOk()) {
                    break;
                }
                if (lastResult.shouldExit()) {
                    break;
                }
            }
        } catch (Exception e) {
            return new CommandOutcome(false, "Ошибка выполнения скрипта: " + e.getMessage(), false);
        } finally {
            console.popInput();
        }
        return lastResult.shouldExit()
                ? new CommandOutcome(true, "", true)
                : lastResult.isOk()
                ? new CommandOutcome(true, "Скрипт выполнен.", false)
                : new CommandOutcome(false, "Скрипт остановлен: " + lastResult.message(), false);
    }
}

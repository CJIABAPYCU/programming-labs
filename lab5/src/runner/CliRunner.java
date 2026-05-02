package runner;

import commands.CommandDispatcher;
import commands.CommandOutcome;
import console.TextIO;
import core.ApplicationContext;

/**
 * Запускает интерактивный цикл.
 */
public class CliRunner {
    private final TextIO console;
    private final CommandDispatcher dispatcher;
    private final ApplicationContext context;
    private boolean shouldExit = false;

    /**
     * Создает раннер.
     *
     * @param console консоль
     * @param dispatcher диспетчер команд
     * @param context контекст приложения
     */
    public CliRunner(TextIO console, CommandDispatcher dispatcher, ApplicationContext context) {
        this.console = console;
        this.dispatcher = dispatcher;
        this.context = context;
    }

    /**
     * Запускает интерактивный режим.
     */
    public void run() {
        while (!shouldExit) {
            console.prompt();
            if (!console.hasNextLine()) {
                break;
            }
            String line = console.readLine();
            CommandOutcome result = runLine(line);
            if (result.message() != null && !result.message().isEmpty()) {
                console.println(result.message());
            }
            if (result.shouldExit()) {
                shouldExit = true;
            }
        }
    }

    /**
     * Выполняет одну командную строку.
     *
     * @param line командная строка
     * @return результат команды
     */
    public CommandOutcome runLine(String line) {
        if (line == null) {
            return new CommandOutcome(false, "Пустая команда.", false);
        }
        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            return new CommandOutcome(true, "", false);
        }
        String[] parts = trimmed.split("\\s+", 2);
        String name = parts[0];
        String args = parts.length > 1 ? parts[1].trim() : "";
        return dispatcher.dispatch(name, args, context);
    }
}

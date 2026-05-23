package client.runner;

import client.command.ClientCommand;
import client.command.ClientCommandContext;
import client.command.ClientCommandRegistry;
import client.command.ClientCommandResult;
import client.console.InputAbortException;
import client.console.OrganizationReader;
import client.console.TextIO;
import client.network.UdpClient;

/**
 * Главный интерактивный цикл клиента.
 *
 * <p><b>Алгоритм:</b>
 * <pre>
 * while (!shouldExit) {
 *     console.prompt();
 *     if (!console.hasNextLine()) break;
 *     String line = console.readLine();
 *     ClientCommandResult res = runLine(line);
 *     if (res.message().nonEmpty()) console.println(res.message());
 *     if (res.shouldExit()) shouldExit = true;
 * }
 * </pre>
 *
 * <p>Метод {@link #runLine(String)} используется и {@link ScriptExecutor}.
 */
public final class CliRunner {

    private final TextIO console;
    private final ClientCommandRegistry registry;
    private final ClientCommandContext ctx;

    private boolean shouldExit;

    /**
     * @param console   консоль
     * @param registry  реестр команд
     * @param udpClient UDP-клиент
     * @param reader    читатель организации
     */
    public CliRunner(TextIO console,
                     ClientCommandRegistry registry,
                     UdpClient udpClient,
                     OrganizationReader reader) {
        this.console = console;
        this.registry = registry;
        this.ctx = new ClientCommandContext(console, reader, udpClient, registry);
    }

    /** Поздняя инъекция исполнителя скриптов (см. ClientCommandContext). */
    public void setScriptExecutor(ScriptExecutor scriptExecutor) {
        ctx.setScriptExecutor(scriptExecutor);
    }

    /** Запускает интерактивный цикл. */
    public void run() {
        while (!shouldExit) {
            console.prompt();
            if (!console.hasNextLine()) {
                break;
            }
            ClientCommandResult result = runLine(console.readLine());
            if (!result.message().isEmpty()) {
                if (result.isOk()) {
                    console.println(result.message());
                } else {
                    console.printError(result.message());
                }
            }
            if (result.shouldExit()) {
                shouldExit = true;
            }
        }
    }

    /**
     * Выполняет одну строку команды.
     *
     * @param line командная строка
     * @return результат
     */
    public ClientCommandResult runLine(String line) {
        if (line == null) {
            return ClientCommandResult.error("Пустая команда.");
        }
        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            return ClientCommandResult.ok();
        }
        String[] parts = trimmed.split("\\s+", 2);
        String name = parts[0];
        String args = parts.length > 1 ? parts[1] : "";
        ClientCommand command = registry.find(name);
        if (command == null) {
            return ClientCommandResult.error("Неизвестная команда. Введите 'help'.");
        }
        try {
            return command.execute(args, ctx);
        } catch (InputAbortException e) {
            return ClientCommandResult.error("Ввод прерван: " + e.getMessage());
        } catch (RuntimeException e) {
            return ClientCommandResult.error("Внутренняя ошибка клиента: " + e.getMessage());
        }
    }
}

package client.command.parsers;

import client.command.ClientCommand;
import client.command.ClientCommandContext;
import client.command.ClientCommandResult;

/**
 * Локальная команда {@code execute_script}.
 */
public final class ExecuteScriptCommand implements ClientCommand {

    @Override public String keyword() { return "execute_script"; }
    @Override public String summary() { return "исполнить скрипт из файла"; }

    @Override
    public ClientCommandResult execute(String args, ClientCommandContext ctx) {
        if (args == null || args.isBlank()) {
            return ClientCommandResult.error("Использование: execute_script <file>");
        }
        if (ctx.scriptExecutor() == null) {
            return ClientCommandResult.error("Исполнитель скриптов не инициализирован.");
        }
        return ctx.scriptExecutor().execute(args.trim());
    }
}

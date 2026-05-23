package client.command.parsers;

import client.command.ClientCommand;
import client.command.ClientCommandContext;
import client.command.ClientCommandResult;

/**
 * Local exit command.
 */
public final class ExitCommand implements ClientCommand {

    @Override public String keyword() { return "exit"; }
    @Override public String summary() { return "завершить работу клиента"; }

    @Override
    public ClientCommandResult execute(String args, ClientCommandContext ctx) {
        ClientCommandResult noArgs = CommandSupport.noArgs(keyword(), args);
        if (noArgs != null) {
            return noArgs;
        }
        ctx.console().println("Завершение работы клиента.");
        return ClientCommandResult.exit();
    }
}

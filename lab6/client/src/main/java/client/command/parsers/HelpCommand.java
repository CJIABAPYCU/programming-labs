package client.command.parsers;

import client.command.ClientCommand;
import client.command.ClientCommandContext;
import client.command.ClientCommandResult;

/**
 * Local help command.
 */
public final class HelpCommand implements ClientCommand {

    @Override public String keyword() { return "help"; }
    @Override public String summary() { return "вывести справку по доступным командам"; }

    @Override
    public ClientCommandResult execute(String args, ClientCommandContext ctx) {
        ClientCommandResult noArgs = CommandSupport.noArgs(keyword(), args);
        if (noArgs != null) {
            return noArgs;
        }
        ctx.registry().all().forEach(command ->
                ctx.console().println(command.keyword() + " : " + command.summary()));
        return ClientCommandResult.ok();
    }
}

package client.command.parsers;

import client.command.ClientCommand;
import client.command.ClientCommandContext;
import client.command.ClientCommandResult;
import common.network.CommandType;
import common.network.Request;
import common.network.payload.EmptyPayload;

/**
 * Client command show.
 */
public final class ShowCommand implements ClientCommand {

    @Override public String keyword() { return "show"; }
    @Override public String summary() { return "вывести все элементы коллекции"; }

    @Override
    public ClientCommandResult execute(String args, ClientCommandContext ctx) {
        ClientCommandResult noArgs = CommandSupport.noArgs(keyword(), args);
        if (noArgs != null) {
            return noArgs;
        }
        return CommandSupport.organizationsRequest(ctx,
                new Request(CommandType.SHOW, EmptyPayload.INSTANCE), "Коллекция пуста.");
    }
}

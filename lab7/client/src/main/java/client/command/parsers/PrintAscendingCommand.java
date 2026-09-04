package client.command.parsers;

import client.command.ClientCommand;
import client.command.ClientCommandContext;
import client.command.ClientCommandResult;
import common.network.CommandType;
import common.network.Request;
import common.network.payload.EmptyPayload;

/**
 * Клиентская команда {@code print_ascending}.
 */
public final class PrintAscendingCommand implements ClientCommand {

    @Override public String keyword() { return "print_ascending"; }
    @Override public String summary() { return "вывести элементы коллекции в порядке возрастания"; }

    @Override
    public ClientCommandResult execute(String args, ClientCommandContext ctx) {
        ClientCommandResult noArgs = CommandSupport.noArgs(keyword(), args);
        if (noArgs != null) {
            return noArgs;
        }
        return CommandSupport.organizationsRequest(ctx,
                new Request(CommandType.PRINT_ASCENDING, EmptyPayload.INSTANCE), "Коллекция пуста.");
    }
}

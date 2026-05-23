package client.command.parsers;

import client.command.ClientCommand;
import client.command.ClientCommandContext;
import client.command.ClientCommandResult;
import common.network.CommandType;
import common.network.Request;
import common.network.payload.NamePayload;

/**
 * Client command filter_contains_name name.
 */
public final class FilterContainsNameCommand implements ClientCommand {

    @Override public String keyword() { return "filter_contains_name"; }
    @Override public String summary() { return "вывести элементы, name которых содержит подстроку"; }

    @Override
    public ClientCommandResult execute(String args, ClientCommandContext ctx) {
        if (args == null || args.isEmpty()) {
            return ClientCommandResult.error("Использование: filter_contains_name <name>");
        }
        return CommandSupport.organizationsRequest(ctx,
                new Request(CommandType.FILTER_CONTAINS_NAME, new NamePayload(args)), "Совпадений нет.");
    }
}

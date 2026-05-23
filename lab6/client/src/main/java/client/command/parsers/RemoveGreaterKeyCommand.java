package client.command.parsers;

import client.command.ClientCommand;
import client.command.ClientCommandContext;
import client.command.ClientCommandResult;
import common.network.CommandType;
import common.network.Request;
import common.network.payload.KeyPayload;

/**
 * Client command remove_greater_key key.
 */
public final class RemoveGreaterKeyCommand implements ClientCommand {

    @Override public String keyword() { return "remove_greater_key"; }
    @Override public String summary() { return "удалить элементы, ключ которых превышает заданный"; }

    @Override
    public ClientCommandResult execute(String args, ClientCommandContext ctx) {
        int key;
        try {
            key = CommandSupport.parseIntArg(args, "remove_greater_key <key>");
        } catch (IllegalArgumentException e) {
            return ClientCommandResult.error(e.getMessage());
        }
        return CommandSupport.simpleRequest(ctx, new Request(CommandType.REMOVE_GREATER_KEY, new KeyPayload(key)));
    }
}

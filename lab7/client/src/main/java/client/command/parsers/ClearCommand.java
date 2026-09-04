package client.command.parsers;

import client.command.ClientCommand;
import client.command.ClientCommandContext;
import client.command.ClientCommandResult;
import common.network.CommandType;
import common.network.Request;
import common.network.payload.EmptyPayload;

/**
 * Клиентская команда {@code clear}.
 */
public final class ClearCommand implements ClientCommand {

    @Override public String keyword() { return "clear"; }
    @Override public String summary() { return "очистить коллекцию"; }

    @Override
    public ClientCommandResult execute(String args, ClientCommandContext ctx) {
        ClientCommandResult noArgs = CommandSupport.noArgs(keyword(), args);
        if (noArgs != null) {
            return noArgs;
        }
        return CommandSupport.simpleRequest(ctx, new Request(CommandType.CLEAR, EmptyPayload.INSTANCE));
    }
}

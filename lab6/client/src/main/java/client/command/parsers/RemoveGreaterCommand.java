package client.command.parsers;

import client.command.ClientCommand;
import client.command.ClientCommandContext;
import client.command.ClientCommandResult;
import client.console.InputAbortException;
import common.model.Organization;
import common.network.CommandType;
import common.network.Request;
import common.network.payload.OrganizationPayload;

/**
 * Client command remove_greater {element}.
 */
public final class RemoveGreaterCommand implements ClientCommand {

    @Override public String keyword() { return "remove_greater"; }
    @Override public String summary() { return "удалить все элементы, превышающие заданный"; }

    @Override
    public ClientCommandResult execute(String args, ClientCommandContext ctx) throws InputAbortException {
        ClientCommandResult noArgs = CommandSupport.noArgs(keyword(), args);
        if (noArgs != null) {
            return noArgs;
        }
        Organization organization = ctx.reader().readNew();
        return CommandSupport.simpleRequest(ctx,
                new Request(CommandType.REMOVE_GREATER, new OrganizationPayload(organization)));
    }
}

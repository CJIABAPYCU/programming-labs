package client.command.parsers;

import client.command.ClientCommand;
import client.command.ClientCommandContext;
import client.command.ClientCommandResult;
import client.console.InputAbortException;
import common.model.Organization;
import common.network.CommandType;
import common.network.Request;
import common.network.payload.KeyOrganizationPayload;

/**
 * Client command insert key {element}.
 */
public final class InsertCommand implements ClientCommand {

    @Override public String keyword() { return "insert"; }
    @Override public String summary() { return "добавить новый элемент с заданным ключом"; }

    @Override
    public ClientCommandResult execute(String args, ClientCommandContext ctx) throws InputAbortException {
        int key;
        try {
            key = CommandSupport.parseIntArg(args, "insert <key>");
        } catch (IllegalArgumentException e) {
            return ClientCommandResult.error(e.getMessage());
        }
        Organization organization = ctx.reader().readNew();
        return CommandSupport.simpleRequest(ctx,
                new Request(CommandType.INSERT, new KeyOrganizationPayload(key, organization)));
    }
}

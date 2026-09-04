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
 * Клиентская команда {@code replace_if_lowe key {element}}.
 */
public final class ReplaceIfLoweCommand implements ClientCommand {

    @Override public String keyword() { return "replace_if_lowe"; }
    @Override public String summary() { return "заменить значение по ключу, если новое значение меньше старого"; }

    @Override
    public ClientCommandResult execute(String args, ClientCommandContext ctx) throws InputAbortException {
        int key;
        try {
            key = CommandSupport.parseIntArg(args, "replace_if_lowe <key>");
        } catch (IllegalArgumentException e) {
            return ClientCommandResult.error(e.getMessage());
        }
        Organization organization = ctx.reader().readNew();
        return CommandSupport.simpleRequest(ctx,
                new Request(CommandType.REPLACE_IF_LOWE, new KeyOrganizationPayload(key, organization)));
    }
}

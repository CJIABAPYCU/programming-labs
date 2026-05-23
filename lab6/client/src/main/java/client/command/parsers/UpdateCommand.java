package client.command.parsers;

import client.command.ClientCommand;
import client.command.ClientCommandContext;
import client.command.ClientCommandResult;
import client.console.InputAbortException;
import common.model.Organization;
import common.network.CommandType;
import common.network.Request;
import common.network.payload.UpdatePayload;

/**
 * Client command update id {element}.
 */
public final class UpdateCommand implements ClientCommand {

    @Override public String keyword() { return "update"; }
    @Override public String summary() { return "обновить значение элемента коллекции по id"; }

    @Override
    public ClientCommandResult execute(String args, ClientCommandContext ctx) throws InputAbortException {
        int id;
        try {
            id = CommandSupport.parseIntArg(args, "update <id>");
        } catch (IllegalArgumentException e) {
            return ClientCommandResult.error(e.getMessage());
        }
        if (id <= 0) {
            return ClientCommandResult.error("id должен быть > 0.");
        }
        Organization organization = ctx.reader().readForUpdate(id);
        return CommandSupport.simpleRequest(ctx,
                new Request(CommandType.UPDATE, new UpdatePayload(id, organization)));
    }
}

package server.command.handlers;

import common.model.Organization;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import common.network.payload.KeyPayload;
import server.command.ServerCommand;
import server.core.ServerContext;

/**
 * Handles remove_key key.
 */
public final class RemoveKeyHandler implements ServerCommand {

    @Override
    public CommandType type() {
        return CommandType.REMOVE_KEY;
    }

    @Override
    public Response execute(Request request, ServerContext context) {
        if (!(request.getPayload() instanceof KeyPayload payload)) {
            return Response.error(request.getRequestId(), "Некорректные аргументы remove_key.");
        }
        Organization removed = context.repository().removeKey(payload.getKey());
        if (removed == null) {
            return Response.error(request.getRequestId(), "Элемент с ключом " + payload.getKey() + " не найден.");
        }
        return Response.ok(request.getRequestId(), "Удалено: " + removed);
    }
}

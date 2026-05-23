package server.command.handlers;

import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import common.network.payload.KeyPayload;
import server.command.ServerCommand;
import server.core.ServerContext;

/**
 * Handles remove_greater_key key.
 */
public final class RemoveGreaterKeyHandler implements ServerCommand {

    @Override
    public CommandType type() {
        return CommandType.REMOVE_GREATER_KEY;
    }

    @Override
    public Response execute(Request request, ServerContext context) {
        if (!(request.getPayload() instanceof KeyPayload payload)) {
            return Response.error(request.getRequestId(), "Некорректные аргументы remove_greater_key.");
        }
        int removed = context.repository().removeGreaterKey(payload.getKey());
        return Response.ok(request.getRequestId(), "Удалено элементов: " + removed + ".");
    }
}

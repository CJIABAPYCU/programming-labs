package server.command.handlers;

import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import common.network.payload.KeyPayload;
import server.command.ServerCommand;
import server.core.ServerContext;

import java.util.List;

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
        String owner = request.getCredentials().getLogin();
        List<Integer> ids = context.repository().findGreaterKeyIds(payload.getKey(), owner);
        for (int id : ids) {
            context.storage().deleteById(id);
        }
        int removed = context.repository().removeGreaterKey(payload.getKey(), owner);
        return Response.ok(request.getRequestId(), "Удалено элементов: " + removed + ".");
    }
}

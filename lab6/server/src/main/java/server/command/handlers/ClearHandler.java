package server.command.handlers;

import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import server.command.ServerCommand;
import server.core.ServerContext;

/**
 * Очищает коллекцию.
 */
public final class ClearHandler implements ServerCommand {

    @Override
    public CommandType type() {
        return CommandType.CLEAR;
    }

    @Override
    public Response execute(Request request, ServerContext context) {
        context.repository().clear();
        return Response.ok(request.getRequestId(), "Коллекция очищена.");
    }
}

package server.command.handlers;

import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import server.command.ServerCommand;
import server.core.ServerContext;

public final class ClearHandler implements ServerCommand {

    @Override
    public CommandType type() {
        return CommandType.CLEAR;
    }

    @Override
    public Response execute(Request request, ServerContext context) {
        String owner = request.getCredentials().getLogin();
        int deleted = context.storage().deleteByOwner(owner);
        context.repository().removeByOwner(owner);
        return Response.ok(request.getRequestId(), "Удалено объектов: " + deleted + ".");
    }
}

package server.command.handlers;

import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import common.network.payload.OrganizationsPayload;
import server.command.ServerCommand;
import server.core.ServerContext;

/**
 * Обрабатывает команду {@code print_ascending}.
 */
public final class PrintAscendingHandler implements ServerCommand {

    @Override
    public CommandType type() {
        return CommandType.PRINT_ASCENDING;
    }

    @Override
    public Response execute(Request request, ServerContext context) {
        return Response.ok(request.getRequestId(), "",
                new OrganizationsPayload(context.repository().snapshotAscending()));
    }
}

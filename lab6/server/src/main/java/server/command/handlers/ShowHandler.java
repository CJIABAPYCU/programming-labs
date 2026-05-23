package server.command.handlers;

import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import common.network.payload.OrganizationsPayload;
import server.command.ServerCommand;
import server.core.ServerContext;

/**
 * Возвращает все элементы коллекции, отсортированные по {@code name}.
 *
 * <p>Сортировка делается на сервере через Stream API
 * ({@link server.core.OrganizationRepository#snapshotSortedByName()}).
 */
public final class ShowHandler implements ServerCommand {

    @Override
    public CommandType type() {
        return CommandType.SHOW;
    }

    @Override
    public Response execute(Request request, ServerContext context) {
        return Response.ok(request.getRequestId(), "",
                new OrganizationsPayload(context.repository().snapshotSortedByName()));
    }
}

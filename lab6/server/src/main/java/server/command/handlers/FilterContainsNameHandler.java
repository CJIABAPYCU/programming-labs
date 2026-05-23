package server.command.handlers;

import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import common.network.payload.NamePayload;
import common.network.payload.OrganizationsPayload;
import server.command.ServerCommand;
import server.core.ServerContext;

/**
 * Возвращает элементы, у которых поле {@code name} содержит подстроку.
 *
 * <p>Реализация — через {@code Stream.filter(...)} в репозитории.
 */
public final class FilterContainsNameHandler implements ServerCommand {

    @Override
    public CommandType type() {
        return CommandType.FILTER_CONTAINS_NAME;
    }

    @Override
    public Response execute(Request request, ServerContext context) {
        if (!(request.getPayload() instanceof NamePayload payload) || payload.getName() == null) {
            return Response.error(request.getRequestId(), "Некорректные аргументы filter_contains_name.");
        }
        return Response.ok(request.getRequestId(), "",
                new OrganizationsPayload(context.repository().filterContainsName(payload.getName())));
    }
}

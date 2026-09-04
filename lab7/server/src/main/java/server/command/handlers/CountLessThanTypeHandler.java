package server.command.handlers;

import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import common.network.payload.TypePayload;
import server.command.ServerCommand;
import server.core.ServerContext;

/**
 * Обрабатывает команду {@code count_less_than_type type}.
 */
public final class CountLessThanTypeHandler implements ServerCommand {

    @Override
    public CommandType type() {
        return CommandType.COUNT_LESS_THAN_TYPE;
    }

    @Override
    public Response execute(Request request, ServerContext context) {
        if (!(request.getPayload() instanceof TypePayload payload) || payload.getType() == null) {
            return Response.error(request.getRequestId(), "Некорректные аргументы count_less_than_type.");
        }
        long count = context.repository().countLessThanType(payload.getType());
        return Response.ok(request.getRequestId(), String.valueOf(count));
    }
}

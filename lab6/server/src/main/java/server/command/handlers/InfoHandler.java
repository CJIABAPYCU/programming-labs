package server.command.handlers;

import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import common.network.payload.InfoPayload;
import server.command.ServerCommand;
import server.core.OrganizationRepository;
import server.core.ServerContext;

/**
 * Возвращает мета-информацию о коллекции.
 */
public final class InfoHandler implements ServerCommand {

    @Override
    public CommandType type() {
        return CommandType.INFO;
    }

    @Override
    public Response execute(Request request, ServerContext context) {
        OrganizationRepository repo = context.repository();
        InfoPayload info = new InfoPayload(repo.typeName(), repo.initTime(), repo.size(), repo.lastSaveTime());
        return Response.ok(request.getRequestId(), "", info);
    }
}

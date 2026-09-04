package server.command.handlers;

import common.model.Organization;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import common.network.payload.OrganizationPayload;
import server.command.ServerCommand;
import server.core.ServerContext;

import java.util.List;

public final class RemoveGreaterHandler implements ServerCommand {

    @Override
    public CommandType type() {
        return CommandType.REMOVE_GREATER;
    }

    @Override
    public Response execute(Request request, ServerContext context) {
        if (!(request.getPayload() instanceof OrganizationPayload payload)) {
            return Response.error(request.getRequestId(), "Некорректные аргументы remove_greater.");
        }
        Organization sample = payload.getOrganization();
        if (sample == null || !sample.validateWithoutGeneratedFields()) {
            return Response.error(request.getRequestId(), "Поля Organization невалидны.");
        }
        String owner = request.getCredentials().getLogin();
        List<Integer> ids = context.repository().findGreaterIds(sample, owner);
        for (int id : ids) {
            context.storage().deleteById(id);
        }
        int removed = context.repository().removeGreater(sample, owner);
        return Response.ok(request.getRequestId(), "Удалено элементов: " + removed + ".");
    }
}

package server.command.handlers;

import common.model.Organization;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import common.network.payload.IdPayload;
import common.network.payload.KeyOrganizationPayload;
import server.command.ServerCommand;
import server.core.ServerContext;

import java.time.ZonedDateTime;

public final class InsertHandler implements ServerCommand {

    @Override
    public CommandType type() {
        return CommandType.INSERT;
    }

    @Override
    public Response execute(Request request, ServerContext context) {
        if (!(request.getPayload() instanceof KeyOrganizationPayload payload)) {
            return Response.error(request.getRequestId(), "Некорректные аргументы insert.");
        }
        Organization draft = payload.getOrganization();
        if (draft == null || !draft.validateWithoutGeneratedFields()) {
            return Response.error(request.getRequestId(), "Поля Organization невалидны.");
        }
        if (context.repository().containsKey(payload.getKey())) {
            return Response.error(request.getRequestId(), "Ключ уже существует.");
        }
        String owner = request.getCredentials().getLogin();
        int id = context.storage().insert(payload.getKey(), draft, owner);
        if (id < 0) {
            return Response.error(request.getRequestId(), "Ошибка записи в БД.");
        }
        Organization generated = draft.withGeneratedAndOwner(id, ZonedDateTime.now(), owner);
        context.repository().insert(payload.getKey(), generated);
        return Response.ok(request.getRequestId(), "Организация добавлена. id=" + id, new IdPayload(id));
    }
}

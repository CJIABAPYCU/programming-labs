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

/**
 * Handles insert key {element}.
 */
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
            return Response.error(request.getRequestId(), "Не удалось добавить элемент: ключ уже существует.");
        }
        context.idProvider().sync(context.repository().snapshot());
        int id = context.idProvider().nextId();
        Organization generated = draft.withGenerated(id, ZonedDateTime.now());
        if (!context.repository().insert(payload.getKey(), generated)) {
            context.idProvider().release(id);
            return Response.error(request.getRequestId(), "Не удалось добавить элемент: ключ занят или объект невалиден.");
        }
        return Response.ok(request.getRequestId(), "Организация добавлена. id=" + id, new IdPayload(id));
    }
}

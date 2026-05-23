package server.command.handlers;

import common.model.Organization;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import common.network.payload.KeyOrganizationPayload;
import server.command.ServerCommand;
import server.core.OrganizationRepository.ReplaceResult;
import server.core.ServerContext;

import java.time.ZonedDateTime;

/**
 * Handles replace_if_lowe key {element}.
 */
public final class ReplaceIfLoweHandler implements ServerCommand {

    @Override
    public CommandType type() {
        return CommandType.REPLACE_IF_LOWE;
    }

    @Override
    public Response execute(Request request, ServerContext context) {
        if (!(request.getPayload() instanceof KeyOrganizationPayload payload)) {
            return Response.error(request.getRequestId(), "Некорректные аргументы replace_if_lowe.");
        }
        Organization draft = payload.getOrganization();
        if (draft == null || !draft.validateWithoutGeneratedFields()) {
            return Response.error(request.getRequestId(), "Поля Organization невалидны.");
        }
        if (context.repository().findByKey(payload.getKey()).isEmpty()) {
            return Response.error(request.getRequestId(), "Ключ не найден.");
        }
        context.idProvider().sync(context.repository().snapshot());
        int id = context.idProvider().nextId();
        Organization generated = draft.withGenerated(id, ZonedDateTime.now());
        ReplaceResult result = context.repository().replaceIfLower(payload.getKey(), generated);
        if (result != ReplaceResult.REPLACED) {
            context.idProvider().release(id);
        }
        return switch (result) {
            case REPLACED -> Response.ok(request.getRequestId(), "Значение заменено.");
            case KEY_NOT_FOUND -> Response.error(request.getRequestId(), "Ключ не найден.");
            case NOT_LOWER -> Response.ok(request.getRequestId(), "Новое значение не меньше старого, замена не выполнена.");
            case INVALID_VALUE -> Response.error(request.getRequestId(), "Новое значение невалидно.");
        };
    }
}

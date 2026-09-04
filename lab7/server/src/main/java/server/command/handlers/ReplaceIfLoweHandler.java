package server.command.handlers;

import common.model.Organization;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import common.network.payload.KeyOrganizationPayload;
import server.command.ServerCommand;
import server.core.OrganizationRepository.ReplaceResult;
import server.core.ServerContext;

import java.util.Optional;

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
        Organization existing = context.repository().findByKey(payload.getKey()).orElse(null);
        if (existing == null) {
            return Response.error(request.getRequestId(), "Ключ не найден.");
        }
        String owner = request.getCredentials().getLogin();
        Optional<String> currentOwner = context.repository().ownerOfKey(payload.getKey());
        if (currentOwner.isEmpty() || !currentOwner.get().equals(owner)) {
            return Response.forbidden(request.getRequestId(), "Можно изменять только свои объекты.");
        }
        Organization generated = draft.withGeneratedAndOwner(existing.getId(), existing.getCreationDate(), owner);
        if (generated.compareTo(existing) >= 0) {
            return Response.ok(request.getRequestId(), "Новое значение не меньше старого, замена не выполнена.");
        }
        if (!context.storage().update(existing.getId(), generated)) {
            return Response.error(request.getRequestId(), "Ошибка обновления в БД.");
        }
        context.repository().replaceIfLower(payload.getKey(), generated);
        return Response.ok(request.getRequestId(), "Значение заменено.");
    }
}

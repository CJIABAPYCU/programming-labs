package server.command.handlers;

import common.model.Organization;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import common.network.payload.UpdatePayload;
import server.command.ServerCommand;
import server.core.ServerContext;

import java.util.Optional;

public final class UpdateHandler implements ServerCommand {

    @Override
    public CommandType type() {
        return CommandType.UPDATE;
    }

    @Override
    public Response execute(Request request, ServerContext context) {
        if (!(request.getPayload() instanceof UpdatePayload payload)) {
            return Response.error(request.getRequestId(), "Некорректные аргументы update.");
        }
        if (payload.getId() <= 0 || payload.getId() > Integer.MAX_VALUE) {
            return Response.error(request.getRequestId(), "id должен быть положительным int.");
        }
        Organization existing = context.repository().findById(payload.getId()).orElse(null);
        if (existing == null) {
            return Response.error(request.getRequestId(), "Элемент с id=" + payload.getId() + " не найден.");
        }
        String owner = request.getCredentials().getLogin();
        Optional<String> currentOwner = context.repository().ownerOfId(payload.getId());
        if (currentOwner.isEmpty() || !currentOwner.get().equals(owner)) {
            return Response.forbidden(request.getRequestId(), "Можно изменять только свои объекты.");
        }
        Organization draft = payload.getOrganization();
        if (draft == null || !draft.validateWithoutGeneratedFields()) {
            return Response.error(request.getRequestId(), "Поля Organization невалидны.");
        }
        Organization updated = draft.withGeneratedAndOwner((int) payload.getId(), existing.getCreationDate(), owner);
        if (!context.storage().update((int) payload.getId(), updated)) {
            return Response.error(request.getRequestId(), "Ошибка обновления в БД.");
        }
        context.repository().updateById(updated);
        return Response.ok(request.getRequestId(), "Элемент обновлён.");
    }
}

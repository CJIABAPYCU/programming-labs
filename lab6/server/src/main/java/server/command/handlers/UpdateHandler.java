package server.command.handlers;

import common.model.Organization;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import common.network.payload.UpdatePayload;
import server.command.ServerCommand;
import server.core.OrganizationRepository;
import server.core.ServerContext;

/**
 * Обновляет элемент по id.
 *
 * <p><b>Контракт:</b>
 * <ul>
 *   <li>id берётся из {@link UpdatePayload#getId()};</li>
 *   <li>creationDate сохраняется от существующего элемента;</li>
 *   <li>остальные поля заменяются на пришедшие;</li>
 *   <li>если элемента нет — ERROR.</li>
 * </ul>
 */
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
        Organization draft = payload.getOrganization();
        if (draft == null || !draft.validateWithoutGeneratedFields()) {
            return Response.error(request.getRequestId(), "Поля Organization невалидны.");
        }
        Organization updated = draft.withGenerated((int) payload.getId(), existing.getCreationDate());
        if (!context.repository().updateById(updated)) {
            return Response.error(request.getRequestId(), "Не удалось обновить элемент.");
        }
        return Response.ok(request.getRequestId(), "Элемент обновлён.");
    }
}

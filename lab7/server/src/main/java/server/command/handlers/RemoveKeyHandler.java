package server.command.handlers;

import common.model.Organization;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import common.network.payload.KeyPayload;
import server.command.ServerCommand;
import server.core.ServerContext;

import java.util.Optional;

public final class RemoveKeyHandler implements ServerCommand {

    @Override
    public CommandType type() {
        return CommandType.REMOVE_KEY;
    }

    @Override
    public Response execute(Request request, ServerContext context) {
        if (!(request.getPayload() instanceof KeyPayload payload)) {
            return Response.error(request.getRequestId(), "Некорректные аргументы remove_key.");
        }
        String owner = request.getCredentials().getLogin();
        Optional<String> currentOwner = context.repository().ownerOfKey(payload.getKey());
        if (currentOwner.isEmpty()) {
            return Response.error(request.getRequestId(), "Элемент с ключом " + payload.getKey() + " не найден.");
        }
        if (!currentOwner.get().equals(owner)) {
            return Response.forbidden(request.getRequestId(), "Можно удалять только свои объекты.");
        }
        Organization removed = context.repository().findByKey(payload.getKey()).orElse(null);
        if (removed == null) {
            return Response.error(request.getRequestId(), "Элемент не найден.");
        }
        if (!context.storage().deleteById(removed.getId())) {
            return Response.error(request.getRequestId(), "Ошибка удаления из БД.");
        }
        context.repository().removeKey(payload.getKey());
        return Response.ok(request.getRequestId(), "Удалено: " + removed);
    }
}

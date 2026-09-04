package server.command;

import common.auth.Credentials;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import server.auth.PasswordHasher;
import server.core.ServerContext;

import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Logger;

public final class CommandRegistry {

    private static final Logger LOG = Logger.getLogger(CommandRegistry.class.getName());

    private final Map<CommandType, ServerCommand> handlers = new EnumMap<>(CommandType.class);

    public void register(ServerCommand command) {
        handlers.put(command.type(), command);
    }

    public Response execute(Request request, ServerContext context) {
        if (request.getType() != CommandType.REGISTER && request.getType() != CommandType.LOGIN) {
            Credentials creds = request.getCredentials();
            if (creds == null || creds.getLogin() == null || creds.getPassword() == null) {
                return Response.forbidden(request.getRequestId(), "Требуется авторизация.");
            }
            if (!context.storage().checkUser(creds.getLogin(), PasswordHasher.md5(creds.getPassword()))) {
                return Response.forbidden(request.getRequestId(), "Неверный логин или пароль.");
            }
        }

        ServerCommand handler = handlers.get(request.getType());
        if (handler == null) {
            LOG.warning(() -> "Unknown command type: " + request.getType());
            return Response.error(request.getRequestId(), "Неизвестная команда: " + request.getType());
        }
        try {
            return handler.execute(request, context);
        } catch (Exception e) {
            LOG.severe(() -> "Handler crashed: " + e);
            return Response.error(request.getRequestId(),
                    "Внутренняя ошибка сервера: " + e.getMessage());
        }
    }
}

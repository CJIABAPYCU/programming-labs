package server.command.handlers;

import common.auth.Credentials;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import server.auth.PasswordHasher;
import server.command.ServerCommand;
import server.core.ServerContext;

public final class RegisterHandler implements ServerCommand {

    @Override
    public CommandType type() {
        return CommandType.REGISTER;
    }

    @Override
    public Response execute(Request request, ServerContext context) {
        Credentials creds = request.getCredentials();
        if (creds == null || creds.getLogin() == null || creds.getLogin().isBlank()
                || creds.getPassword() == null || creds.getPassword().isBlank()) {
            return Response.error(request.getRequestId(), "Логин и пароль не могут быть пустыми.");
        }
        String hash = PasswordHasher.md5(creds.getPassword());
        if (context.storage().registerUser(creds.getLogin(), hash)) {
            return Response.ok(request.getRequestId(), "Пользователь зарегистрирован.");
        }
        return Response.error(request.getRequestId(), "Логин уже занят.");
    }
}

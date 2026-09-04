package client.command.parsers;

import client.command.ClientCommand;
import client.command.ClientCommandContext;
import client.command.ClientCommandResult;
import client.command.ClientNetworkHelper;
import common.auth.Credentials;
import common.network.CommandType;
import common.network.Request;
import common.network.payload.EmptyPayload;

public final class LoginCommand implements ClientCommand {

    @Override public String keyword() { return "login"; }
    @Override public String summary() { return "войти в систему"; }

    @Override
    public ClientCommandResult execute(String args, ClientCommandContext ctx) {
        if (args == null || args.isBlank()) {
            return ClientCommandResult.error("Использование: login <login>");
        }
        String login = args.trim();
        ctx.console().print("Пароль: ");
        String password = ctx.console().readLine();
        if (password == null || password.isBlank()) {
            return ClientCommandResult.error("Пароль не может быть пустым.");
        }
        Credentials creds = new Credentials(login, password);
        Request request = new Request(CommandType.LOGIN, EmptyPayload.INSTANCE).withCredentials(creds);
        var outcome = ClientNetworkHelper.send(ctx, request);
        if (outcome.isNetworkError()) {
            return ClientCommandResult.error(outcome.errorText());
        }
        if (!outcome.response().isOk()) {
            return ClientCommandResult.error(outcome.response().getMessage());
        }
        ctx.setCredentials(creds);
        return ClientCommandResult.ok(outcome.response().getMessage());
    }
}

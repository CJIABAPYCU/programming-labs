package client.command.parsers;

import client.command.ClientCommandContext;
import client.command.ClientCommandResult;
import client.command.ClientNetworkHelper;
import common.network.Request;
import common.network.Response;
import common.network.payload.OrganizationsPayload;

/**
 * Shared helpers for client commands.
 */
final class CommandSupport {

    private CommandSupport() {
    }

    static ClientCommandResult noArgs(String command, String args) {
        if (args != null && !args.isBlank()) {
            return ClientCommandResult.error("Команда '" + command + "' не принимает аргументы.");
        }
        return null;
    }

    static ClientCommandResult simpleRequest(ClientCommandContext ctx, Request request) {
        var outcome = ClientNetworkHelper.send(ctx, request);
        if (outcome.isNetworkError()) {
            return ClientCommandResult.error(outcome.errorText());
        }
        Response response = outcome.response();
        if (!response.isOk()) {
            return ClientCommandResult.error(response.getMessage());
        }
        return ClientCommandResult.ok(response.getMessage());
    }

    static ClientCommandResult organizationsRequest(ClientCommandContext ctx,
                                                    Request request,
                                                    String emptyMessage) {
        var outcome = ClientNetworkHelper.send(ctx, request);
        if (outcome.isNetworkError()) {
            return ClientCommandResult.error(outcome.errorText());
        }
        Response response = outcome.response();
        if (!response.isOk()) {
            return ClientCommandResult.error(response.getMessage());
        }
        OrganizationsPayload payload = (OrganizationsPayload) response.getPayload();
        if (payload.getOrganizations().isEmpty()) {
            return ClientCommandResult.ok(emptyMessage);
        }
        payload.getOrganizations().forEach(org -> ctx.console().println(org));
        return ClientCommandResult.ok(response.getMessage());
    }

    static int parseIntArg(String args, String usage) {
        if (args == null || args.isBlank()) {
            throw new IllegalArgumentException("Использование: " + usage);
        }
        try {
            return Integer.parseInt(args.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Аргумент должен быть целым числом.");
        }
    }
}

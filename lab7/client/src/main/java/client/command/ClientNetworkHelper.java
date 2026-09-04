package client.command;

import client.network.ServerUnavailableException;
import client.network.UdpClient;
import common.network.Request;
import common.network.Response;

public final class ClientNetworkHelper {

    private ClientNetworkHelper() {
    }

    public static ClientResponseOutcome send(ClientCommandContext ctx, Request request) {
        if (ctx.credentials() != null) {
            request = request.withCredentials(ctx.credentials());
        }
        try {
            Response response = ctx.udpClient().sendAndReceive(request);
            return new ClientResponseOutcome(response, null);
        } catch (ServerUnavailableException e) {
            return new ClientResponseOutcome(null, "Сервер недоступен. Попробуйте позже.");
        } catch (java.io.IOException e) {
            return new ClientResponseOutcome(null, "Ошибка сети: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            return new ClientResponseOutcome(null, "Ошибка чтения ответа: " + e.getMessage());
        }
    }

    public record ClientResponseOutcome(Response response, String errorText) {
        public boolean isNetworkError() {
            return response == null;
        }
    }
}

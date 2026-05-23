package client.command;

import client.network.ServerUnavailableException;
import client.network.UdpClient;
import common.network.Request;
import common.network.Response;

/**
 * Утилита-обёртка вокруг {@link UdpClient#sendAndReceive(Request)}.
 *
 * <p>Превращает все сетевые исключения в {@link ClientCommandResult}
 * — чтобы каждый парсер не дублировал try/catch.
 */
public final class ClientNetworkHelper {

    private ClientNetworkHelper() {
    }

    /**
     * Отправляет запрос и возвращает результат, готовый к показу пользователю.
     *
     * @param ctx     контекст
     * @param request запрос
     * @return результат
     */
    public static ClientResponseOutcome send(ClientCommandContext ctx, Request request) {
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

    /**
     * Контейнер ответа: либо {@link Response}, либо текст ошибки сети.
     *
     * @param response   ответ сервера (может быть {@code null} при ошибке)
     * @param errorText  текст ошибки сети (может быть {@code null} при успехе)
     */
    public record ClientResponseOutcome(Response response, String errorText) {
        public boolean isNetworkError() {
            return response == null;
        }
    }
}

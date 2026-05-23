package server.command;

import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import server.core.ServerContext;

import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Реестр серверных команд (паттерн Registry / Strategy).
 *
 * <p>Регистрация делается в {@link CommandRegistryFactory#registerAll(CommandRegistry)}.
 *
 * <p>Метод {@link #execute(Request, ServerContext)} находит хэндлер по
 * {@link Request#getType()} и делегирует ему. Если хэндлер не найден —
 * возвращает {@link Response#error(long, String)}.
 *
 * <p><b>Команда {@code save} здесь отсутствует</b> — она исполняется
 * напрямую через {@link server.command.handlers.ServerOnlySaveCommand}
 * по вводу в консоль сервера.
 */
public final class CommandRegistry {

    private static final Logger LOG = Logger.getLogger(CommandRegistry.class.getName());

    private final Map<CommandType, ServerCommand> handlers = new EnumMap<>(CommandType.class);

    /**
     * Создаёт пустой реестр.
     */
    public CommandRegistry() {
    }

    /**
     * Регистрирует обработчик.
     *
     * @param command обработчик
     */
    public void register(ServerCommand command) {
        handlers.put(command.type(), command);
    }

    /**
     * Выполняет команду.
     *
     * @param request запрос
     * @param context контекст
     * @return ответ
     */
    public Response execute(Request request, ServerContext context) {
        ServerCommand handler = handlers.get(request.getType());
        Response response;
        if (handler == null) {
            LOG.warning(() -> "Unknown command type: " + request.getType());
            response = Response.error(request.getRequestId(), "Неизвестная команда: " + request.getType());
        } else {
            try {
                response = handler.execute(request, context);
            } catch (Exception e) {
                LOG.severe(() -> "Handler crashed: " + e);
                response = Response.error(request.getRequestId(),
                        "Внутренняя ошибка сервера: " + e.getMessage());
            }
        }
        return response;
    }
}

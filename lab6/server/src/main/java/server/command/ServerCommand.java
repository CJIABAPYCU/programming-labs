package server.command;

import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import server.core.ServerContext;

/**
 * Серверный обработчик одной команды (паттерн Command).
 *
 * <p>Реализация — один класс на одну {@link CommandType}. Все хэндлеры
 * лежат в {@code server.command.handlers}.
 *
 * <p><b>Контракт.</b> Метод {@link #execute(Request, ServerContext)}:
 * <ul>
 *   <li>не должен бросать исключений — все ошибки оборачиваются в
 *       {@link Response#error(long, String)};</li>
 *   <li>должен использовать {@code Stream API} там, где это требуется
 *       для обработки коллекции;</li>
 *   <li>должен корректно проверять тип payload (бросать ERROR при
 *       несоответствии).</li>
 * </ul>
 */
public interface ServerCommand {

    /**
     * @return тип команды, которую обрабатывает данный хэндлер
     */
    CommandType type();

    /**
     * Выполнить команду.
     *
     * @param request запрос (не {@code null})
     * @param context контекст приложения (не {@code null})
     * @return ответ (не {@code null})
     */
    Response execute(Request request, ServerContext context);
}

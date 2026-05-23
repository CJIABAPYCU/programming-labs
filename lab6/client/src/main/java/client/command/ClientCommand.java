package client.command;

import client.console.InputAbortException;
import client.console.OrganizationReader;
import client.console.TextIO;
import client.network.UdpClient;
import common.network.Response;

/**
 * Клиентская команда (паттерн Command).
 *
 * <p>Реализация знает:
 * <ul>
 *   <li>{@link #keyword()} — имя команды (для парсинга строки);</li>
 *   <li>{@link #summary()} — короткое описание для локального help;</li>
 *   <li>{@link #execute(String, ClientCommandContext)} — что сделать
 *       (валидировать аргументы, отправить на сервер, вывести ответ;
     *       либо выполнить локально — для {@code exit}, {@code execute_script}).</li>
 * </ul>
 *
 * <p>Возвращает {@link ClientCommandResult}, который сообщает CliRunner-у,
 * стоит ли продолжать.
 */
public interface ClientCommand {

    /** @return ключевое слово команды (например, "insert") */
    String keyword();

    /** @return короткое описание (для локального help) */
    String summary();

    /**
     * Выполнить команду.
     *
     * @param args   аргументы (часть строки после ключевого слова, может быть "")
     * @param ctx    общий контекст клиента
     * @return результат
     * @throws InputAbortException если ввод составного типа прерван
     */
    ClientCommandResult execute(String args, ClientCommandContext ctx) throws InputAbortException;
}

package server.command.handlers;

import server.core.ServerContext;

import java.util.logging.Logger;

/**
 * Команда {@code save}, доступная <b>только в консоли сервера</b>.
 *
 * <p><b>Не реализует {@link server.command.ServerCommand}</b> и не
 * регистрируется в {@link server.command.CommandRegistry}. Это сделано
 * специально, чтобы исключить вообще возможность вызова с клиента
 * (даже сформировав вручную UDP-датаграмму, клиент не может попасть в
 * этот код, так как нет подходящего {@link common.network.CommandType}).
 *
 * <p>Используется из {@link server.console.ServerConsole}.
 */
public final class ServerOnlySaveCommand {

    private static final Logger LOG = Logger.getLogger(ServerOnlySaveCommand.class.getName());

    private final ServerContext context;

    /**
     * @param context контекст сервера
     */
    public ServerOnlySaveCommand(ServerContext context) {
        this.context = context;
    }

    /**
     * Выполняет сохранение коллекции в файл.
     *
     * @return текстовое сообщение для оператора сервера
     */
    public String run() {
        LOG.info("Server save command invoked");
        boolean ok = context.saveCollection();
        return ok ? "Коллекция сохранена." : "Ошибка сохранения. Подробности в логе.";
    }
}

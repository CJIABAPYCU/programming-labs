package server.console;

import server.command.handlers.ServerOnlySaveCommand;
import server.core.ServerContext;
import server.network.UdpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

/**
 * Консоль сервера: интерактивный ввод команд оператора.
 *
 * <p>Поддерживаемые команды (только здесь, не из сети):
 * <ul>
 *   <li>{@code save} — сохранить коллекцию;</li>
 *   <li>{@code exit} — корректно завершить сервер.</li>
 * </ul>
 *
 * <p><b>Не блокирует поток.</b> Чтобы сервер оставался однопоточным,
 * используется приём «poll-then-read»:
 * <ol>
 *   <li>{@link UdpServer#run()} в основном цикле вызывает {@link #poll()};</li>
 *   <li>{@code poll()} проверяет {@code reader.ready()} и читает строку,
 *       только если данные доступны;</li>
 *   <li>иначе сразу возвращается.</li>
 * </ol>
 *
 * <p>В ОС, где {@code System.in.ready()} ведёт себя ненадёжно (Windows
 * через IDE), допустимо завершать сервер только по Ctrl+C — при этом
 * обязательно работает shutdown hook ({@link ServerContext#shutdown()}).
 */
public final class ServerConsole {

    private static final Logger LOG = Logger.getLogger(ServerConsole.class.getName());

    private final BufferedReader reader;
    private final PrintStream out;
    private final ServerContext context;
    private final ServerOnlySaveCommand saveCommand;

    /**
     * @param in      входной поток (обычно {@code System.in})
     * @param out     вывод (обычно {@code System.out})
     * @param context контекст сервера
     */
    public ServerConsole(InputStream in, PrintStream out, ServerContext context) {
        this.reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        this.out = out;
        this.context = context;
        this.saveCommand = new ServerOnlySaveCommand(context);
    }

    /**
     * Не блокирующее чтение строки (если есть данные).
     */
    public void poll() {
        try {
            if (!reader.ready()) {
                return;
            }
            String line = reader.readLine();
            if (line != null) {
                handle(line.trim());
            }
        } catch (IOException e) {
            LOG.warning(() -> "Console read failed: " + e.getMessage());
        }
    }

    private void handle(String line) {
        switch (line) {
            case "save" -> out.println(saveCommand.run());
            case "exit" -> {
                out.println("Завершение сервера...");
                context.shutdown();
            }
            case "" -> {
                // ignore
            }
            default -> out.println("Серверная команда не распознана: " + line);
        }
    }
}

package server.console;

import server.command.handlers.ServerOnlySaveCommand;
import server.core.ServerContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public final class ServerConsole {

    private static final Logger LOG = Logger.getLogger(ServerConsole.class.getName());

    private final BufferedReader reader;
    private final PrintStream out;
    private final ServerContext context;
    private final ServerOnlySaveCommand saveCommand;

    public ServerConsole(InputStream in, PrintStream out, ServerContext context) {
        this.reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        this.out = out;
        this.context = context;
        this.saveCommand = new ServerOnlySaveCommand(context);
    }

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
            case "" -> { }
            default -> out.println("Серверная команда не распознана: " + line);
        }
    }
}

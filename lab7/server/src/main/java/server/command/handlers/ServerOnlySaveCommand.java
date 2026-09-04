package server.command.handlers;

import server.core.ServerContext;

public final class ServerOnlySaveCommand {

    private final ServerContext context;

    public ServerOnlySaveCommand(ServerContext context) {
        this.context = context;
    }

    public String run() {
        return "Коллекция хранится в БД, отдельное сохранение не требуется.";
    }
}

package server.core;

import server.command.CommandRegistry;
import server.storage.PostgresStorage;

import java.util.logging.Logger;

public final class ServerContext {

    private static final Logger LOG = Logger.getLogger(ServerContext.class.getName());

    private final OrganizationRepository repository;
    private final PostgresStorage storage;
    private final CommandRegistry registry;

    private volatile boolean shuttingDown;

    public ServerContext(OrganizationRepository repository,
                         PostgresStorage storage,
                         CommandRegistry registry) {
        this.repository = repository;
        this.storage = storage;
        this.registry = registry;
    }

    public OrganizationRepository repository() { return repository; }
    public PostgresStorage storage() { return storage; }
    public CommandRegistry registry() { return registry; }

    public boolean isShuttingDown() {
        return shuttingDown;
    }

    public void requestShutdown() {
        this.shuttingDown = true;
    }

    public void shutdown() {
        LOG.info("Shutdown requested");
        storage.close();
        requestShutdown();
    }
}

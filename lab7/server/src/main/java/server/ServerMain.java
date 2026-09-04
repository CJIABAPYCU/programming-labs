package server;

import server.command.CommandRegistry;
import server.command.CommandRegistryFactory;
import server.console.ServerConsole;
import server.core.OrganizationRepository;
import server.core.ServerContext;
import server.logging.LoggingSetup;
import server.network.UdpServer;
import server.storage.DatabaseConfig;
import server.storage.PostgresStorage;

import java.util.logging.Logger;

public final class ServerMain {

    private static final Logger LOG = Logger.getLogger(ServerMain.class.getName());

    private ServerMain() {
    }

    public static void main(String[] args) {
        LoggingSetup.init();
        if (args.length < 1) {
            System.err.println("Использование: java -jar server.jar <port> [db-url] [db-user] [db-password]");
            System.exit(1);
        }

        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("Порт должен быть целым числом.");
            System.exit(1);
            return;
        }

        DatabaseConfig dbConfig = DatabaseConfig.fromArgs(args);
        PostgresStorage storage;
        try {
            storage = new PostgresStorage(dbConfig);
            storage.initSchema();
        } catch (Exception e) {
            System.err.println("Не удалось подключиться к БД: " + e.getMessage());
            System.exit(1);
            return;
        }

        OrganizationRepository repository = new OrganizationRepository();
        repository.load(storage.loadAll());

        CommandRegistry registry = new CommandRegistry();
        CommandRegistryFactory.registerAll(registry);

        ServerContext context = new ServerContext(repository, storage, registry);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!context.isShuttingDown()) {
                storage.close();
            }
        }));

        ServerConsole console = new ServerConsole(System.in, System.out, context);
        UdpServer server = new UdpServer(port, context, console);
        LOG.info(() -> "Starting server on UDP port " + port);
        server.run();
    }
}

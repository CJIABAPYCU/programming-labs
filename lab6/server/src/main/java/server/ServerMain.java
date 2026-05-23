package server;

import common.model.Organization;
import server.command.CommandRegistry;
import server.command.CommandRegistryFactory;
import server.console.ServerConsole;
import server.core.IdProvider;
import server.core.OrganizationRepository;
import server.core.ServerContext;
import server.logging.LoggingSetup;
import server.network.UdpServer;
import server.storage.CsvStorage;

import java.util.LinkedHashMap;
import java.util.logging.Logger;

/**
 * Server application entry point.
 */
public final class ServerMain {

    private static final Logger LOG = Logger.getLogger(ServerMain.class.getName());

    private ServerMain() {
    }

    /**
     * Starts the UDP server.
     *
     * @param args {@code <port> <csv-file>}
     */
    public static void main(String[] args) {
        LoggingSetup.init();
        if (args.length < 2) {
            System.err.println("Использование: java -jar server.jar <port> <csv-file>");
            System.exit(1);
        }

        int port;
        try {
            port = Integer.parseInt(args[0]);
            if (port < 1 || port > 65_535) {
                throw new NumberFormatException("port out of range");
            }
        } catch (NumberFormatException e) {
            System.err.println("Порт должен быть целым числом от 1 до 65535.");
            System.exit(1);
            return;
        }

        String fileName = args[1];
        CsvStorage storage = new CsvStorage();
        LinkedHashMap<Integer, Organization> loaded = storage.load(fileName);
        OrganizationRepository repository = new OrganizationRepository();
        repository.load(loaded);

        IdProvider idProvider = new IdProvider();
        idProvider.sync(repository.snapshot());

        CommandRegistry registry = new CommandRegistry();
        CommandRegistryFactory.registerAll(registry);

        ServerContext context = new ServerContext(repository, idProvider, storage, fileName, registry);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!context.isShuttingDown()) {
                context.saveCollectionSilently();
            }
        }));

        ServerConsole console = new ServerConsole(System.in, System.out, context);
        UdpServer server = new UdpServer(port, context, console);
        LOG.info(() -> "Starting server with file " + fileName + " on UDP port " + port);
        server.run();
    }
}

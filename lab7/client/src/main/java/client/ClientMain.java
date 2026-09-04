package client;

import client.command.ClientCommandRegistry;
import client.command.ClientCommandRegistryFactory;
import client.console.OrganizationReader;
import client.console.TerminalConsole;
import client.console.TextIO;
import client.network.ServerEndpoint;
import client.network.UdpClient;
import client.runner.CliRunner;
import client.runner.ScriptExecutor;

import java.net.InetAddress;

/**
 * Точка входа клиентского приложения.
 */
public final class ClientMain {

    private ClientMain() {
    }

    /**
     * Запускает интерактивный клиент.
     *
     * @param args аргументы командной строки в формате {@code <host> <port>}
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Использование: java -jar client.jar <host> <port>");
            System.exit(1);
        }
        try {
            String host = args[0];
            int port = Integer.parseInt(args[1]);
            if (port < 1 || port > 65_535) {
                throw new NumberFormatException("port out of range");
            }
            ServerEndpoint endpoint = new ServerEndpoint(InetAddress.getByName(host), port);
            try (UdpClient udp = new UdpClient(endpoint)) {
                TextIO console = new TerminalConsole();
                OrganizationReader reader = new OrganizationReader(console);
                ClientCommandRegistry registry = new ClientCommandRegistry();
                ClientCommandRegistryFactory.registerAll(registry);
                CliRunner runner = new CliRunner(console, registry, udp, reader);
                ScriptExecutor scripts = new ScriptExecutor(console, runner);
                runner.setScriptExecutor(scripts);
                runner.run();
            }
        } catch (Exception e) {
            System.err.println("Не удалось запустить клиент: " + e.getMessage());
            System.exit(1);
        }
    }
}

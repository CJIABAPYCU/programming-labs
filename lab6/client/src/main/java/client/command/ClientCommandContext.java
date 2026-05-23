package client.command;

import client.console.OrganizationReader;
import client.console.TextIO;
import client.network.UdpClient;
import client.runner.ScriptExecutor;

/**
 * Контекст клиентской команды (DI-контейнер).
 *
 * <p>Передаётся в {@link ClientCommand#execute(String, ClientCommandContext)}.
 * Содержит ровно те зависимости, которые нужны клиентским командам:
     * консоль, читатель организации, UDP-клиент, реестр, исполнитель скриптов.
 */
public final class ClientCommandContext {

    private final TextIO console;
    private final OrganizationReader reader;
    private final UdpClient udpClient;
    private final ClientCommandRegistry registry;
    private ScriptExecutor scriptExecutor;

    /**
     * @param console     консоль
     * @param reader      читатель организации
     * @param udpClient   UDP-клиент
     * @param registry    реестр команд (для help/execute_script)
     */
    public ClientCommandContext(TextIO console,
                                OrganizationReader reader,
                                UdpClient udpClient,
                                ClientCommandRegistry registry) {
        this.console = console;
        this.reader = reader;
        this.udpClient = udpClient;
        this.registry = registry;
    }

    public TextIO console()                  { return console; }
    public OrganizationReader reader()       { return reader; }
    public UdpClient udpClient()             { return udpClient; }
    public ClientCommandRegistry registry()  { return registry; }
    public ScriptExecutor scriptExecutor()   { return scriptExecutor; }

    /**
     * Поздняя инъекция исполнителя скриптов (cyclic dep: ScriptExecutor → CliRunner → ctx).
     *
     * @param scriptExecutor исполнитель скриптов
     */
    public void setScriptExecutor(ScriptExecutor scriptExecutor) {
        this.scriptExecutor = scriptExecutor;
    }
}

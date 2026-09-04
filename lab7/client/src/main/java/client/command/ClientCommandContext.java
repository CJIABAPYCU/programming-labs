package client.command;

import client.console.OrganizationReader;
import client.console.TextIO;
import client.network.UdpClient;
import client.runner.ScriptExecutor;
import common.auth.Credentials;

public final class ClientCommandContext {

    private final TextIO console;
    private final OrganizationReader reader;
    private final UdpClient udpClient;
    private final ClientCommandRegistry registry;
    private ScriptExecutor scriptExecutor;
    private Credentials credentials;

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
    public Credentials credentials()         { return credentials; }

    public void setScriptExecutor(ScriptExecutor scriptExecutor) {
        this.scriptExecutor = scriptExecutor;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}

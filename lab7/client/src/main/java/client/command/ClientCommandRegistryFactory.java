package client.command;

import client.command.parsers.ClearCommand;
import client.command.parsers.CountLessThanTypeCommand;
import client.command.parsers.ExecuteScriptCommand;
import client.command.parsers.ExitCommand;
import client.command.parsers.FilterContainsNameCommand;
import client.command.parsers.HelpCommand;
import client.command.parsers.InfoCommand;
import client.command.parsers.InsertCommand;
import client.command.parsers.LoginCommand;
import client.command.parsers.PrintAscendingCommand;
import client.command.parsers.RegisterCommand;
import client.command.parsers.RemoveGreaterCommand;
import client.command.parsers.RemoveGreaterKeyCommand;
import client.command.parsers.RemoveKeyCommand;
import client.command.parsers.ReplaceIfLoweCommand;
import client.command.parsers.ShowCommand;
import client.command.parsers.UpdateCommand;

public final class ClientCommandRegistryFactory {

    private ClientCommandRegistryFactory() {
    }

    public static void registerAll(ClientCommandRegistry registry) {
        registry.register(new RegisterCommand());
        registry.register(new LoginCommand());
        registry.register(new HelpCommand());
        registry.register(new InfoCommand());
        registry.register(new ShowCommand());
        registry.register(new InsertCommand());
        registry.register(new UpdateCommand());
        registry.register(new RemoveKeyCommand());
        registry.register(new ClearCommand());
        registry.register(new ExecuteScriptCommand());
        registry.register(new ExitCommand());
        registry.register(new RemoveGreaterCommand());
        registry.register(new ReplaceIfLoweCommand());
        registry.register(new RemoveGreaterKeyCommand());
        registry.register(new CountLessThanTypeCommand());
        registry.register(new FilterContainsNameCommand());
        registry.register(new PrintAscendingCommand());
    }
}

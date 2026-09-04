package server.command;

import server.command.handlers.ClearHandler;
import server.command.handlers.CountLessThanTypeHandler;
import server.command.handlers.FilterContainsNameHandler;
import server.command.handlers.InfoHandler;
import server.command.handlers.InsertHandler;
import server.command.handlers.LoginHandler;
import server.command.handlers.PrintAscendingHandler;
import server.command.handlers.RegisterHandler;
import server.command.handlers.RemoveGreaterHandler;
import server.command.handlers.RemoveGreaterKeyHandler;
import server.command.handlers.RemoveKeyHandler;
import server.command.handlers.ReplaceIfLoweHandler;
import server.command.handlers.ShowHandler;
import server.command.handlers.UpdateHandler;

public final class CommandRegistryFactory {

    private CommandRegistryFactory() {
    }

    public static void registerAll(CommandRegistry registry) {
        registry.register(new RegisterHandler());
        registry.register(new LoginHandler());
        registry.register(new InfoHandler());
        registry.register(new ShowHandler());
        registry.register(new InsertHandler());
        registry.register(new UpdateHandler());
        registry.register(new RemoveKeyHandler());
        registry.register(new ClearHandler());
        registry.register(new RemoveGreaterHandler());
        registry.register(new ReplaceIfLoweHandler());
        registry.register(new RemoveGreaterKeyHandler());
        registry.register(new CountLessThanTypeHandler());
        registry.register(new FilterContainsNameHandler());
        registry.register(new PrintAscendingHandler());
    }
}

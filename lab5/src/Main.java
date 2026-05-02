import commands.CommandDispatcher;
import commands.impl.Add;
import commands.impl.Clear;
import commands.impl.ExecuteScript;
import commands.impl.Exit;
import commands.impl.FilterContainsName;
import commands.impl.Head;
import commands.impl.Help;
import commands.impl.History;
import commands.impl.Info;
import commands.impl.MaxByCoordinates;
import commands.impl.PrintOfficialAddressDescending;
import commands.impl.RemoveById;
import commands.impl.RemoveHead;
import commands.impl.Save;
import commands.impl.Show;
import commands.impl.Update;
import console.TextIO;
import console.OrganizationReader;
import console.TerminalConsole;
import core.ApplicationContext;
import core.OrganizationRepository;
import core.IdProvider;
import io.OrganizationCsvStorage;
import io.EnvConfig;
import model.Organization;
import runner.CliRunner;
import runner.ScriptExecutor;

import java.util.List;

/**
 * Точка входа приложения.
 */
public class Main {
    /**
     * Запрещает создание экземпляров служебного класса.
     */
    private Main() {
    }

    /**
     * Точка входа программы.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        TextIO console = new TerminalConsole();
        String fileName = EnvConfig.getFileName();
        OrganizationCsvStorage storage = new OrganizationCsvStorage(console);
        OrganizationRepository repository = new OrganizationRepository(console);
        List<Organization> loaded = storage.load(fileName);
        repository.load(loaded);

        IdProvider idProvider = new IdProvider();
        try {
            idProvider.sync(repository.list());
        } catch (IllegalStateException e) {
            console.printError(e.getMessage());
            return;
        }

        OrganizationReader reader = new OrganizationReader(console);
        ApplicationContext context = new ApplicationContext(console, repository, storage, idProvider, reader, fileName);

        CommandDispatcher dispatcher = new CommandDispatcher();
        context.setDispatcher(dispatcher);

        CliRunner runner = new CliRunner(console, dispatcher, context);
        ScriptExecutor scriptExecutor = new ScriptExecutor(console, runner);
        context.setScriptExecutor(scriptExecutor);

        registerCommands(dispatcher);

        runner.run();
    }

    private static void registerCommands(CommandDispatcher dispatcher) {
        dispatcher.register(new Help());
        dispatcher.register(new Info());
        dispatcher.register(new Show());
        dispatcher.register(new Add());
        dispatcher.register(new Update());
        dispatcher.register(new RemoveById());
        dispatcher.register(new Clear());
        dispatcher.register(new Save());
        dispatcher.register(new ExecuteScript());
        dispatcher.register(new Exit());
        dispatcher.register(new Head());
        dispatcher.register(new RemoveHead());
        dispatcher.register(new History());
        dispatcher.register(new MaxByCoordinates());
        dispatcher.register(new FilterContainsName());
        dispatcher.register(new PrintOfficialAddressDescending());
    }
}

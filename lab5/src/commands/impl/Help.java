package commands.impl;

import commands.CommandAction;
import commands.CommandOutcome;
import core.ApplicationContext;

import java.util.Map;

/**
 * Выводит справку по всем доступным командам.
 */
public class Help implements CommandAction {
    /**
     * Создает команду справки.
     */
    public Help() {
    }

    @Override
    public String keyword() {
        return "help";
    }

    @Override
    public String summary() {
        return "вывести справку по командам";
    }

    @Override
    public CommandOutcome run(String args, ApplicationContext context) {
        if (args != null && !args.isEmpty()) {
            return new CommandOutcome(false, "Команда 'help' не принимает аргументы.", false);
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, CommandAction> entry : context.dispatcher().commands().entrySet()) {
            sb.append(entry.getKey()).append(" : ").append(entry.getValue().summary())
                    .append(System.lineSeparator());
        }
        return new CommandOutcome(true, sb.toString().trim(), false);
    }
}

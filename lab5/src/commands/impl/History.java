package commands.impl;

import commands.CommandAction;
import commands.CommandOutcome;
import core.ApplicationContext;

/**
 * Выводит историю команд.
 */
public class History implements CommandAction {
    /**
     * Создает команду вывода истории.
     */
    public History() {
    }

    @Override
    public String keyword() {
        return "history";
    }

    @Override
    public String summary() {
        return "вывести последние 6 команд";
    }

    @Override
    public CommandOutcome run(String args, ApplicationContext context) {
        if (args != null && !args.isEmpty()) {
            return new CommandOutcome(false, "Команда 'history' не принимает аргументы.", false);
        }
        StringBuilder sb = new StringBuilder();
        for (String cmd : context.dispatcher().history()) {
            sb.append(cmd).append(System.lineSeparator());
        }
        if (sb.length() == 0) {
            return new CommandOutcome(true, "История пуста.", false);
        }
        return new CommandOutcome(true, sb.toString().trim(), false);
    }
}

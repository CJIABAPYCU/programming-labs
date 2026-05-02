package commands.impl;

import commands.CommandAction;
import commands.CommandOutcome;
import core.ApplicationContext;
import model.Organization;

import java.util.List;

/**
 * Выводит все элементы коллекции.
 */
public class Show implements CommandAction {
    /**
     * Создает команду вывода всех элементов.
     */
    public Show() {
    }

    @Override
    public String keyword() {
        return "show";
    }

    @Override
    public String summary() {
        return "вывести все элементы";
    }

    @Override
    public CommandOutcome run(String args, ApplicationContext context) {
        if (args != null && !args.isEmpty()) {
            return new CommandOutcome(false, "Команда 'show' не принимает аргументы.", false);
        }
        List<Organization> all = context.repository().list();
        if (all.isEmpty()) {
            return new CommandOutcome(true, "Коллекция пуста.", false);
        }
        StringBuilder sb = new StringBuilder();
        for (Organization org : all) {
            sb.append(org).append(System.lineSeparator());
        }
        return new CommandOutcome(true, sb.toString().trim(), false);
    }
}

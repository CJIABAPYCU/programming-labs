package commands.impl;

import commands.CommandAction;
import commands.CommandOutcome;
import core.ApplicationContext;
import model.Organization;

import java.util.List;

/**
 * Фильтрует элементы, чье название содержит подстроку.
 */
public class FilterContainsName implements CommandAction {
    /**
     * Создает команду фильтрации по имени.
     */
    public FilterContainsName() {
    }

    @Override
    public String keyword() {
        return "filter_contains_name";
    }

    @Override
    public String summary() {
        return "вывести элементы, name которых содержит подстроку";
    }

    @Override
    public CommandOutcome run(String args, ApplicationContext context) {
        if (args == null || args.isEmpty()) {
            return new CommandOutcome(false, "Команда 'filter_contains_name' требует подстроку.", false);
        }
        List<Organization> all = context.repository().list();
        StringBuilder sb = new StringBuilder();
        for (Organization org : all) {
            if (org.getName().contains(args)) {
                sb.append(org).append(System.lineSeparator());
            }
        }
        if (sb.length() == 0) {
            return new CommandOutcome(true, "Совпадений нет.", false);
        }
        return new CommandOutcome(true, sb.toString().trim(), false);
    }
}

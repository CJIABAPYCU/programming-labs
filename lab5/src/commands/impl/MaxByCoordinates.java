package commands.impl;

import commands.CommandAction;
import commands.CommandOutcome;
import core.ApplicationContext;
import model.Organization;

import java.util.List;

/**
 * Выводит элемент с максимальным значением координат.
 */
public class MaxByCoordinates implements CommandAction {
    /**
     * Создает команду поиска максимума по координатам.
     */
    public MaxByCoordinates() {
    }

    @Override
    public String keyword() {
        return "max_by_coordinates";
    }

    @Override
    public String summary() {
        return "вывести элемент с максимальными coordinates";
    }

    @Override
    public CommandOutcome run(String args, ApplicationContext context) {
        if (args != null && !args.isEmpty()) {
            return new CommandOutcome(false, "Команда 'max_by_coordinates' не принимает аргументы.", false);
        }
        List<Organization> all = context.repository().list();
        if (all.isEmpty()) {
            return new CommandOutcome(true, "Коллекция пуста.", false);
        }
        Organization max = all.get(0);
        for (Organization org : all) {
            if (org.getCoordinates().compareTo(max.getCoordinates()) > 0) {
                max = org;
            }
        }
        return new CommandOutcome(true, max.toString(), false);
    }
}

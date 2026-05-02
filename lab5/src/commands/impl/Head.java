package commands.impl;

import commands.CommandAction;
import commands.CommandOutcome;
import core.ApplicationContext;
import model.Organization;

/**
 * Выводит первый элемент коллекции.
 */
public class Head implements CommandAction {
    /**
     * Создает команду вывода первого элемента.
     */
    public Head() {
    }

    @Override
    public String keyword() {
        return "head";
    }

    @Override
    public String summary() {
        return "вывести первый элемент";
    }

    @Override
    public CommandOutcome run(String args, ApplicationContext context) {
        if (args != null && !args.isEmpty()) {
            return new CommandOutcome(false, "Команда 'head' не принимает аргументы.", false);
        }
        Organization head = context.repository().first();
        if (head == null) {
            return new CommandOutcome(true, "Коллекция пуста.", false);
        }
        return new CommandOutcome(true, head.toString(), false);
    }
}

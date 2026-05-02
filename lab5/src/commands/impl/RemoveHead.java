package commands.impl;

import commands.CommandAction;
import commands.CommandOutcome;
import core.ApplicationContext;
import model.Organization;

/**
 * Удаляет и выводит первый элемент коллекции.
 */
public class RemoveHead implements CommandAction {
    /**
     * Создает команду удаления первого элемента.
     */
    public RemoveHead() {
    }

    @Override
    public String keyword() {
        return "remove_head";
    }

    @Override
    public String summary() {
        return "удалить и вывести первый элемент";
    }

    @Override
    public CommandOutcome run(String args, ApplicationContext context) {
        if (args != null && !args.isEmpty()) {
            return new CommandOutcome(false, "Команда 'remove_head' не принимает аргументы.", false);
        }
        Organization head = context.repository().removeFirst();
        if (head == null) {
            return new CommandOutcome(true, "Коллекция пуста.", false);
        }
        return new CommandOutcome(true, "Удалено: " + head, false);
    }
}

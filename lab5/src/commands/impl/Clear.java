package commands.impl;

import commands.CommandAction;
import commands.CommandOutcome;
import core.ApplicationContext;

/**
 * Очищает коллекцию.
 */
public class Clear implements CommandAction {
    /**
     * Создает команду очистки коллекции.
     */
    public Clear() {
    }

    @Override
    public String keyword() {
        return "clear";
    }

    @Override
    public String summary() {
        return "очистить коллекцию";
    }

    @Override
    public CommandOutcome run(String args, ApplicationContext context) {
        if (args != null && !args.isEmpty()) {
            return new CommandOutcome(false, "Команда 'clear' не принимает аргументы.", false);
        }
        context.repository().clear();
        return new CommandOutcome(true, "Коллекция очищена.", false);
    }
}

package commands.impl;

import commands.CommandAction;
import commands.CommandOutcome;
import core.ApplicationContext;

/**
 * Сохраняет коллекцию в файл.
 */
public class Save implements CommandAction {
    /**
     * Создает команду сохранения коллекции.
     */
    public Save() {
    }

    @Override
    public String keyword() {
        return "save";
    }

    @Override
    public String summary() {
        return "сохранить коллекцию в файл";
    }

    @Override
    public CommandOutcome run(String args, ApplicationContext context) {
        if (args != null && !args.isEmpty()) {
            return new CommandOutcome(false, "Команда 'save' не принимает аргументы.", false);
        }
        boolean saved = context.storage().save(context.fileName(), context.repository().items());
        if (!saved) {
            return new CommandOutcome(false, "Сохранение не выполнено.", false);
        }
        context.repository().markSavedNow();
        return new CommandOutcome(true, "Сохранение выполнено.", false);
    }
}

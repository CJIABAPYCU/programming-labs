package commands.impl;

import commands.CommandAction;
import commands.CommandOutcome;
import core.ApplicationContext;
import model.Organization;

/**
 * Удаляет элемент по id.
 */
public class RemoveById implements CommandAction {
    /**
     * Создает команду удаления по идентификатору.
     */
    public RemoveById() {
    }

    @Override
    public String keyword() {
        return "remove_by_id";
    }

    @Override
    public String summary() {
        return "удалить элемент по id";
    }

    @Override
    public CommandOutcome run(String args, ApplicationContext context) {
        if (args == null || args.isEmpty()) {
            return new CommandOutcome(false, "Команда 'remove_by_id' требует id.", false);
        }
        long id;
        try {
            id = Long.parseLong(args.trim());
        } catch (NumberFormatException e) {
            return new CommandOutcome(false, "Неверный формат id.", false);
        }
        Organization removed = context.repository().deleteById(id);
        if (removed == null) {
            return new CommandOutcome(false, "Организация с id " + id + " не найдена.", false);
        }
        return new CommandOutcome(true, "Удалено: " + removed, false);
    }
}

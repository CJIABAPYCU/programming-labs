package commands.impl;

import commands.CommandAction;
import commands.CommandOutcome;
import console.InputAbortException;
import core.ApplicationContext;
import model.Organization;

/**
 * Обновляет элемент по id.
 */
public class Update implements CommandAction {
    /**
     * Создает команду обновления элемента.
     */
    public Update() {
    }

    @Override
    public String keyword() {
        return "update";
    }

    @Override
    public String summary() {
        return "обновить элемент по id";
    }

    @Override
    public CommandOutcome run(String args, ApplicationContext context) {
        if (args == null || args.isEmpty()) {
            return new CommandOutcome(false, "Команда 'update' требует id.", false);
        }
        long id;
        try {
            id = Long.parseLong(args.trim());
        } catch (NumberFormatException e) {
            return new CommandOutcome(false, "Неверный формат id.", false);
        }
        Organization existing = context.repository().findById(id);
        if (existing == null) {
            return new CommandOutcome(false, "Организация с id " + id + " не найдена.", false);
        }
        try {
            Organization updated = context.reader()
                    .readForUpdate(existing.getId(), existing.getCreationDate());
            boolean ok = context.repository().update(updated);
            if (!ok) {
                return new CommandOutcome(false, "Не удалось обновить организацию.", false);
            }
            return new CommandOutcome(true, "Организация обновлена.", false);
        } catch (InputAbortException e) {
            return new CommandOutcome(false, "Ввод прерван при обновлении.", false);
        }
    }
}

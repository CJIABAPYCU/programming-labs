package commands.impl;

import commands.CommandAction;
import commands.CommandOutcome;
import console.InputAbortException;
import core.ApplicationContext;
import model.Organization;

/**
 * Добавляет новый элемент в коллекцию.
 */
public class Add implements CommandAction {
    /**
     * Создает команду добавления.
     */
    public Add() {
    }

    @Override
    public String keyword() {
        return "add";
    }

    @Override
    public String summary() {
        return "добавить новый элемент";
    }

    @Override
    public CommandOutcome run(String args, ApplicationContext context) {
        if (args != null && !args.isEmpty()) {
            return new CommandOutcome(false, "Команда 'add' не принимает аргументы.", false);
        }
        try {
            int id = context.idProvider().nextId();
            Organization org = context.reader().readNew(id);
            if (!org.validate()) {
                return new CommandOutcome(false, "Поля Organization невалидны.", false);
            }
            boolean added = context.repository().add(org);
            if (!added) {
                return new CommandOutcome(false, "Не удалось добавить организацию.", false);
            }
            return new CommandOutcome(true, "Организация добавлена.", false);
        } catch (InputAbortException e) {
            return new CommandOutcome(false, "Ввод прерван при добавлении.", false);
        } catch (IllegalStateException e) {
            return new CommandOutcome(false, e.getMessage(), false);
        }
    }
}

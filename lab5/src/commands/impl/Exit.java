package commands.impl;

import commands.CommandAction;
import commands.CommandOutcome;
import core.ApplicationContext;

/**
 * Завершает программу без сохранения.
 */
public class Exit implements CommandAction {
    /**
     * Создает команду завершения программы.
     */
    public Exit() {
    }

    @Override
    public String keyword() {
        return "exit";
    }

    @Override
    public String summary() {
        return "завершить программу без сохранения";
    }

    @Override
    public CommandOutcome run(String args, ApplicationContext context) {
        if (args != null && !args.isEmpty()) {
            return new CommandOutcome(false, "Команда 'exit' не принимает аргументы.", false);
        }
        return new CommandOutcome(true, "Выход без сохранения.", true);
    }
}

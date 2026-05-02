package commands.impl;

import commands.CommandAction;
import commands.CommandOutcome;
import core.ApplicationContext;

/**
 * Выполняет команды из файла скрипта.
 */
public class ExecuteScript implements CommandAction {
    /**
     * Создает команду запуска скрипта.
     */
    public ExecuteScript() {
    }

    @Override
    public String keyword() {
        return "execute_script";
    }

    @Override
    public String summary() {
        return "исполнить команды из скрипта";
    }

    @Override
    public CommandOutcome run(String args, ApplicationContext context) {
        if (args == null || args.isEmpty()) {
            return new CommandOutcome(false, "Команда 'execute_script' требует имя файла.", false);
        }
        return context.scriptExecutor().execute(args.trim());
    }
}

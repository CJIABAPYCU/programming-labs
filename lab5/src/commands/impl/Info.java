package commands.impl;

import commands.CommandAction;
import commands.CommandOutcome;
import core.ApplicationContext;

import java.time.format.DateTimeFormatter;

/**
 * Выводит информацию о коллекции.
 */
public class Info implements CommandAction {
    /**
     * Создает команду вывода информации о коллекции.
     */
    public Info() {
    }

    @Override
    public String keyword() {
        return "info";
    }

    @Override
    public String summary() {
        return "вывести информацию о коллекции";
    }

    @Override
    public CommandOutcome run(String args, ApplicationContext context) {
        if (args != null && !args.isEmpty()) {
            return new CommandOutcome(false, "Команда 'info' не принимает аргументы.", false);
        }
        String typeName = context.repository().typeName();
        String initTime = context.repository().initTime() == null
                ? "не инициализировано"
                : context.repository().initTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String saveTime = context.repository().lastSaveTime() == null
                ? "не сохранялось"
                : context.repository().lastSaveTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String message = "Тип: " + typeName + System.lineSeparator()
                + "Время инициализации: " + initTime + System.lineSeparator()
                + "Время последнего сохранения: " + saveTime + System.lineSeparator()
                + "Размер: " + context.repository().size();
        return new CommandOutcome(true, message, false);
    }
}

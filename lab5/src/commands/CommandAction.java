package commands;

import core.ApplicationContext;

/**
 * Интерфейс команды.
 */
public interface CommandAction {
    /**
     * Возвращает ключ команды.
     *
     * @return ключ команды
     */
    String keyword();

    /**
     * Возвращает описание команды.
     *
     * @return описание команды
     */
    String summary();

    /**
     * Выполняет команду.
     *
     * @param args аргументы команды
     * @param context контекст приложения
     * @return результат выполнения
     */
    CommandOutcome run(String args, ApplicationContext context);
}

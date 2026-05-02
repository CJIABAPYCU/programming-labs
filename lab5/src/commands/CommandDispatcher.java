package commands;

import core.ApplicationContext;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Хранит и выполняет команды.
 */
public class CommandDispatcher {
    private static final int HISTORY_LIMIT = 6;

    private final Map<String, CommandAction> commands = new LinkedHashMap<>();
    private final Deque<String> history = new ArrayDeque<>();

    /**
     * Создает диспетчер команд.
     */
    public CommandDispatcher() {
    }

    /**
     * Регистрирует команду.
     *
     * @param command команда
     */
    public void register(CommandAction command) {
        commands.put(command.keyword(), command);
    }

    /**
     * Выполняет команду по имени.
     *
     * @param name имя команды
     * @param args аргументы команды
     * @param context контекст приложения
     * @return результат
     */
    public CommandOutcome dispatch(String name, String args, ApplicationContext context) {
        if (name == null || name.isEmpty()) {
            return new CommandOutcome(true, "", false);
        }
        addHistory(name);
        CommandAction command = commands.get(name);
        if (command == null) {
            return new CommandOutcome(false, "Неизвестная команда. Введите 'help' для справки.", false);
        }
        return command.run(args, context);
    }

    /**
     * Возвращает зарегистрированные команды.
     *
     * @return отображение команд
     */
    public Map<String, CommandAction> commands() {
        return commands;
    }

    /**
     * Возвращает историю последних команд.
     *
     * @return список истории
     */
    public List<String> history() {
        return new ArrayList<>(history);
    }

    private void addHistory(String name) {
        if (name == null || name.isEmpty()) {
            return;
        }
        if (history.size() >= HISTORY_LIMIT) {
            history.removeFirst();
        }
        history.addLast(name);
    }
}

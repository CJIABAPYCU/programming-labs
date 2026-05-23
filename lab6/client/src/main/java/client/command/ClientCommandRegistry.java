package client.command;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Реестр клиентских команд.
 *
 * <p>{@link LinkedHashMap} — чтобы порядок регистрации сохранялся
 * (для красивого вывода локального help).
 */
public final class ClientCommandRegistry {

    private final Map<String, ClientCommand> byKeyword = new LinkedHashMap<>();

    /**
     * Создаёт пустой реестр.
     */
    public ClientCommandRegistry() {
    }

    /**
     * Регистрирует команду.
     *
     * @param command команда
     */
    public void register(ClientCommand command) {
        byKeyword.put(command.keyword(), command);
    }

    /**
     * @param keyword ключевое слово
     * @return команда или {@code null}
     */
    public ClientCommand find(String keyword) {
        return byKeyword.get(keyword);
    }

    /**
     * @return все команды в порядке регистрации
     */
    public Collection<ClientCommand> all() {
        return byKeyword.values();
    }
}

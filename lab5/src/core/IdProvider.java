package core;

import model.Organization;

import java.util.Collection;

/**
 * Генерирует уникальные идентификаторы для организации.
 */
public class IdProvider {
    private int currentId = 0;

    /**
     * Создает генератор идентификаторов.
     */
    public IdProvider() {
    }

    /**
     * Синхронизирует генератор с существующей коллекцией.
     *
     * @param collection коллекция с существующими id
     */
    public void sync(Collection<Organization> collection) {
        if (collection.isEmpty()) {
            currentId = 0;
            return;
        }

        boolean[] used = new boolean[collection.size() + 1];
        for (Organization org : collection) {
            int id = org.getId();
            if (id >= 1 && id <= collection.size()) {
                used[id] = true;
            }
        }

        int maxContinuousId = 0;
        for (int id = 1; id < used.length; id++) {
            if (!used[id]) {
                break;
            }
            maxContinuousId = id;
        }

        if (maxContinuousId == 0) {
            throw new IllegalStateException("Не найден непрерывный диапазон id, начинающийся с 1.");
        }

        currentId = maxContinuousId;
    }

    /**
     * Генерирует следующий id.
     *
     * @return новый id
     */
    public int nextId() {
        if (currentId == Integer.MAX_VALUE) {
            throw new IllegalStateException("Невозможно сгенерировать новый id: достигнут предел int.");
        }
        currentId += 1;
        return currentId;
    }
}

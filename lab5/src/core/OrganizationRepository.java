package core;

import console.TextIO;
import model.Organization;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Управляет коллекцией организаций.
 */
public class OrganizationRepository {
    private final ArrayDeque<Organization> collection = new ArrayDeque<>();
    private final TextIO console;
    private LocalDateTime initTime;
    private LocalDateTime lastSaveTime;

    /**
     * Создает менеджер коллекции.
     *
     * @param console консоль для сообщений
     */
    public OrganizationRepository(TextIO console) {
        this.console = console;
    }

    /**
     * Инициализирует коллекцию переданными данными.
     *
     * @param data исходные данные
     */
    public void load(Iterable<Organization> data) {
        collection.clear();
        Set<Integer> ids = new HashSet<>();
        for (Organization org : data) {
            if (org == null || !org.validate()) {
                console.printError("Некорректный объект пропущен: " + org);
                continue;
            }
            if (ids.contains(org.getId())) {
                console.printError("Дублирующийся id пропущен: " + org.getId());
                continue;
            }
            ids.add(org.getId());
            collection.add(org);
        }
        initTime = LocalDateTime.now();
    }

    /**
     * Возвращает количество элементов в коллекции.
     *
     * @return размер коллекции
     */
    public int size() {
        return collection.size();
    }

    /**
     * Возвращает время инициализации коллекции.
     *
     * @return время инициализации
     */
    public LocalDateTime initTime() {
        return initTime;
    }

    /**
     * Возвращает время последнего сохранения коллекции.
     *
     * @return время последнего сохранения
     */
    public LocalDateTime lastSaveTime() {
        return lastSaveTime;
    }

    /**
     * Обновляет время последнего сохранения.
     */
    public void markSavedNow() {
        lastSaveTime = LocalDateTime.now();
    }

    /**
     * Возвращает имя типа используемой коллекции.
     *
     * @return имя типа коллекции
     */
    public String typeName() {
        return collection.getClass().getSimpleName();
    }

    /**
     * Возвращает копию текущей коллекции в виде списка.
     *
     * @return снимок коллекции
     */
    public List<Organization> list() {
        return new ArrayList<>(collection);
    }

    /**
     * Возвращает коллекцию для сохранения в файл.
     *
     * @return представление коллекции для сохранения
     */
    public Iterable<Organization> items() {
        return collection;
    }

    /**
     * Добавляет организацию в коллекцию.
     *
     * @param organization организация
     * @return true, если добавлено
     */
    public boolean add(Organization organization) {
        if (organization == null || !organization.validate()) {
            return false;
        }
        if (findById(organization.getId()) != null) {
            return false;
        }
        collection.add(organization);
        return true;
    }

    /**
     * Обновляет организацию с тем же id.
     *
     * @param organization новая организация
     * @return true, если обновлено
     */
    public boolean update(Organization organization) {
        if (organization == null || !organization.validate()) {
            return false;
        }
        Organization existing = findById(organization.getId());
        if (existing == null) {
            return false;
        }
        collection.remove(existing);
        collection.add(organization);
        return true;
    }

    /**
     * Удаляет организацию по id.
     *
     * @param id id
     * @return удаленная организация или null
     */
    public Organization deleteById(long id) {
        Organization existing = findById(id);
        if (existing == null) {
            return null;
        }
        collection.remove(existing);
        return existing;
    }

    /**
     * Очищает коллекцию.
     */
    public void clear() {
        collection.clear();
    }

    /**
     * Возвращает первый элемент.
     *
     * @return первая организация или null
     */
    public Organization first() {
        return collection.peekFirst();
    }

    /**
     * Удаляет и возвращает первый элемент.
     *
     * @return удаленная организация или null
     */
    public Organization removeFirst() {
        return collection.pollFirst();
    }

    /**
     * Ищет организацию по id.
     *
     * @param id id
     * @return организация или null
     */
    public Organization findById(long id) {
        for (Organization org : collection) {
            if (org.getId() == id) {
                return org;
            }
        }
        return null;
    }

}

package server.core;

import common.model.Organization;
import common.model.OrganizationType;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Server-side collection manager.
 */
public class OrganizationRepository {

    private final LinkedHashMap<Integer, Organization> collection = new LinkedHashMap<>();
    private LocalDateTime initTime;
    private LocalDateTime lastSaveTime;

    /**
     * Loads initial data. Invalid entries and duplicate generated ids are skipped.
     *
     * @param data map loaded from CSV
     */
    public void load(Map<Integer, Organization> data) {
        collection.clear();
        Set<Integer> ids = new HashSet<>();
        if (data != null) {
            data.entrySet().stream()
                    .filter(e -> e.getKey() != null)
                    .filter(e -> e.getValue() != null && e.getValue().validate())
                    .filter(e -> ids.add(e.getValue().getId()))
                    .forEach(e -> collection.put(e.getKey(), e.getValue()));
        }
        initTime = LocalDateTime.now();
    }

    public int size() { return collection.size(); }
    public LocalDateTime initTime() { return initTime; }
    public LocalDateTime lastSaveTime() { return lastSaveTime; }
    public void markSavedNow() { lastSaveTime = LocalDateTime.now(); }
    public String typeName() { return collection.getClass().getSimpleName(); }

    /**
     * @return values in insertion order
     */
    public List<Organization> snapshot() {
        return List.copyOf(collection.values());
    }

    /**
     * @return values sorted by name for transmission to the client
     */
    public List<Organization> snapshotSortedByName() {
        return collection.values().stream()
                .sorted(Comparator.comparing(Organization::getName))
                .collect(Collectors.toList());
    }

    /**
     * @return values sorted by natural order
     */
    public List<Organization> snapshotAscending() {
        return collection.values().stream()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * @return map entries for CSV persistence
     */
    public Set<Map.Entry<Integer, Organization>> entries() {
        return collection.entrySet();
    }

    /**
     * Checks whether a map key exists.
     *
     * @param key map key
     * @return true if key exists
     */
    public boolean containsKey(int key) {
        return collection.containsKey(key);
    }

    /**
     * Inserts a new organization by map key.
     *
     * @param key map key
     * @param organization organization with generated fields already assigned
     * @return true if inserted
     */
    public boolean insert(int key, Organization organization) {
        if (collection.containsKey(key) || organization == null || !organization.validate()) {
            return false;
        }
        if (findById(organization.getId()).isPresent()) {
            return false;
        }
        collection.put(key, organization);
        return true;
    }

    /**
     * Replaces an existing organization found by generated id.
     *
     * @param updated replacement with preserved id and creation date
     * @return true if updated
     */
    public boolean updateById(Organization updated) {
        if (updated == null || !updated.validate()) {
            return false;
        }
        Optional<Map.Entry<Integer, Organization>> existing = findEntryById(updated.getId());
        if (existing.isEmpty()) {
            return false;
        }
        collection.put(existing.get().getKey(), updated);
        return true;
    }

    /**
     * Removes an entry by map key.
     *
     * @param key map key
     * @return removed organization or null
     */
    public Organization removeKey(int key) {
        return collection.remove(key);
    }

    /** Clears the collection. */
    public void clear() {
        collection.clear();
    }

    /**
     * Removes values greater than the given organization.
     *
     * @param sample comparison sample
     * @return number of removed entries
     */
    public int removeGreater(Organization sample) {
        List<Integer> keys = collection.entrySet().stream()
                .filter(e -> e.getValue().compareTo(sample) > 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        keys.forEach(collection::remove);
        return keys.size();
    }

    /**
     * Replaces a value by key when a new value is lower than the current value.
     *
     * @param key map key
     * @param replacement replacement with generated fields
     * @return replacement result
     */
    public ReplaceResult replaceIfLower(int key, Organization replacement) {
        Organization current = collection.get(key);
        if (current == null) {
            return ReplaceResult.KEY_NOT_FOUND;
        }
        if (replacement == null || !replacement.validate()) {
            return ReplaceResult.INVALID_VALUE;
        }
        if (replacement.compareTo(current) < 0) {
            collection.put(key, replacement);
            return ReplaceResult.REPLACED;
        }
        return ReplaceResult.NOT_LOWER;
    }

    /**
     * Removes all entries with key greater than the given key.
     *
     * @param key comparison key
     * @return number of removed entries
     */
    public int removeGreaterKey(int key) {
        List<Integer> keys = collection.keySet().stream()
                .filter(k -> k > key)
                .collect(Collectors.toList());
        keys.forEach(collection::remove);
        return keys.size();
    }

    /**
     * Counts organizations whose type is less than the given type.
     *
     * @param type comparison type
     * @return count
     */
    public long countLessThanType(OrganizationType type) {
        return collection.values().stream()
                .map(Organization::getType)
                .filter(t -> t != null && t.compareTo(type) < 0)
                .count();
    }

    /**
     * Finds organizations by name substring.
     *
     * @param substring substring to search
     * @return matching organizations
     */
    public List<Organization> filterContainsName(String substring) {
        return collection.values().stream()
                .filter(o -> o.getName().contains(substring))
                .collect(Collectors.toList());
    }

    /**
     * Finds an organization by generated id.
     *
     * @param id generated id
     * @return optional organization
     */
    public Optional<Organization> findById(long id) {
        return findEntryById(id).map(Map.Entry::getValue);
    }

    /**
     * Finds an organization by map key.
     *
     * @param key map key
     * @return optional organization
     */
    public Optional<Organization> findByKey(int key) {
        return Optional.ofNullable(collection.get(key));
    }

    private Optional<Map.Entry<Integer, Organization>> findEntryById(long id) {
        return collection.entrySet().stream()
                .filter(e -> e.getValue().getId() == id)
                .findFirst();
    }

    /** Result of replace_if_lowe. */
    public enum ReplaceResult {
        /** Value replaced. */
        REPLACED,
        /** Key does not exist. */
        KEY_NOT_FOUND,
        /** New value is not lower. */
        NOT_LOWER,
        /** Replacement is invalid. */
        INVALID_VALUE
    }
}

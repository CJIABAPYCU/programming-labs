package server.core;

import common.model.Organization;
import common.model.OrganizationType;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class OrganizationRepository {

    private final LinkedHashMap<Integer, Organization> collection = new LinkedHashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private LocalDateTime initTime;

    public void load(Map<Integer, Organization> data) {
        lock.writeLock().lock();
        try {
            collection.clear();
            if (data != null) {
                collection.putAll(data);
            }
            initTime = LocalDateTime.now();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int size() {
        lock.readLock().lock();
        try {
            return collection.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    public LocalDateTime initTime() {
        lock.readLock().lock();
        try {
            return initTime;
        } finally {
            lock.readLock().unlock();
        }
    }

    public String typeName() {
        return collection.getClass().getSimpleName();
    }

    public List<Organization> snapshotSortedByName() {
        lock.readLock().lock();
        try {
            return collection.values().stream()
                    .sorted(Comparator.comparing(Organization::getName))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Organization> snapshotAscending() {
        lock.readLock().lock();
        try {
            return collection.values().stream()
                    .sorted()
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean containsKey(int key) {
        lock.readLock().lock();
        try {
            return collection.containsKey(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean insert(int key, Organization organization) {
        lock.writeLock().lock();
        try {
            if (collection.containsKey(key)) {
                return false;
            }
            collection.put(key, organization);
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean updateById(Organization updated) {
        lock.writeLock().lock();
        try {
            Optional<Map.Entry<Integer, Organization>> existing = findEntryById(updated.getId());
            if (existing.isEmpty()) {
                return false;
            }
            collection.put(existing.get().getKey(), updated);
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Organization removeKey(int key) {
        lock.writeLock().lock();
        try {
            return collection.remove(key);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void removeByOwner(String owner) {
        lock.writeLock().lock();
        try {
            collection.entrySet().removeIf(e ->
                    owner.equals(e.getValue().getOwnerLogin()));
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int removeGreater(Organization sample, String owner) {
        lock.writeLock().lock();
        try {
            List<Integer> keys = collection.entrySet().stream()
                    .filter(e -> owner.equals(e.getValue().getOwnerLogin()))
                    .filter(e -> e.getValue().compareTo(sample) > 0)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            keys.forEach(collection::remove);
            return keys.size();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public ReplaceResult replaceIfLower(int key, Organization replacement) {
        lock.writeLock().lock();
        try {
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
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int removeGreaterKey(int key, String owner) {
        lock.writeLock().lock();
        try {
            List<Integer> keys = collection.entrySet().stream()
                    .filter(e -> e.getKey() > key)
                    .filter(e -> owner.equals(e.getValue().getOwnerLogin()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            keys.forEach(collection::remove);
            return keys.size();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public long countLessThanType(OrganizationType type) {
        lock.readLock().lock();
        try {
            return collection.values().stream()
                    .map(Organization::getType)
                    .filter(t -> t != null && t.compareTo(type) < 0)
                    .count();
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Organization> filterContainsName(String substring) {
        lock.readLock().lock();
        try {
            return collection.values().stream()
                    .filter(o -> o.getName().contains(substring))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public Optional<Organization> findById(long id) {
        lock.readLock().lock();
        try {
            return findEntryById(id).map(Map.Entry::getValue);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Optional<Organization> findByKey(int key) {
        lock.readLock().lock();
        try {
            return Optional.ofNullable(collection.get(key));
        } finally {
            lock.readLock().unlock();
        }
    }

    public Optional<String> ownerOfKey(int key) {
        lock.readLock().lock();
        try {
            Organization org = collection.get(key);
            return org == null ? Optional.empty() : Optional.ofNullable(org.getOwnerLogin());
        } finally {
            lock.readLock().unlock();
        }
    }

    public Optional<String> ownerOfId(long id) {
        lock.readLock().lock();
        try {
            return findEntryById(id).map(e -> e.getValue().getOwnerLogin());
        } finally {
            lock.readLock().unlock();
        }
    }

    // возвращает id организаций владельца, которые > sample
    public List<Integer> findGreaterIds(Organization sample, String owner) {
        lock.readLock().lock();
        try {
            return collection.values().stream()
                    .filter(o -> owner.equals(o.getOwnerLogin()))
                    .filter(o -> o.compareTo(sample) > 0)
                    .map(Organization::getId)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    // возвращает id организаций владельца с ключом > key
    public List<Integer> findGreaterKeyIds(int key, String owner) {
        lock.readLock().lock();
        try {
            return collection.entrySet().stream()
                    .filter(e -> e.getKey() > key)
                    .filter(e -> owner.equals(e.getValue().getOwnerLogin()))
                    .map(e -> e.getValue().getId())
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    private Optional<Map.Entry<Integer, Organization>> findEntryById(long id) {
        return collection.entrySet().stream()
                .filter(e -> e.getValue().getId() == id)
                .findFirst();
    }

    public enum ReplaceResult {
        REPLACED,
        KEY_NOT_FOUND,
        NOT_LOWER,
        INVALID_VALUE
    }
}

package server.core;

import common.model.Organization;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Generates the first free positive id on the server.
 */
public class IdProvider {

    private final Set<Integer> usedIds = new HashSet<>();

    /**
     * Synchronizes the generator with current organizations.
     *
     * @param collection current organizations
     */
    public void sync(Collection<Organization> collection) {
        usedIds.clear();
        if (collection == null) {
            return;
        }
        collection.stream()
                .filter(o -> o != null && o.getId() != null && o.getId() > 0)
                .map(Organization::getId)
                .forEach(usedIds::add);
    }

    /**
     * Reserves and returns the first positive id that is not currently used.
     *
     * @return first free positive id
     */
    public int nextId() {
        for (int candidate = 1; candidate < Integer.MAX_VALUE; candidate++) {
            if (!usedIds.contains(candidate)) {
                usedIds.add(candidate);
                return candidate;
            }
        }
        throw new IllegalStateException("Невозможно сгенерировать новый id: свободные int id закончились.");
    }

    /**
     * Releases a reserved id when a command failed before inserting the value.
     *
     * @param id id to release
     */
    public void release(int id) {
        usedIds.remove(id);
    }
}

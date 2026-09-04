package common.network.payload;

import common.model.Organization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Payload со списком организаций.
 *
 * <p>Используется для:
 * <ul>
 *   <li>{@code SHOW} — список <b>отсортирован по {@code name}</b>;</li>
 *   <li>{@code FILTER_CONTAINS_NAME} — отфильтрованный список.</li>
 * </ul>
 *
 * <p>На сервере список собирается через {@code Stream API} и передаётся
 * клиенту как неизменяемое представление.
 */
public final class OrganizationsPayload implements ResponsePayload {

    private static final long serialVersionUID = 1L;

    private final List<Organization> organizations;

    /**
     * @param organizations исходный список (копируется)
     */
    public OrganizationsPayload(List<Organization> organizations) {
        this.organizations = organizations == null
                ? Collections.emptyList()
                : new ArrayList<>(organizations);
    }

    /**
     * @return неизменяемое представление списка
     */
    public List<Organization> getOrganizations() {
        return Collections.unmodifiableList(organizations);
    }
}

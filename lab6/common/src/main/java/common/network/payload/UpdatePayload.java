package common.network.payload;

import common.model.Organization;

/**
 * Payload для команды {@code UPDATE}.
 *
 * <p>Содержит {@code id} обновляемого элемента и новый {@link Organization}
 * (поля кроме id/creationDate). Сервер сохраняет старую дату создания и
 * подставляет переданный id.
 */
public final class UpdatePayload implements RequestPayload {

    private static final long serialVersionUID = 1L;

    private final long id;
    private final Organization organization;

    /**
     * @param id           идентификатор обновляемого элемента
     * @param organization новые поля (без id/creationDate)
     */
    public UpdatePayload(long id, Organization organization) {
        this.id = id;
        this.organization = organization;
    }

    /** @return id обновляемого элемента */
    public long getId() { return id; }

    /** @return новая организация */
    public Organization getOrganization() { return organization; }
}

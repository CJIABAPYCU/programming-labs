package common.network.payload;

import common.model.Organization;

/**
 * Тело сообщения с одной {@link Organization}.
 */
public final class OrganizationPayload implements RequestPayload, ResponsePayload {

    private static final long serialVersionUID = 1L;

    private final Organization organization;

    /**
     * @param organization organization
     */
    public OrganizationPayload(Organization organization) {
        this.organization = organization;
    }

    /** @return организация */
    public Organization getOrganization() {
        return organization;
    }
}

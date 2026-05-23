package common.network.payload;

import common.model.Organization;

/**
 * Payload containing a map key and an organization.
 */
public final class KeyOrganizationPayload implements RequestPayload {

    private static final long serialVersionUID = 1L;

    private final int key;
    private final Organization organization;

    /**
     * @param key LinkedHashMap key
     * @param organization user-entered organization
     */
    public KeyOrganizationPayload(int key, Organization organization) {
        this.key = key;
        this.organization = organization;
    }

    public int getKey() { return key; }
    public Organization getOrganization() { return organization; }
}

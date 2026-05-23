package common.network.payload;

import common.model.OrganizationType;

/**
 * Payload containing an organization type.
 */
public final class TypePayload implements RequestPayload {

    private static final long serialVersionUID = 1L;

    private final OrganizationType type;

    /**
     * @param type non-null type argument
     */
    public TypePayload(OrganizationType type) {
        this.type = type;
    }

    public OrganizationType getType() {
        return type;
    }
}

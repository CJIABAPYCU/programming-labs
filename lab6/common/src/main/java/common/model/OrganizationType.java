package common.model;

import java.io.Serializable;

/**
 * Organization type.
 */
public enum OrganizationType implements Serializable {
    /** Commercial organization. */
    COMMERCIAL,
    /** Public organization. */
    PUBLIC,
    /** Government organization. */
    GOVERNMENT,
    /** Trust. */
    TRUST,
    /** Private limited company. */
    PRIVATE_LIMITED_COMPANY
}

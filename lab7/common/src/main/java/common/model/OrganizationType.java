package common.model;

import java.io.Serializable;

/**
 * Тип организации.
 */
public enum OrganizationType implements Serializable {
    /** Коммерческая организация. */
    COMMERCIAL,
    /** Публичная организация. */
    PUBLIC,
    /** Государственная организация. */
    GOVERNMENT,
    /** Траст. */
    TRUST,
    /** Частная компания с ограниченной ответственностью. */
    PRIVATE_LIMITED_COMPANY
}

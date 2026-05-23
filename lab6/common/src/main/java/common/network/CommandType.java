package common.network;

import java.io.Serializable;

/**
 * Commands that can be sent from the client to the server.
 *
 * <p>Local client commands such as {@code execute_script} and {@code exit},
 * and the server-only {@code save} command, are intentionally absent.
 */
public enum CommandType implements Serializable {
    /** Collection metadata. */
    INFO,
    /** All organizations sorted by name. */
    SHOW,
    /** Insert a new organization by map key. */
    INSERT,
    /** Update an organization by generated id. */
    UPDATE,
    /** Remove an organization by map key. */
    REMOVE_KEY,
    /** Clear the collection. */
    CLEAR,
    /** Remove all organizations greater than a given organization. */
    REMOVE_GREATER,
    /** Replace value by key if the new value is lower than the old one. */
    REPLACE_IF_LOWE,
    /** Remove all entries whose key is greater than the given key. */
    REMOVE_GREATER_KEY,
    /** Count organizations whose type is less than the given type. */
    COUNT_LESS_THAN_TYPE,
    /** Filter organizations by substring in name. */
    FILTER_CONTAINS_NAME,
    /** Print organizations in ascending natural order. */
    PRINT_ASCENDING
}

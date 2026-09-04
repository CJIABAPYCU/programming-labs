package common.network;

import java.io.Serializable;

public enum CommandType implements Serializable {
    INFO,
    SHOW,
    INSERT,
    UPDATE,
    REMOVE_KEY,
    CLEAR,
    REMOVE_GREATER,
    REPLACE_IF_LOWE,
    REMOVE_GREATER_KEY,
    COUNT_LESS_THAN_TYPE,
    FILTER_CONTAINS_NAME,
    PRINT_ASCENDING,
    REGISTER,
    LOGIN
}

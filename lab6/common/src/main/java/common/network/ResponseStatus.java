package common.network;

import java.io.Serializable;

/**
 * Статус ответа сервера.
 */
public enum ResponseStatus implements Serializable {
    /** Команда успешно выполнена. */
    OK,
    /** Ошибка выполнения / валидации / IO. */
    ERROR,
    /** Команда не разрешена клиенту (например, {@code save}). */
    FORBIDDEN
}

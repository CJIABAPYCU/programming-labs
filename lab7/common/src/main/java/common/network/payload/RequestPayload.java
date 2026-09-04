package common.network.payload;

import java.io.Serializable;

/**
 * Маркерный интерфейс для аргументов команды клиента.
 *
 * <p>Каждая команда из {@code common.network.CommandType} соответствует
 * конкретной реализации этого интерфейса. Если параметров нет, используется
 * {@link EmptyPayload}.
 */
public interface RequestPayload extends Serializable {
}

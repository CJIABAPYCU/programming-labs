package common.network.payload;

import java.io.Serializable;

/**
 * Маркерный интерфейс для тела ответа сервера.
 *
 * <p>Если ответ не содержит дополнительных данных, используется
 * {@link EmptyPayload}.
 */
public interface ResponsePayload extends Serializable {
}

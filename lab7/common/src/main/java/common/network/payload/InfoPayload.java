package common.network.payload;

import java.time.LocalDateTime;

/**
 * Payload ответа на команду {@code INFO}.
 *
 * <p>Содержит мета-информацию о коллекции на сервере:
 * <ul>
 *   <li>тип коллекции (например, {@code "LinkedHashMap"});</li>
 *   <li>время инициализации;</li>
 *   <li>текущий размер;</li>
 *   <li>время последнего сохранения (может быть {@code null}).</li>
 * </ul>
 */
public final class InfoPayload implements ResponsePayload {

    private static final long serialVersionUID = 1L;

    private final String collectionType;
    private final LocalDateTime initTime;
    private final int size;
    private final LocalDateTime lastSaveTime;

    /**
     * @param collectionType имя типа коллекции
     * @param initTime       время инициализации
     * @param size           текущий размер
     * @param lastSaveTime   время последнего сохранения, либо {@code null}
     */
    public InfoPayload(String collectionType,
                       LocalDateTime initTime,
                       int size,
                       LocalDateTime lastSaveTime) {
        this.collectionType = collectionType;
        this.initTime = initTime;
        this.size = size;
        this.lastSaveTime = lastSaveTime;
    }

    public String getCollectionType() { return collectionType; }
    public LocalDateTime getInitTime() { return initTime; }
    public int getSize() { return size; }
    public LocalDateTime getLastSaveTime() { return lastSaveTime; }
}

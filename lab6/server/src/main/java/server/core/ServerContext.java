package server.core;

import server.command.CommandRegistry;
import server.storage.CsvStorage;

import common.network.Response;

import java.net.SocketAddress;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * DI-контейнер серверной логики (инъекция зависимостей в команды).
 *
 * <p>Создаётся один раз в {@link server.ServerMain} и передаётся:
 * <ul>
 *   <li>в {@link CommandRegistry} (для построения хэндлеров);</li>
 *   <li>в {@link server.network.UdpServer} (для основного цикла);</li>
 *   <li>в {@link server.console.ServerConsole} (для команд save/exit).</li>
 * </ul>
 *
 * <p><b>Не хранить здесь сетевых ресурсов</b> — они принадлежат {@code UdpServer}
 * и закрываются в его {@code close()}.
 */
public final class ServerContext {

    private static final Logger LOG = Logger.getLogger(ServerContext.class.getName());

    private final OrganizationRepository repository;
    private final IdProvider idProvider;
    private final CsvStorage storage;
    private final String fileName;
    private final CommandRegistry registry;
    private final Map<ResponseCacheKey, Response> responseCache = new LinkedHashMap<>(128, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<ResponseCacheKey, Response> eldest) {
            return size() > 256;
        }
    };

    private volatile boolean shuttingDown;

    /**
     * @param repository репозиторий
     * @param idProvider генератор id
     * @param storage    CSV-хранилище
     * @param fileName   путь к файлу коллекции
     * @param registry   реестр серверных команд
     */
    public ServerContext(OrganizationRepository repository,
                         IdProvider idProvider,
                         CsvStorage storage,
                         String fileName,
                         CommandRegistry registry) {
        this.repository = repository;
        this.idProvider = idProvider;
        this.storage = storage;
        this.fileName = fileName;
        this.registry = registry;
    }

    public OrganizationRepository repository() { return repository; }
    public IdProvider idProvider()             { return idProvider; }
    public CsvStorage storage()                { return storage; }
    public String fileName()                   { return fileName; }
    public CommandRegistry registry()          { return registry; }

    public Response cachedResponse(SocketAddress clientAddress, long requestId) {
        return responseCache.get(new ResponseCacheKey(clientAddress, requestId));
    }

    public void cacheResponse(SocketAddress clientAddress, Response response) {
        if (response != null) {
            responseCache.put(new ResponseCacheKey(clientAddress, response.getRequestId()), response);
        }
    }

    /** @return {@code true}, если получен сигнал на завершение */
    public boolean isShuttingDown() {
        return shuttingDown;
    }

    /** Запросить корректное завершение цикла {@link server.network.UdpServer}. */
    public void requestShutdown() {
        this.shuttingDown = true;
    }

    /**
     * Сохранить коллекцию и пометить время. Вызывается:
     * <ul>
     *   <li>командой {@code save} в консоли сервера;</li>
     *   <li>при завершении (shutdown hook).</li>
     * </ul>
     *
     * @return {@code true} при успешной записи
     */
    public boolean saveCollection() {
        boolean ok = storage.save(fileName, repository.entries());
        if (ok) {
            repository.markSavedNow();
            LOG.info("Коллекция сохранена.");
        }
        return ok;
    }

    /**
     * Saves the collection without logging.
     *
     * <p>Used only from JVM shutdown hook because {@code java.util.logging}
     * handlers can be closed concurrently during JVM shutdown.
     *
     * @return {@code true} on successful write
     */
    public boolean saveCollectionSilently() {
        boolean ok = storage.saveSilently(fileName, repository.entries());
        if (ok) {
            repository.markSavedNow();
        }
        return ok;
    }

    /**
     * Полное завершение: сохранить и выставить флаг shuttingDown.
     */
    public void shutdown() {
        LOG.info("Shutdown requested");
        saveCollection();
        requestShutdown();
    }

    private record ResponseCacheKey(SocketAddress clientAddress, long requestId) {
    }
}

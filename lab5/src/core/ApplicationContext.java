package core;

import commands.CommandDispatcher;
import console.TextIO;
import console.OrganizationReader;
import io.OrganizationCsvStorage;
import runner.ScriptExecutor;

/**
 * Контекст приложения с общими зависимостями.
 */
public class ApplicationContext {
    private final TextIO console;
    private final OrganizationRepository repository;
    private final OrganizationCsvStorage storage;
    private final IdProvider idProvider;
    private final OrganizationReader reader;
    private final String fileName;
    private CommandDispatcher dispatcher;
    private ScriptExecutor scriptExecutor;

    /**
     * Создает контекст приложения.
     *
     * @param console консоль
     * @param repository репозиторий коллекции
     * @param storage помощник хранения CSV
     * @param idProvider поставщик id
     * @param reader считыватель организации
     * @param fileName имя файла
     */
    public ApplicationContext(TextIO console,
                      OrganizationRepository repository,
                      OrganizationCsvStorage storage,
                      IdProvider idProvider,
                      OrganizationReader reader,
                      String fileName) {
        this.console = console;
        this.repository = repository;
        this.storage = storage;
        this.idProvider = idProvider;
        this.reader = reader;
        this.fileName = fileName;
    }

    /**
     * Возвращает консоль приложения.
     *
     * @return консоль
     */
    public TextIO console() {
        return console;
    }

    /**
     * Возвращает репозиторий коллекции.
     *
     * @return менеджер коллекции
     */
    public OrganizationRepository repository() {
        return repository;
    }

    /**
     * Возвращает объект для работы с CSV.
     *
     * @return помощник CSV IO
     */
    public OrganizationCsvStorage storage() {
        return storage;
    }

    /**
     * Возвращает генератор идентификаторов.
     *
     * @return генератор id
     */
    public IdProvider idProvider() {
        return idProvider;
    }

    /**
     * Возвращает считыватель организаций.
     *
     * @return считыватель организации
     */
    public OrganizationReader reader() {
        return reader;
    }

    /**
     * Возвращает имя файла коллекции.
     *
     * @return имя файла
     */
    public String fileName() {
        return fileName;
    }

    /**
     * Возвращает диспетчер команд.
     *
     * @return диспетчер команд
     */
    public CommandDispatcher dispatcher() {
        return dispatcher;
    }

    /**
     * Устанавливает диспетчер команд.
     *
     * @param dispatcher диспетчер команд
     */
    public void setDispatcher(CommandDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    /**
     * Возвращает исполнитель скриптов.
     *
     * @return исполнитель скриптов
     */
    public ScriptExecutor scriptExecutor() {
        return scriptExecutor;
    }

    /**
     * Устанавливает исполнитель скриптов.
     *
     * @param scriptExecutor исполнитель скриптов
     */
    public void setScriptExecutor(ScriptExecutor scriptExecutor) {
        this.scriptExecutor = scriptExecutor;
    }
}

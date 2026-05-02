package io;

/**
 * Вспомогательный класс для имени файла из окружения.
 */
public final class EnvConfig {
    /**
     * Имя переменной окружения по умолчанию для пути к CSV файлу.
     */
    public static final String FILE_ENV = "LAB5_FILE";

    private EnvConfig() {
    }

    /**
     * Считывает имя файла из переменной окружения по умолчанию.
     *
     * @return путь к файлу или null
     */
    public static String getFileName() {
        return System.getenv(FILE_ENV);
    }

    /**
     * Считывает имя файла из указанной переменной окружения.
     *
     * @param key имя переменной окружения
     * @return путь к файлу или null
     */
    public static String getFileName(String key) {
        return System.getenv(key);
    }
}

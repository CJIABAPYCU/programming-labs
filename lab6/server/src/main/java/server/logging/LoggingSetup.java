package server.logging;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Конфигурация {@link java.util.logging} для серверного приложения.
 *
 * <p><b>Доп. задание ТЗ.</b> Все этапы работы сервера логируются:
 * <ul>
 *   <li>старт и привязка к порту;</li>
 *   <li>получение запроса (тип, адрес отправителя);</li>
 *   <li>результат обработки (OK/ERROR + длительность);</li>
 *   <li>отправка ответа;</li>
 *   <li>ошибки сериализации, сети, IO;</li>
 *   <li>сохранение коллекции (по команде / по shutdown);</li>
 *   <li>graceful shutdown.</li>
 * </ul>
 *
 * <p>Файл лога — {@code server.log} в текущем каталоге.
 */
public final class LoggingSetup {

    private LoggingSetup() {
        // utility-класс
    }

    /**
     * Настраивает корневой логгер: уровень, форматтер, ConsoleHandler, FileHandler.
     */
    public static void init() {
        Logger root = Logger.getLogger("");
        for (Handler handler : root.getHandlers()) {
            root.removeHandler(handler);
            handler.close();
        }
        root.setLevel(Level.INFO);

        Formatter formatter = new Formatter() {
            @Override
            public String format(LogRecord record) {
                String message = String.format("%1$tF %1$tT [%2$-7s] %3$s - %4$s%n",
                        record.getMillis(), record.getLevel(),
                        record.getLoggerName(), formatMessage(record));
                if (record.getThrown() == null) {
                    return message;
                }
                StringWriter stackTrace = new StringWriter();
                record.getThrown().printStackTrace(new PrintWriter(stackTrace));
                return message + stackTrace;
            }
        };

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(formatter);
        consoleHandler.setLevel(Level.INFO);
        root.addHandler(consoleHandler);

        try {
            FileHandler fileHandler = new FileHandler("server.log", true);
            fileHandler.setFormatter(formatter);
            fileHandler.setLevel(Level.INFO);
            root.addHandler(fileHandler);
        } catch (IOException e) {
            root.warning(() -> "FileHandler init failed: " + e.getMessage());
        }
    }
}

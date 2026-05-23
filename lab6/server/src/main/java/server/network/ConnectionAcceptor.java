package server.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.logging.Logger;

/**
 * Модуль приёма «подключений».
 *
 * <p>В UDP нет стейтфул-соединений, но ТЗ требует наличие отдельного
 * модуля. Здесь — открытие канала, перевод в неблокирующий режим, привязка
 * к порту и регистрация на селекторе. Используется из {@link UdpServer#run()}.
 */
public final class ConnectionAcceptor {

    private static final Logger LOG = Logger.getLogger(ConnectionAcceptor.class.getName());

    /**
     * Создаёт «приёмник».
     */
    public ConnectionAcceptor() {
    }

    /**
     * Открывает канал, настраивает его и регистрирует на селекторе.
     *
     * @param port     порт привязки
     * @param selector существующий селектор
     * @return открытый канал
     * @throws IOException при ошибке настройки
     */
    public DatagramChannel openAndRegister(int port, Selector selector) throws IOException {
        LOG.info(() -> "Opening UDP DatagramChannel on port " + port);
        DatagramChannel channel = DatagramChannel.open();
        channel.configureBlocking(false);
        LOG.info("DatagramChannel switched to non-blocking mode");
        channel.bind(new InetSocketAddress(port));
        LOG.info(() -> "DatagramChannel bound to UDP port " + port);
        channel.register(selector, SelectionKey.OP_READ);
        LOG.info("DatagramChannel registered in selector for OP_READ");
        return channel;
    }
}

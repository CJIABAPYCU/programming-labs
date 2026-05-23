package client.network;

import java.net.InetAddress;

/**
 * Точка подключения к серверу: адрес + порт.
 */
public final class ServerEndpoint {

    private final InetAddress address;
    private final int port;

    /**
     * @param address IP/hostname сервера
     * @param port    UDP-порт сервера
     */
    public ServerEndpoint(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    /** @return адрес */
    public InetAddress address() { return address; }

    /** @return порт */
    public int port() { return port; }
}

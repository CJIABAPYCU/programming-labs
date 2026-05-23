/**
 * Серверный модуль лабораторной №6.
 *
 * <p>Подмодули:
 * <ul>
 *   <li>{@link server.network} — приём/чтение/отправка по UDP через
 *       {@link java.nio.channels.DatagramChannel} в неблокирующем режиме;</li>
 *   <li>{@link server.command} — реестр и хэндлеры команд (паттерн Command);</li>
 *   <li>{@link server.command.handlers} — конкретные обработчики;</li>
 *   <li>{@link server.core} — репозиторий, генератор id, контекст;</li>
 *   <li>{@link server.storage} — CSV через {@link java.util.Scanner} и
 *       {@link java.io.OutputStreamWriter};</li>
 *   <li>{@link server.console} — консоль оператора (save/exit);</li>
 *   <li>{@link server.logging} — конфигурация {@code java.util.logging}.</li>
 * </ul>
 *
 * <p><b>Сервер однопоточный.</b>
 */
package server;

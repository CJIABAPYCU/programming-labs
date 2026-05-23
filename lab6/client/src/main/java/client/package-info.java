/**
 * Клиентский модуль лабораторной №6.
 *
 * <p>Подмодули:
 * <ul>
 *   <li>{@link client.network} — UDP через {@link java.net.DatagramSocket}
 *       и {@link java.net.DatagramPacket} (требование ТЗ: на клиенте — датаграммы);</li>
 *   <li>{@link client.console} — интерактивный ввод-вывод и читатель Organization;</li>
 *   <li>{@link client.command} — клиентские команды (паттерн Command), реестр,
 *       парсеры аргументов;</li>
 *   <li>{@link client.command.parsers} — конкретные команды, включая локальные
 *       {@code exit} и {@code execute_script};</li>
 *   <li>{@link client.runner} — главный цикл и исполнитель скриптов.</li>
 * </ul>
 *
 * <p><b>Команды {@code save} нет</b> — по требованию ТЗ.
 */
package client;

/**
 * Общие классы клиентского и серверного модулей лабораторной №6.
 *
 * <p>Содержит:
 * <ul>
 *   <li>{@link common.model} — иммутабельные сериализуемые сущности домена;</li>
 *   <li>{@link common.network} — модель сетевого протокола (Request, Response,
 *       enum-ы, константы);</li>
 *   <li>{@link common.network.payload} — типизированные payload-объекты;</li>
 *   <li>{@link common.network.util} — утилита сериализации в byte[].</li>
 * </ul>
 *
 * <p>Этот модуль <b>не</b> зависит от {@code client} и {@code server}.
 */
package common;

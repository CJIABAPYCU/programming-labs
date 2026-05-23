package common.model;

/**
 * Контракт самовалидации сущности.
 *
 * <p>Реализуют все классы домена ({@link Organization}, {@link Coordinates},
 * {@link Address}, {@link Location}). Метод должен возвращать {@code true},
 * только если все поля удовлетворяют требованиям, указанным в их javadoc.
 *
 * <p><b>Контракт реализации.</b> Никаких исключений; ничего не печатать,
 * никаких сетевых/IO-операций. Только проверка состояния.
 */
public interface Validatable {
    /**
     * Проверяет инвариант объекта.
     *
     * @return {@code true}, если объект валиден
     */
    boolean validate();
}

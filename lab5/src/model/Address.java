package model;

import java.util.Objects;

/**
 * Объект адреса.
 */
public class Address implements Validatable {
    private final String street;
    private final Location town;

    /**
     * Создает объект адреса.
     *
     * @param street улица (может быть null)
     * @param town населенный пункт (может быть null)
     */
    public Address(String street, Location town) {
        this.street = street;
        this.town = town;
    }

    /**
     * Возвращает улицу адреса.
     *
     * @return улица
     */
    public String getStreet() {
        return street;
    }

    /**
     * Возвращает населенный пункт адреса.
     *
     * @return населенный пункт
     */
    public Location getTown() {
        return town;
    }

    @Override
    public boolean validate() {
        return town == null || town.validate();
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", town=" + town +
                '}';
    }

    /**
     * Возвращает стабильную строку для сортировки.
     *
     * @return строковое представление для сортировки
     */
    public String toSortableString() {
        String streetPart = street == null ? "" : street;
        String townPart = town == null ? "" : town.toString();
        return streetPart + "|" + townPart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Address address = (Address) o;
        return Objects.equals(street, address.street) && Objects.equals(town, address.town);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, town);
    }
}

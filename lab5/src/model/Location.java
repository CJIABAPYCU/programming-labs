package model;

import java.util.Objects;

/**
 * Объект локации.
 */
public class Location implements Validatable {
    private final Integer x;
    private final Double y;
    private final String name;

    /**
     * Создает объект локации.
     *
     * @param x значение x (не null)
     * @param y значение y (не null)
     * @param name поле name (может быть null)
     */
    public Location(Integer x, Double y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    /**
     * Возвращает координату x населенного пункта.
     *
     * @return значение x
     */
    public Integer getX() {
        return x;
    }

    /**
     * Возвращает координату y населенного пункта.
     *
     * @return значение y
     */
    public Double getY() {
        return y;
    }

    /**
     * Возвращает название населенного пункта.
     *
     * @return поле name
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean validate() {
        return x != null && y != null;
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Location location = (Location) o;
        return Objects.equals(x, location.x)
                && Objects.equals(y, location.y)
                && Objects.equals(name, location.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, name);
    }
}

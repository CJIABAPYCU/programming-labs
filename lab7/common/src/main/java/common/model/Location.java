package common.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Локация внутри адреса.
 */
public class Location implements Serializable, Validatable {

    private static final long serialVersionUID = 1L;

    private final double x;
    private final double y;
    private final String name;

    /**
     * @param x координата x
     * @param y координата y
     * @param name непустое название локации
     */
    public Location(double x, double y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public String getName() { return name; }

    @Override
    public boolean validate() {
        return name != null && !name.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "Location{x=" + x + ", y=" + y + ", name='" + name + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location location)) {
            return false;
        }
        return Double.compare(location.x, x) == 0
                && Double.compare(location.y, y) == 0
                && Objects.equals(name, location.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, name);
    }
}

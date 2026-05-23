package common.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Location inside an address.
 */
public class Location implements Serializable, Validatable {

    private static final long serialVersionUID = 1L;

    private final double x;
    private final double y;
    private final String name;

    /**
     * @param x x coordinate
     * @param y y coordinate
     * @param name non-empty location name
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

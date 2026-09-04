package common.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Координаты организации.
 */
public class Coordinates implements Serializable, Validatable {

    private static final long serialVersionUID = 1L;

    private final float x;
    private final long y;

    /**
     * @param x координата x
     * @param y координата y, должна быть больше -323
     */
    public Coordinates(float x, long y) {
        this.x = x;
        this.y = y;
    }

    public float getX() { return x; }
    public long getY() { return y; }

    @Override
    public boolean validate() {
        return y > -323;
    }

    @Override
    public String toString() {
        return "Coordinates{x=" + x + ", y=" + y + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Coordinates that)) {
            return false;
        }
        return Float.compare(that.x, x) == 0 && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

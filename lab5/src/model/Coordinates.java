package model;

import java.util.Objects;

/**
 * Координаты с правилами валидации.
 */
public class Coordinates implements Comparable<Coordinates>, Validatable {
    private final double x;
    private final float y;

    /**
     * Создает координаты.
     *
     * @param x значение x
     * @param y значение y (должно быть больше -913)
     */
    public Coordinates(double x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Возвращает координату x.
     *
     * @return значение x
     */
    public double getX() {
        return x;
    }

    /**
     * Возвращает координату y.
     *
     * @return значение y
     */
    public float getY() {
        return y;
    }

    @Override
    public boolean validate() {
        return y > -913;
    }

    @Override
    public int compareTo(Coordinates other) {
        int cmp = Double.compare(this.x, other.x);
        if (cmp != 0) {
            return cmp;
        }
        return Float.compare(this.y, other.y);
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
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
        Coordinates that = (Coordinates) o;
        return Double.compare(that.x, x) == 0 && Float.compare(that.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

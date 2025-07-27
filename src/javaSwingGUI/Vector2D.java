package javaSwingGUI;

public class Vector2D {
    public double x;
    public double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void add(Vector2D other) {
        this.x += other.x;
        this.y += other.y;
    }

    public void multiply(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public void normalize() {
        double mag = magnitude();
        if (mag != 0) {
            this.x /= mag;
            this.y /= mag;
        }
    }

    public Vector2D copy() {
        return new Vector2D(this.x, this.y);
    }

    public void set(Vector2D other) {
        this.x = other.x;
        this.y = other.y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean isZero() {
        return x == 0 && y == 0;
    }
    public static Vector2D fromAngle(double angle) {
        return new Vector2D(Math.cos(angle), Math.sin(angle));
    }

}

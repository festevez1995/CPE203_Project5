
final class Point {
    protected int x;
    protected int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public boolean equals(Object other) {
        return other instanceof Point && ((Point) other).x == this.x && ((Point) other).y == this.y;
    }

    public void setPoints(int x, int y) {
        this.x += x;
        this.y += y;
    }
    public int hashCode() {
        int result = 17;
        result = result * 31 + x;
        result = result * 31 + y;
        return result;
    }

    public static int distanceSquared(Point end, Point p) {
        return Math.abs(end.x - p.x) + Math.abs(end.y - p.y);
    }

    public boolean adjacent(Point p) {
        return (x == p.x && Math.abs(y - p.y) == 1) ||
                (y == p.y && Math.abs(x - p.x) == 1);
    }

}

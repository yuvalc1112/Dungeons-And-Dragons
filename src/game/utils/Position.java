package game.utils;


public class Position {
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public double distance(Position other) {
        int dx = this.x - other.x;
        int dy = this.y - other.y;
        return (int)Math.sqrt(Math.pow((dx), 2) + Math.pow((dy), 2));

    }
    public int compareTo(Position p1) {
        if (this.x == p1.x & this.y == p1.y) {
            return 0;
        } else if (this.y < p1.y) {
            return -1;
        } else if (this.y > p1.y) {
            return 1;
        } else {
            return this.x < p1.x ? -1 : 1;
        }
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position)) return false;
        Position p = (Position) obj;
        return this.x == p.x && this.y == p.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }
}
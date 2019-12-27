package pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour;

public class Point {
    public int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point subtract(Point other) {
        return new Point(this.x - other.x, this.y - other.y);
    }

    public int length() {
        return (int) Math.round(Math.sqrt(this.x * this.x - this.y * this.y));
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", this.x, this.y);
    }
}

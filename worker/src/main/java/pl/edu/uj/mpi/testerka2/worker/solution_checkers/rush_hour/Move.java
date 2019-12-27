package pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour;

public class Move {
    public Car car;
    public char dir;
    public int d;

    public Move(Car car, char direction, int distance) {
        this.car = car;
        this.dir = direction;
        this.d = distance;
    }

    @Override
    public String toString() {
        return String.format("%c %c %d", this.car.id, this.dir, this.d);
    }
}

package pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour;

import pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.exceptions.InvalidCarDirectionException;
import pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.exceptions.InvalidCarFormatException;
import pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.exceptions.InvalidCarIdException;
import pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.exceptions.InvalidCarLengthException;
import pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.exceptions.InvalidCarPositionException;
import pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.exceptions.InvalidNumberOfDescriptorsException;

public class Car {
    public Point pos;
    public int len;
    public char dir, id;

    public Car(char id, int x, int y, char direction, int length) {
        this.id = id;
        this.pos = new Point(x, y);
        this.len = length;
        this.dir = direction;
    }

    public boolean checkCollision(Point p) {
        return (dir == 'V' && p.x == pos.x && p.y >= pos.y && p.y <= pos.y + len - 1)
                || (dir == 'H' && p.y == pos.y && p.x >= pos.x && p.x <= pos.x + len - 1);
    }

    @Override
    public Car clone() {
        return new Car(this.id, this.pos.x, this.pos.y, this.dir, this.len);
    }

    public Car move(Move move) {
        Car newCar = this.clone();

        if (move.dir == 'D') newCar.pos.y -= move.d;

        if (move.dir == 'U') newCar.pos.y += move.d;

        if (move.dir == 'L') newCar.pos.x -= move.d;

        if (move.dir == 'R') newCar.pos.x += move.d;

        return newCar;
    }

    public static Car parseString(String input) throws InvalidCarFormatException {
        String[] pieces = input.split(" ");

        if (pieces.length != 5)
            throw new InvalidNumberOfDescriptorsException(input, pieces.length);

        if (pieces[0].length() != 1)
            throw new InvalidCarIdException(input, pieces[0]);

        char carId = pieces[0].charAt(0);

        int x = Integer.parseInt(pieces[1]), y = Integer.parseInt(pieces[2]);

        if (x < 0 || x > 5 || y < 0 || y > 5)
            throw new InvalidCarPositionException(input, x, y);

        char direction = pieces[3].charAt(0);

        if (direction != 'V' && direction != 'H')
            throw new InvalidCarDirectionException(input, direction);

        int length = Integer.parseInt(pieces[4]);

        if (length < 1 || length > 6)
            throw new InvalidCarLengthException(input, length);

        return new Car(carId, x, y, direction, length);
    }

    @Override
    public String toString() {
        return String.format("%c [%c, %d] %s", this.id, this.dir, this.len, this.pos.toString());
    }
}

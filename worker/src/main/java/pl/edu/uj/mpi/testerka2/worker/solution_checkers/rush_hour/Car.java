package pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour;

import pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.exceptions.InvalidCarDirectionException;
import pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.exceptions.InvalidCarFormatException;
import pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.exceptions.InvalidCarIdException;
import pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.exceptions.InvalidCarLengthException;
import pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.exceptions.InvalidCarPositionException;
import pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.exceptions.InvalidNumberOfDescriptorsException;
import java.util.Optional;

public record Car(char id, Point pos, Direction dir, int len) {
    public enum Direction {
        Vertical,
        Horizontal;

        public static Optional<Direction> fromChar(char ch) {
            return switch (ch) {
                case 'V' -> Optional.of(Vertical);
                case 'H' -> Optional.of(Horizontal);
                default -> Optional.empty();
            };
        }
    }

    public boolean checkCollision(Point p) {
        return (dir == Direction.Vertical && p.x() == pos.x() && p.y() >= pos.y() && p.y() <= pos.y() + len - 1)
                || (dir == Direction.Horizontal && p.y() == pos.y() && p.x() >= pos.x() && p.x() <= pos.x() + len - 1);
    }

    public Car move(Move move) {
        Point newPos = switch (move.dir()) {
            case Down -> new Point(pos.x(), pos.y() - move.d());
            case Up -> new Point(pos.x(), pos.y() + move.d());
            case Left -> new Point(pos.x() - move.d(), pos.y());
            case Right -> new Point(pos.x() + move.d(), pos.y());
        };

        return new Car(this.id, newPos, this.dir, this.len);
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

        char directionChar = pieces[3].charAt(0);

        Direction direction = Direction.fromChar(directionChar)
            .orElseThrow(() -> new InvalidCarDirectionException(input, directionChar));

        int length = Integer.parseInt(pieces[4]);

        if (length < 1 || length > 6)
            throw new InvalidCarLengthException(input, length);

        return new Car(carId, new Point(x, y), direction, length);
    }

    @Override
    public String toString() {
        return String.format("%c [%c, %d] %s", this.id, this.dir, this.len, this.pos.toString());
    }
}

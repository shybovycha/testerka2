package pl.edu.uj.mpi.testerka2.solution_checkers.rush_hour;

import java.util.Optional;

public record Move(Car car, Direction dir, int d) {
    public enum Direction {
        Left,
        Right,
        Up,
        Down;

        public char toChar() {
            return switch (this) {
                case Left -> 'L';
                case Right -> 'R';
                case Up -> 'U';
                case Down -> 'D';
            };
        }

        public static Optional<Direction> fromChar(char c) {
            return switch (c) {
                case 'L' -> Optional.of(Direction.Left);
                case 'R' -> Optional.of(Direction.Right);
                case 'U' -> Optional.of(Direction.Up);
                case 'D' -> Optional.of(Direction.Down);
                default -> Optional.empty();
            };
        }
    }

    @Override
    public String toString() {
        return String.format("%c %c %d", this.car.id(), this.dir.toChar(), this.d);
    }
}

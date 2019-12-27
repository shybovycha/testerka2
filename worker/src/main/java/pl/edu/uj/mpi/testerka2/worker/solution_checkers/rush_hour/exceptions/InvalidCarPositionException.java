package pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.exceptions;

public class InvalidCarPositionException extends InvalidCarFormatException {
    public InvalidCarPositionException(String carStr, int x, int y) {
        super(carStr, String.format("Car position (%d, %d) is invalid", x, y));
    }
}

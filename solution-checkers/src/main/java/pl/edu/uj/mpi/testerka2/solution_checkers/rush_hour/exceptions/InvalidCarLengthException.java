package pl.edu.uj.mpi.testerka2.solution_checkers.rush_hour.exceptions;

public class InvalidCarLengthException extends InvalidCarFormatException {
    public InvalidCarLengthException(String carStr, int length) {
        super(carStr, String.format("Car length (%d) is invalid", length));
    }
}

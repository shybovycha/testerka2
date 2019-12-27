package pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.exceptions;

public class InvalidCarIdMoveException extends InvalidMoveFormatException {
    public InvalidCarIdMoveException(String moveStr, char id) {
        super(moveStr, String.format("Car ID (%s) is invalid", id));
    }

    public InvalidCarIdMoveException(String moveStr, String id) {
        super(moveStr, String.format("Car ID (%s) is invalid", id));
    }
}

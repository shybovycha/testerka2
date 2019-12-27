package pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.exceptions;

public class InvalidMoveDirectionException extends InvalidMoveFormatException {
    public InvalidMoveDirectionException(String moveStr, char direction) {
        super(moveStr, String.format("Move direction (%s) is invalid", direction));
    }
}

package pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.exceptions;

public class InvalidMoveFormatException extends Exception {
    public InvalidMoveFormatException(String moveStr, String message) {
        super(String.format("Invalid move format for string '%s'. %s", moveStr, message));
    }
}

package pl.edu.uj.mpi.testerka2.core.checker.exceptions;

/**
 * Created by shybovycha on 30/05/16.
 */
public class InvalidMoveFormatException extends Exception {
    public InvalidMoveFormatException(String moveStr, String message) {
        super(String.format("Invalid move format for string '%s'. %s", moveStr, message));
    }
}

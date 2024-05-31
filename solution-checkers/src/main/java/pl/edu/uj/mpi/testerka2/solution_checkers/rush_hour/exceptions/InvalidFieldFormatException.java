package pl.edu.uj.mpi.testerka2.solution_checkers.rush_hour.exceptions;

public class InvalidFieldFormatException extends Exception {
    public InvalidFieldFormatException(String fieldStr, String message) {
        super(String.format("Invalid field format for string '%s'. %s", fieldStr, message));
    }
}

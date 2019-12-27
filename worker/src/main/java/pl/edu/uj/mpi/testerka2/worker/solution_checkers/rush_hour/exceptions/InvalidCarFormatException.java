package pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.exceptions;

public class InvalidCarFormatException extends Exception {
    public InvalidCarFormatException(String carStr, String message) {
        super(String.format("Invalid car format for string '%s'. %s", carStr, message));
    }
}

package pl.edu.uj.mpi.testerka2.solution_checkers.rush_hour.exceptions;

public class InvalidNumberOfMoveDescriptorsException extends InvalidMoveFormatException {
    public InvalidNumberOfMoveDescriptorsException(String moveStr, int numberOfDescriptors) {
        super(moveStr, String.format("Expected 3 move descriptors, but got %d", numberOfDescriptors));
    }
}

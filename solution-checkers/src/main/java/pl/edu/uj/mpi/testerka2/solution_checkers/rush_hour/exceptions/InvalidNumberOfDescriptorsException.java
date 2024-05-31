package pl.edu.uj.mpi.testerka2.solution_checkers.rush_hour.exceptions;

public class InvalidNumberOfDescriptorsException extends InvalidCarFormatException {
    public InvalidNumberOfDescriptorsException(String carStr, int numberOfDescriptors) {
        super(carStr, String.format("Expected 5 car descriptors, but got %d", numberOfDescriptors));
    }
}

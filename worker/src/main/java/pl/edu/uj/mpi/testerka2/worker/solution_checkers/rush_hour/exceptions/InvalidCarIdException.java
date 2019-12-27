package pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.exceptions;

public class InvalidCarIdException extends InvalidCarFormatException {
    public InvalidCarIdException(String carStr, String carId) {
        super(carStr, String.format("Invalid car ID (%s)", carId));
    }
}

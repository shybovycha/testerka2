package pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.exceptions;

public class InvalidCarDirectionException extends InvalidCarFormatException {
    public InvalidCarDirectionException(String carStr, char direction) {
        super(carStr, String.format("Car direction (%s) is invalid", direction));
    }
}

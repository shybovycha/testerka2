package pl.edu.uj.mpi.testerka2.core.checker;

/**
 * Created by shybovycha on 10/05/16.
 */
public class InvalidFieldFormatException extends Exception {
    public InvalidFieldFormatException(String fieldStr, String message) {
        super(String.format("Invalid field format for string '%s'. %s", fieldStr, message));
    }
}

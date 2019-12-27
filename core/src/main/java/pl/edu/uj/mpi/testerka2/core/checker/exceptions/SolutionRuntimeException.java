package pl.edu.uj.mpi.testerka2.core.checker.exceptions;

/**
 * Created by shybovycha on 30/05/16.
 */
public class SolutionRuntimeException extends Exception {
    public SolutionRuntimeException(String output) {
        super(output);
    }
}

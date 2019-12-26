package pl.edu.uj.mpi.testerka2.core.checker;

/**
 * Created by shybovycha on 30/05/16.
 */
public class SolutionRuntimeException extends Exception {
    public SolutionRuntimeException(String output) {
        super(output);
    }
}

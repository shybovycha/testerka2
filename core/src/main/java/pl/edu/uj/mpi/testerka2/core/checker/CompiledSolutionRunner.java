package pl.edu.uj.mpi.testerka2.core.checker;

import pl.edu.uj.mpi.testerka2.core.entities.Solution;

import java.util.Scanner;

/**
 * Created by shybovycha on 12/05/16.
 */
public abstract class CompiledSolutionRunner extends SolutionRunner {
    @Override
    public String getOutputFor(Solution solution, String input) throws Exception {
        compile(solution);

        return runBinary(solution, input);
    }

    protected void compile(Solution solution) throws Exception {
        StringBuilder errors = new StringBuilder();

        Process process = this.getCompilerProcessBuilder(solution).start();

        try (Scanner stderrScanner = new Scanner(process.getErrorStream())) {
	        while (stderrScanner.hasNextLine()) {
	            errors.append(stderrScanner.nextLine()).append("\n");
	        }

	        if (errors.length() > 0) {
	            throw new SolutionCompilationException(errors.toString());
	        }
        }
    }

    protected abstract ProcessBuilder getCompilerProcessBuilder(Solution solution);
}

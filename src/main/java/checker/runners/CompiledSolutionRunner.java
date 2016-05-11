package checker.runners;

import checker.entities.Solution;

import java.util.Scanner;

/**
 * Created by shybovycha on 12/05/16.
 */
public abstract class CompiledSolutionRunner extends SolutionRunner {
    @Override
    public String getOutputFor(Solution solution, String input) throws Exception {
        this.compile(solution);

        return this.runBinary(solution, input);
    }

    protected void compile(Solution solution) throws Exception {
        StringBuilder errors = new StringBuilder();

        Process process = this.getCompilerProcessBuilder(solution).start();

        Scanner stderrScanner = new Scanner(process.getErrorStream());

        while (stderrScanner.hasNextLine()) {
            errors.append(stderrScanner.nextLine()).append("\n");
        }

        if (errors.length() > 0) {
            throw new Exception(String.format("COMPILATION ERRORS (solution #%d): %s", solution.getId(), errors.toString()));
        }
    }

    protected abstract ProcessBuilder getCompilerProcessBuilder(Solution solution);
}

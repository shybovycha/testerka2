package checker.runners;

import checker.entities.Solution;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by shybovycha on 12/05/16.
 */
public abstract class SolutionRunner {
    public boolean accepts(Solution solution) {
        return this.getAcceptedLanguage().equals(solution.getLanguage());
    }

    public String getOutputFor(Solution solution, String input) throws Exception {
        return this.runBinary(solution, input);
    }

    protected String runBinary(Solution solution, String input) throws Exception {
        StringBuilder output = new StringBuilder();
        StringBuilder errors = new StringBuilder();

        Process process = this.getRunProcessBuilder(solution).start();

        Scanner stdoutScanner = new Scanner(process.getInputStream());
        Scanner stderrScanner = new Scanner(process.getErrorStream());
        PrintWriter writer = new PrintWriter(process.getOutputStream());

        writer.write("1\n"); // FIXME: number of test cases

        writer.write(input);
        writer.close();

        while (stdoutScanner.hasNextLine()) {
            output.append(stdoutScanner.nextLine()).append("\n");
        }

        while (stderrScanner.hasNextLine()) {
            errors.append(stderrScanner.nextLine()).append("\n");
        }

        if (errors.length() > 0)
            throw new Exception(String.format("RUNTIME ERRORS (solution #%d): %s", solution.getId(), errors.toString()));

        return output.toString();
    }

    protected abstract ProcessBuilder getRunProcessBuilder(Solution solution);

    protected abstract String getAcceptedLanguage();
}

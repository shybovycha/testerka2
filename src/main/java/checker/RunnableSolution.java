package checker;

import checker.entities.Solution;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by shybovycha on 10/05/16.
 */
public abstract class RunnableSolution extends Solution {
    public RunnableSolution(String source) {
        super(source);
    }

    public RunnableSolution(Solution source) {
        super(source);
    }

    public String getOutputFor(String input) {
        return runBinary(input);
    }

    protected abstract ProcessBuilder getRunProcessBuilder();

    protected String runBinary(String input) {
        StringBuilder output = new StringBuilder();
        StringBuilder errors = new StringBuilder();

        Process process = null;

        try {
            process = this.getRunProcessBuilder().start();

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

            System.out.printf("ERRORS: %s", errors.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output.toString();
    }
}

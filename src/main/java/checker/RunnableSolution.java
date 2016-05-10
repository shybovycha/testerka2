package checker;

import checker.entities.Solution;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by shybovycha on 10/05/16.
 */
public abstract class RunnableSolution extends Solution {
    public RunnableSolution(String source) {
        super(source);
    }

    @Override
    protected String getOutputFor(String input) {
        return runBinary(input);
    }

    protected abstract ProcessBuilder getRunProcessBuilder();

    protected String runBinary(String input) {
        StringBuilder output = new StringBuilder();
        Process process = null;

        try {
            process = this.getRunProcessBuilder().start();
            Scanner scanner = new Scanner(process.getInputStream());

            while (scanner.hasNextLine())
                output.append(scanner.nextLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output.toString();
    }
}

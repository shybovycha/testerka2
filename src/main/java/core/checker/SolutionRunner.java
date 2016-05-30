package core.checker;

import core.entities.Solution;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by shybovycha on 12/05/16.
 */
public abstract class SolutionRunner {
    @Value("${runner.timeout}")
    private int runTimeout;

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

        RunnerWorker worker = new RunnerWorker(process);
        worker.start();

        try {
            worker.join(runTimeout);

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
                throw new SolutionRuntimeException(errors.toString());
        } catch (InterruptedException e) {
            worker.interrupt();
            Thread.currentThread().interrupt();
            throw e;
        } finally {
            process.destroy();
        }

        return output.toString();
    }

    protected abstract ProcessBuilder getRunProcessBuilder(Solution solution);

    protected abstract String getAcceptedLanguage();

    protected abstract String getDescription();

    protected String getSolutionDir(Solution solution) {
        return String.format("solutions/%d", solution.getId());
    }

    protected void writeSolutionToFile(Solution solution, String filename) {
        try {
            new File(this.getSolutionDir(solution)).mkdirs();

            File f = new File(String.format("%s/%s", this.getSolutionDir(solution), filename));
            PrintWriter writer = new PrintWriter(f);

            writer.write(solution.getSource());

            writer.close();
        } catch (Exception e) {
            // TODO: logger
            e.printStackTrace();
        }
    }
}

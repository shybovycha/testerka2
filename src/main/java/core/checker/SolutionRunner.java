package core.checker;

import core.entities.Solution;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * Created by shybovycha on 12/05/16.
 */
public abstract class SolutionRunner {
    @Value("${runner.timeout}")
    private int runTimeout;

    private static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

    private static <T> T timedCall(Callable<T> c, long timeout, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        FutureTask<T> task = new FutureTask<T>(c);
        THREAD_POOL.execute(task);
        return task.get(timeout, timeUnit);
    }

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

        try {
            writer.write("1\n"); // FIXME: number of test cases

            writer.write(input);
            writer.close();

            timedCall(process::waitFor, runTimeout, TimeUnit.MILLISECONDS);

            while (stdoutScanner.hasNextLine()) {
                output.append(stdoutScanner.nextLine()).append("\n");
            }

            while (stderrScanner.hasNextLine()) {
                errors.append(stderrScanner.nextLine()).append("\n");
            }

            if (errors.length() > 0)
                throw new SolutionRuntimeException(errors.toString());
        } catch (TimeoutException e) {
            System.out.printf(">>> SOLUTION TIMED OUT\n");
            throw new TimeoutException("Solution timed out");
        } finally {
            process.destroy();
        }

        return output.toString();
    }

    protected abstract ProcessBuilder getRunProcessBuilder(Solution solution);

    public abstract String getAcceptedLanguage();

    public abstract String getDescription();

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

package pl.edu.uj.mpi.testerka2.core.checker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.uj.mpi.testerka2.core.checker.exceptions.SolutionRuntimeException;
import pl.edu.uj.mpi.testerka2.core.entities.Solution;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * Created by shybovycha on 12/05/16.
 */
public abstract class SolutionRunner {
    private static final Logger LOG = LoggerFactory.getLogger(SolutionRunner.class);

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

        try (
        		Scanner stdoutScanner = new Scanner(process.getInputStream());
        		Scanner stderrScanner = new Scanner(process.getErrorStream());
        		PrintWriter writer = new PrintWriter(process.getOutputStream());
        ) {

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

            if (errors.length() > 0) {
                throw new SolutionRuntimeException(errors.toString());
            }
        } catch (TimeoutException e) {
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
            LOG.error("Error writing solution to a file", e);
        }
    }
}

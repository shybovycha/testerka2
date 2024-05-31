package pl.edu.uj.mpi.testerka2.api.checker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.edu.uj.mpi.testerka2.api.entities.Solution;
import pl.edu.uj.mpi.testerka2.api.entities.SolutionResult;
import pl.edu.uj.mpi.testerka2.api.entities.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Component
public abstract class SolutionChecker {
    private static final Logger LOG = LoggerFactory.getLogger(SolutionChecker.class);

    public abstract boolean accepts(Solution solution);

    public abstract void checkSingleTest(SolutionRunner runner, Solution solution, TestCase testCase, SolutionResult result) throws Exception;

    public List<SolutionResult> check(Solution solution, List<TestCase> testCases, SolutionRunner runner) {
        LOG.debug("Running test cases for solution #{}", solution.getId());

        List<SolutionResult> results = new ArrayList<>(testCases.size());

        for (TestCase testCase : testCases) {
            SolutionResult result = new SolutionResult(solution, testCase);

            // we can go on even if one test case was timed out
            try {
                checkSingleTest(runner, solution, testCase, result);

                LOG.debug("Test case #{} for solution #{} finished with {}", testCase.getId(), solution.getId(), result.getStatus());
            } catch (TimeoutException e) {
                result.setStatus(SolutionResult.Status.TIMEOUT);

                LOG.debug("Test case #{} for solution #{} timed out", testCase.getId(), solution.getId(), e);
            } catch (Exception e) {
                result.setStatus(SolutionResult.Status.RUN_ERROR);

                LOG.debug("Test case #{} for solution #{} failed with error", testCase.getId(), solution.getId(), e);
            } finally {
                results.add(result);
            }
        }

        return results;
    }
}

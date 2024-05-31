package pl.edu.uj.mpi.testerka2.worker.solution_checkers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.edu.uj.mpi.testerka2.core.checker.SolutionRunner;
import pl.edu.uj.mpi.testerka2.core.entities.Solution;
import pl.edu.uj.mpi.testerka2.core.entities.SolutionResult;
import pl.edu.uj.mpi.testerka2.core.entities.TestCase;

import java.util.Collections;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class RushHourSolutionCheckerTest {
    @Mock
    private SolutionRunner solutionRunner;

    @Mock
    private Solution solution;

    @Mock
    private TestCase testCase;

    private RushHourSolutionChecker solutionChecker;

    @Before
    public void setUp() {
        solutionChecker = new RushHourSolutionChecker();
    }

    @Test
    public void check__sets_the_checking_and_passed_status() throws Exception {
        final String input = """
            3
            X 0 3 H 2
            A 4 1 H 2
            C 4 2 V 3""";

        final String expectedOutput = """
            3
            A L 2
            C D 2
            X R 4""";

        doReturn(input).when(testCase).getInput();
        doReturn(expectedOutput).when(solutionRunner).getOutputFor(solution, input);

        assertThat("Results list has one item per test case with the success status",
                solutionChecker.check(solution, Collections.singletonList(testCase), solutionRunner),
                contains(hasProperty("status", is(SolutionResult.Status.PASSED_CORRECT)))
        );
    }

    @Test
    public void check__sets_error_solution_status_if_solution_check_returns_error() throws Exception {
        final String input = """
            3
            X 0 3 H 2
            A 4 1 H 2
            C 4 2 V 3""";

        final String expectedOutput = """
            1
            3
            A L 2
            C D 2
            X R 4""";

        doReturn(input).when(testCase).getInput();
        doReturn(expectedOutput).when(solutionRunner).getOutputFor(solution, input);

        solutionChecker.check(solution, Collections.singletonList(testCase), solutionRunner);

        assertThat("Results list has one item per test case with the success status",
                solutionChecker.check(solution, Collections.singletonList(testCase), solutionRunner),
                contains(hasProperty("status", is(SolutionResult.Status.PASSED_INCORRECT)))
        );
    }
}

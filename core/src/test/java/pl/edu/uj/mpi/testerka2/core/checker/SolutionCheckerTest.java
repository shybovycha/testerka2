package pl.edu.uj.mpi.testerka2.core.checker;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.edu.uj.mpi.testerka2.core.entities.Solution;
import pl.edu.uj.mpi.testerka2.core.entities.TestCase;
import pl.edu.uj.mpi.testerka2.core.repositories.SolutionRepository;
import pl.edu.uj.mpi.testerka2.core.repositories.SolutionResultRepository;
import pl.edu.uj.mpi.testerka2.core.repositories.TestCaseRepository;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class SolutionCheckerTest {
    @Mock
    private TestCaseRepository testCaseRepository;

    @Mock
    private SolutionRepository solutionRepository;

    @Mock
    private SolutionResultRepository solutionResultRepository;

    @Mock
    private SolutionRunner solutionRunner;

    @Mock
    private Solution solution;

    @Mock
    private TestCase testCase;

    private SolutionChecker solutionChecker;

    @Before
    public void setUp() {
        doNothing().when(solution).setStatus(any(Solution.SolutionStatus.class));
        doNothing().when(solution).setErrorMessage(any(String.class));

        doReturn(true).when(solutionRunner).accepts(solution);

        solutionChecker = new SolutionChecker(testCaseRepository, solutionRepository, solutionResultRepository, ImmutableList.of(solutionRunner));
    }

    @Test
    public void check__sets_the_checking_and_passed_status() throws Exception {
        final String input = "3\n" +
                             "X 0 3 H 2\n" +
                             "A 4 1 H 2\n" +
                             "C 4 2 V 3";

        final String expectedOutput = "3\n" +
                                      "A L 2\n" +
                                      "C D 2\n" +
                                      "X R 4";

        doReturn(input).when(testCase).getInput();
        doReturn(Collections.singletonList(testCase)).when(testCaseRepository).findAll();
        doReturn(expectedOutput).when(solutionRunner).getOutputFor(solution, input);

        solutionChecker.check(solution);

        verify(solution).setStatus(Solution.SolutionStatus.CHECKING);
        verify(solution).setStatus(Solution.SolutionStatus.PASSED_CORRECT);
    }

    @Test(expected = InvalidFieldFormatException.class)
    public void check__throws_exception_for_invalid_output() throws Exception {
        final String input = "3\n" +
                             "X 0 3 H 2\n" +
                             "A 4 1 H 2\n" +
                             "C 4 2 V 3";

        final String expectedOutput = "1\n" +
                                      "3\n" +
                                      "A L 2\n" +
                                      "C D 2\n" +
                                      "X R 4";

        doReturn(input).when(testCase).getInput();
        doReturn(Collections.singletonList(testCase)).when(testCaseRepository).findAll();
        doReturn(expectedOutput).when(solutionRunner).getOutputFor(solution, input);

        solutionChecker.check(solution);
    }
}

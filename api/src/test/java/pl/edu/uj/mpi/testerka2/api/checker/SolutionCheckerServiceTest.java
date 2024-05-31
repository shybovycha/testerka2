package pl.edu.uj.mpi.testerka2.api.checker;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.edu.uj.mpi.testerka2.api.entities.Solution;
import pl.edu.uj.mpi.testerka2.api.entities.SolutionResult;
import pl.edu.uj.mpi.testerka2.api.entities.TestCase;
import pl.edu.uj.mpi.testerka2.api.repositories.SolutionRepository;
import pl.edu.uj.mpi.testerka2.api.repositories.SolutionResultRepository;
import pl.edu.uj.mpi.testerka2.api.repositories.TestCaseRepository;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class SolutionCheckerServiceTest {
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

    @Mock
    private SolutionChecker solutionChecker;

    @Mock
    private SolutionResult solutionResult;

    private SolutionCheckerService solutionCheckerService;

    @Before
    public void setUp() {
        doNothing().when(solution).setStatus(any(Solution.Status.class));

        doReturn(true).when(solutionRunner).accepts(solution);
        doReturn(true).when(solutionChecker).accepts(solution);

        solutionCheckerService = new SolutionCheckerService(testCaseRepository,
                solutionRepository,
                solutionResultRepository,
                List.of(solutionRunner),
                List.of(solutionChecker)
        );
    }

    @Test
    public void check__sets_the_checking_and_passed_status() throws Exception {
        doReturn(Collections.singletonList(testCase)).when(testCaseRepository).findAll();

        doReturn(SolutionResult.Status.PASSED_CORRECT).when(solutionResult).getStatus();
        doReturn(Collections.singletonList(solutionResult)).when(solutionChecker).check(solution, Collections.singletonList(testCase), solutionRunner);

        solutionCheckerService.check(solution);

        verify(solution).setStatus(Solution.Status.CHECKING);
        verify(solution).setStatus(Solution.Status.PASSED_CORRECT);
    }

    @Test
    public void check__sets_error_solution_status_if_solution_check_returns_error() throws Exception {
        doReturn(Collections.singletonList(testCase)).when(testCaseRepository).findAll();

        doReturn(SolutionResult.Status.RUN_ERROR).when(solutionResult).getStatus();
        doReturn(Collections.singletonList(solutionResult)).when(solutionChecker).check(solution, Collections.singletonList(testCase), solutionRunner);

        solutionCheckerService.check(solution);

        verify(solution).setStatus(Solution.Status.CHECKING);
        verify(solution).setStatus(Solution.Status.RUN_ERROR);
    }

    @Test
    public void check__sets_incorrect_solution_status_if_solution_check_returns_incorrect() throws Exception {
        doReturn(Collections.singletonList(testCase)).when(testCaseRepository).findAll();

        doReturn(SolutionResult.Status.PASSED_INCORRECT).when(solutionResult).getStatus();
        doReturn(Collections.singletonList(solutionResult)).when(solutionChecker).check(solution, Collections.singletonList(testCase), solutionRunner);

        solutionCheckerService.check(solution);

        verify(solution).setStatus(Solution.Status.CHECKING);
        verify(solution).setStatus(Solution.Status.PASSED_INCORRECT);
    }

}

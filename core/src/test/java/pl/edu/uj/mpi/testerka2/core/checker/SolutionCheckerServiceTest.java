package pl.edu.uj.mpi.testerka2.core.checker;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.edu.uj.mpi.testerka2.core.entities.Solution;
import pl.edu.uj.mpi.testerka2.core.entities.SolutionResult;
import pl.edu.uj.mpi.testerka2.core.entities.SolutionResultStatus;
import pl.edu.uj.mpi.testerka2.core.entities.SolutionStatus;
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
        doNothing().when(solution).setStatus(any(SolutionStatus.class));

        doReturn(true).when(solutionRunner).accepts(solution);
        doReturn(true).when(solutionChecker).accepts(solution);

        solutionCheckerService = new SolutionCheckerService(testCaseRepository,
                solutionRepository,
                solutionResultRepository,
                ImmutableList.of(solutionRunner),
                ImmutableList.of(solutionChecker)
        );
    }

    @Test
    public void check__sets_the_checking_and_passed_status() throws Exception {
        doReturn(Collections.singletonList(testCase)).when(testCaseRepository).findAll();

        doReturn(SolutionResultStatus.PASSED_CORRECT).when(solutionResult).getStatus();
        doReturn(Collections.singletonList(solutionResult)).when(solutionChecker).check(solution, Collections.singletonList(testCase), solutionRunner);

        solutionCheckerService.check(solution);

        verify(solution).setStatus(SolutionStatus.CHECKING);
        verify(solution).setStatus(SolutionStatus.PASSED_CORRECT);
    }

    @Test
    public void check__sets_error_solution_status_if_solution_check_returns_error() throws Exception {
        doReturn(Collections.singletonList(testCase)).when(testCaseRepository).findAll();

        doReturn(SolutionResultStatus.RUN_ERROR).when(solutionResult).getStatus();
        doReturn(Collections.singletonList(solutionResult)).when(solutionChecker).check(solution, Collections.singletonList(testCase), solutionRunner);

        solutionCheckerService.check(solution);

        verify(solution).setStatus(SolutionStatus.CHECKING);
        verify(solution).setStatus(SolutionStatus.RUN_ERROR);
    }

    @Test
    public void check__sets_incorrect_solution_status_if_solution_check_returns_incorrect() throws Exception {
        doReturn(Collections.singletonList(testCase)).when(testCaseRepository).findAll();

        doReturn(SolutionResultStatus.PASSED_INCORRECT).when(solutionResult).getStatus();
        doReturn(Collections.singletonList(solutionResult)).when(solutionChecker).check(solution, Collections.singletonList(testCase), solutionRunner);

        solutionCheckerService.check(solution);

        verify(solution).setStatus(SolutionStatus.CHECKING);
        verify(solution).setStatus(SolutionStatus.PASSED_INCORRECT);
    }

}

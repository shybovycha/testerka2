package pl.edu.uj.mpi.testerka2.core.checker;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.edu.uj.mpi.testerka2.core.entities.Solution;
import pl.edu.uj.mpi.testerka2.core.entities.SolutionResult;
import pl.edu.uj.mpi.testerka2.core.entities.TestCase;
import pl.edu.uj.mpi.testerka2.core.repositories.SolutionResultRepository;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class PointCalculatorTest {
    private final static long TEST_CASE1_ID = 41L;

    @Mock
    private SolutionResultRepository solutionResultRepository;

    @Mock
    private Solution solution;

    @Mock
    private SolutionResult solutionResult;

    @Mock
    private Solution solution1;

    @Mock
    private Solution solution2;

    @Mock
    private Solution solution3;

    @Mock
    private SolutionResult solutionResult1;

    @Mock
    private SolutionResult solutionResult2;

    @Mock
    private SolutionResult solutionResult3;

    @Mock
    private TestCase testCase1;

    private PointCalculator pointCalculator;

    @Before
    public void setUp() {
        doReturn(TEST_CASE1_ID).when(testCase1).getId();

        doReturn(testCase1).when(solutionResult1).getTestCase();
        doReturn(testCase1).when(solutionResult2).getTestCase();
        doReturn(testCase1).when(solutionResult3).getTestCase();

        doReturn(solution1).when(solutionResult1).getSolution();
        doReturn(solution2).when(solutionResult2).getSolution();
        doReturn(solution3).when(solutionResult3).getSolution();

        doReturn(Collections.singletonList(solutionResult)).when(solution).getResults();
        doReturn(testCase1).when(solutionResult).getTestCase();

        doReturn(Solution.SolutionStatus.PASSED_CORRECT).when(solution1).getStatus();
        doReturn(Solution.SolutionStatus.PASSED_CORRECT).when(solution2).getStatus();
        doReturn(Solution.SolutionStatus.PASSED_CORRECT).when(solution3).getStatus();

        doReturn(2).when(solutionResult1).getPoints();
        doReturn(5).when(solutionResult2).getPoints();
        doReturn(10).when(solutionResult3).getPoints();
        doReturn(10).when(solutionResult).getPoints();

        pointCalculator = new PointCalculator(solutionResultRepository);
    }

    @Test
    public void getPointsFor__returns_average_points_deviation_percentage_across_all_solutions_ever_submitted() {
        // no solutions
        doReturn(Collections.emptyList()).when(solutionResultRepository).findAll();

        assertThat("No solutions result in 0 deviation", pointCalculator.getPointsFor(solution), is(0));

        // only solution1 (2)
        doReturn(Collections.singletonList(solutionResult1)).when(solutionResultRepository).findAll();

        assertThat("Only minimal points are used for deviation", pointCalculator.getPointsFor(solution), is(20));

        // only solution1 (2) and solution3 (10)
        doReturn(ImmutableList.of(solutionResult1, solutionResult3)).when(solutionResultRepository).findAll();

        assertThat("Only minimal points are used for deviation", pointCalculator.getPointsFor(solution), is(20));

        // only solution2 (5) and solution3 (10)
        doReturn(ImmutableList.of(solutionResult2, solutionResult3)).when(solutionResultRepository).findAll();

        assertThat("Only minimal points are used for deviation", pointCalculator.getPointsFor(solution), is(50));

        // solution1 (2), solution2 (5) and solution3 (10)
        doReturn(ImmutableList.of(solutionResult1, solutionResult2, solutionResult3)).when(solutionResultRepository).findAll();

        assertThat("Only minimal points are used for deviation", pointCalculator.getPointsFor(solution), is(20));

        // only solution3 (10)
        doReturn(ImmutableList.of(solutionResult3)).when(solutionResultRepository).findAll();

        assertThat("Only minimal points are used for deviation", pointCalculator.getPointsFor(solution), is(100));
    }

    @Test
    public void getPointsFor__returns_average_points_deviation_percentage_across_all_passed_solutions_ever_submitted() {
        doReturn(Solution.SolutionStatus.PASSED_CORRECT).when(solution1).getStatus();
        doReturn(Solution.SolutionStatus.PASSED_INCORRECT).when(solution2).getStatus();
        doReturn(Solution.SolutionStatus.REJECTED).when(solution3).getStatus();

        // no solutions
        doReturn(Collections.emptyList()).when(solutionResultRepository).findAll();

        assertThat("No solutions result in 0 deviation", pointCalculator.getPointsFor(solution), is(0));

        // only solution1 (2)
        doReturn(Collections.singletonList(solutionResult1)).when(solutionResultRepository).findAll();

        assertThat("One passed solution results in the deviation", pointCalculator.getPointsFor(solution), is(20));

        // only solution1 (2) and solution3 (10)
        doReturn(ImmutableList.of(solutionResult1, solutionResult3)).when(solutionResultRepository).findAll();

        assertThat("One passed and one incorrect solution result in the deviation of the success solution only", pointCalculator.getPointsFor(solution), is(20));

        // only solution2 (5) and solution3 (10)
        doReturn(ImmutableList.of(solutionResult2, solutionResult3)).when(solutionResultRepository).findAll();

        assertThat("One failed and one incorrect solution result in 0 deviation", pointCalculator.getPointsFor(solution), is(0));

        // solution1 (2), solution2 (5) and solution3 (10)
        doReturn(ImmutableList.of(solutionResult1, solutionResult2, solutionResult3)).when(solutionResultRepository).findAll();

        assertThat("One passed, one incorrect and one failed solution result in the deviation of the success solution only", pointCalculator.getPointsFor(solution), is(20));

        // only solution3 (10)
        doReturn(ImmutableList.of(solutionResult3)).when(solutionResultRepository).findAll();

        assertThat("One failed solution results in 0 deviation", pointCalculator.getPointsFor(solution), is(0));
    }
}

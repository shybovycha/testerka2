package pl.edu.uj.mpi.testerka2.worker.solution_checkers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.edu.uj.mpi.testerka2.core.checker.SolutionChecker;
import pl.edu.uj.mpi.testerka2.core.checker.SolutionRunner;
import pl.edu.uj.mpi.testerka2.core.entities.Solution;
import pl.edu.uj.mpi.testerka2.core.entities.SolutionResult;
import pl.edu.uj.mpi.testerka2.core.entities.TestCase;
import pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.Field;
import pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.FieldParser;
import pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.Move;
import pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.MoveParser;
import pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.exceptions.InvalidFieldFormatException;

import java.util.List;

@Component
public class RushHourSolutionChecker extends SolutionChecker {
    private static final Logger LOG = LoggerFactory.getLogger(RushHourSolutionChecker.class);

    public RushHourSolutionChecker() {}

    @Override
    public boolean accepts(Solution solution) {
        return true;
    }

    @Override
    public void checkSingleTest(SolutionRunner runner, Solution solution, TestCase testCase, SolutionResult result) throws Exception {
        result.setStatus(SolutionResult.Status.CHECKING);

        String output = runner.getOutputFor(solution, testCase.getInput());

        result.setOutput(output);

        try {
            Field field = new FieldParser().parseField(testCase.getInput());

            List<Move> moves = new MoveParser(field).parseAll(output);

            result.setPoints(moves.size());

            for (Move move : moves) {
                if (!field.isMoveValid(move)) {
                    LOG.debug("Test case #{} for solution #{}: move {} is not valid", testCase.getId(), solution.getId(), move);
                    result.setStatus(SolutionResult.Status.PASSED_INCORRECT);
                    return;
                }

                field = field.applyMove(move);
            }

            if (field.isSolved()) {
                result.setStatus(SolutionResult.Status.PASSED_CORRECT);
            } else {
                result.setStatus(SolutionResult.Status.PASSED_INCORRECT);
            }
        } catch (InvalidFieldFormatException e) {
            result.setStatus(SolutionResult.Status.PASSED_INCORRECT);
        }
    }
}

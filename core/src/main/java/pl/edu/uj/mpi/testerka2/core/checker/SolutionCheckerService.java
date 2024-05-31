package pl.edu.uj.mpi.testerka2.core.checker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.uj.mpi.testerka2.core.entities.Solution;
import pl.edu.uj.mpi.testerka2.core.entities.SolutionResult;
import pl.edu.uj.mpi.testerka2.core.entities.TestCase;
import pl.edu.uj.mpi.testerka2.core.repositories.SolutionRepository;
import pl.edu.uj.mpi.testerka2.core.repositories.SolutionResultRepository;
import pl.edu.uj.mpi.testerka2.core.repositories.TestCaseRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;

@Service
public class SolutionCheckerService {
    private static final Logger LOG = LoggerFactory.getLogger(SolutionCheckerService.class);

    protected TestCaseRepository testCaseRepository;
    protected SolutionRepository solutionRepository;
    protected SolutionResultRepository solutionResultRepository;
    protected List<SolutionRunner> runnersAvailable;
    protected List<SolutionChecker> checkersAvailable;

    @Autowired
    public SolutionCheckerService(
        TestCaseRepository testCaseRepository,
        SolutionRepository solutionRepository,
        SolutionResultRepository solutionResultRepository,
        List<SolutionRunner> runnersAvailable,
        List<SolutionChecker> checkersAvailable) {

        this.testCaseRepository = testCaseRepository;
        this.solutionRepository = solutionRepository;
        this.solutionResultRepository = solutionResultRepository;
        this.runnersAvailable = runnersAvailable;
        this.checkersAvailable = checkersAvailable;
    }

    // gets list of test cases passed successfully
    public void check(Solution solution) {
        Optional<SolutionRunner> runner = runnersAvailable.stream().filter(r -> r.accepts(solution)).findFirst();

        solution.setStatus(Solution.Status.CHECKING);
        solutionRepository.save(solution);

        LOG.debug("Checking solution {}", solution.getId());

        if (runner.isEmpty()) {
            solution.setStatus(Solution.Status.REJECTED);
            solutionRepository.save(solution);
            LOG.warn("Rejected solution {} - no suitable runner found", solution.getId());
            return;
        }

        // as we do not have dependency injection here, we have no TestCaseRepository
        // thus make this class being instantiated once
        List<TestCase> testCases = (List<TestCase>) testCaseRepository.findAll();

        Optional<SolutionChecker> checker = checkersAvailable.stream().filter(c -> c.accepts(solution)).findFirst();

        if (checker.isEmpty()) {
            solution.setStatus(Solution.Status.REJECTED);
            solutionRepository.save(solution);
            LOG.warn("Rejected solution {} - no suitable checker found", solution.getId());
            return;
        }

        try {
            List<SolutionResult> allResults = checker.get().check(solution, testCases, runner.get());

            Map<SolutionResult.Status, List<SolutionResult>> resultsByStatus = allResults.stream().collect(groupingBy(SolutionResult::getStatus));

            if (resultsByStatus.containsKey(SolutionResult.Status.REJECTED)) {
                solution.setStatus(Solution.Status.REJECTED);
            } else if (resultsByStatus.containsKey(SolutionResult.Status.RUN_ERROR)) {
                solution.setStatus(Solution.Status.RUN_ERROR);
            } else if (resultsByStatus.containsKey(SolutionResult.Status.TIMEOUT)) {
                solution.setStatus(Solution.Status.TIMEOUT);
            } else if (resultsByStatus.containsKey(SolutionResult.Status.PASSED_INCORRECT)) {
                solution.setStatus(Solution.Status.PASSED_INCORRECT);
            } else if (resultsByStatus.containsKey(SolutionResult.Status.PASSED_CORRECT)) {
                solution.setStatus(Solution.Status.PASSED_CORRECT);
            } else {
                solution.setStatus(Solution.Status.REJECTED);
            }

            solutionResultRepository.saveAll(allResults);

            solution.setResults(allResults);
            solutionRepository.save(solution);
        } catch (Exception e) {
            // but for more critical errors we stop checker
            solution.setStatus(Solution.Status.RUN_ERROR);
            solution.setErrorMessage(e.getMessage());
            solutionRepository.save(solution);

            LOG.error("Errors during running solution #{}", solution.getId(), e);

            throw e;
        }

        LOG.debug("Solution #{} checks finished with status {}", solution.getId(), solution.getStatus());
    }
}

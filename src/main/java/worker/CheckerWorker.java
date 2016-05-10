package worker;

import checker.SolutionChecker;
import checker.entities.Solution;
import checker.repositories.SolutionRepository;
import checker.repositories.SolutionResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by shybovycha on 10/05/16.
 */
@Component
public class CheckerWorker {
    @Autowired
    SolutionRepository solutionRepository;

    @Autowired
    SolutionResultRepository solutionResultRepository;

    @Scheduled(fixedRate = 5000)
    public void checkForPendingSolutions() {
        Iterable<Solution> pendingSolutions = solutionRepository.findByStatus(Solution.SolutionStatus.PENDING);

        SolutionChecker checker = new SolutionChecker();

        for (Solution solution : pendingSolutions) {
            solution.setStatus(Solution.SolutionStatus.CHECKING);

            solutionResultRepository.save(checker.check(solution));

            solution.setStatus(Solution.SolutionStatus.DONE);
        }
    }
}

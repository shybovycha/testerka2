package worker;

import checker.SolutionChecker;
import checker.entities.Solution;
import checker.repositories.SolutionRepository;
import checker.repositories.SolutionResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by shybovycha on 10/05/16.
 */
@Component
@EnableScheduling
@EnableJpaRepositories("checker.repositories")
@EntityScan("checker.entities")
@EnableAutoConfiguration
public class CheckerWorker {
    @Autowired
    SolutionRepository solutionRepository;

    @Autowired
    SolutionResultRepository solutionResultRepository;

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void checkForPendingSolutions() {
        Iterable<Solution> pendingSolutions = solutionRepository.findByStatus(Solution.SolutionStatus.PENDING);

        SolutionChecker checker = new SolutionChecker();

        for (Solution solution : pendingSolutions) {
            solution.setStatus(Solution.SolutionStatus.CHECKING);
            solutionRepository.save(solution);

            solutionResultRepository.save(checker.check(solution));

            solution.setStatus(Solution.SolutionStatus.DONE);
            solutionRepository.save(solution);
        }
    }
}

package worker;

import checker.RunnableSolution;
import checker.SolutionChecker;
import checker.SolutionFactory;
import checker.entities.Solution;
import checker.repositories.SolutionRepository;
import checker.repositories.SolutionResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
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
@ComponentScan("checker")
public class CheckerWorker {
    @Autowired
    SolutionRepository solutionRepository;

    @Autowired
    SolutionResultRepository solutionResultRepository;

    @Autowired
    SolutionFactory solutionFactory;

    @Autowired
    SolutionChecker checker;

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void checkForPendingSolutions() {
        Iterable<Solution> pendingSolutions = solutionRepository.findByStatus(Solution.SolutionStatus.PENDING);

        for (Solution solution : pendingSolutions) {
            solution.setStatus(Solution.SolutionStatus.CHECKING);
            solutionRepository.save(solution);

            RunnableSolution runnable = solutionFactory.forLanguage(solution);

            if (runnable == null)
                continue;

            solutionResultRepository.save(checker.check(runnable));

            solution.setStatus(Solution.SolutionStatus.DONE);
            solutionRepository.save(solution);
        }
    }
}

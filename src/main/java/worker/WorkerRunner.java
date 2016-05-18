package worker;

import checker.SolutionChecker;
import checker.entities.Solution;
import checker.repositories.SolutionRepository;
import checker.repositories.SolutionResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
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
@SpringBootApplication(exclude = { EmbeddedServletContainerAutoConfiguration.class, WebMvcAutoConfiguration.class })
public class WorkerRunner {
    @Autowired
    SolutionRepository solutionRepository;

    @Autowired
    SolutionResultRepository solutionResultRepository;

    @Autowired
    SolutionChecker checker;

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void checkForPendingSolutions() {
        Iterable<Solution> pendingSolutions = solutionRepository.findByStatus(Solution.SolutionStatus.PENDING);

        for (Solution solution : pendingSolutions) {
            solution.setStatus(Solution.SolutionStatus.CHECKING);
            solutionRepository.save(solution);

            checker.check(solution);
        }
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplicationBuilder()
            .sources(WorkerRunner.class)
            .web(false)
            .build();

        app.run(args);
    }
}

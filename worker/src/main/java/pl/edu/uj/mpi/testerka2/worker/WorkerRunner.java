package pl.edu.uj.mpi.testerka2.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.edu.uj.mpi.testerka2.api.checker.SolutionCheckerService;
import pl.edu.uj.mpi.testerka2.api.entities.Solution;
import pl.edu.uj.mpi.testerka2.api.repositories.SolutionRepository;

import java.util.List;

/**
 * Created by shybovycha on 10/05/16.
 */
@Component
@EnableScheduling
@EnableJpaRepositories("pl.edu.uj.mpi.testerka2.api.repositories")
@EntityScan("pl.edu.uj.mpi.testerka2.api.entities")
@ComponentScan(basePackages = {"pl.edu.uj.mpi.testerka2.api"})
@SpringBootApplication(exclude = {ServletWebServerFactoryAutoConfiguration.class, WebMvcAutoConfiguration.class})
public class WorkerRunner {
    private final SolutionRepository solutionRepository;
    private final SolutionCheckerService checkerService;

    @Autowired
    public WorkerRunner(
        SolutionRepository solutionRepository,
        SolutionCheckerService checkerService
    ) {
        this.solutionRepository = solutionRepository;
        this.checkerService = checkerService;
    }

    @Scheduled(fixedRate = 5000)
    public void checkForPendingSolutions() {
        List<Solution> pendingSolutions = solutionRepository.findByStatus(Solution.Status.PENDING);

        for (Solution solution : pendingSolutions) {
            solution.setStatus(Solution.Status.CHECKING);
            solutionRepository.save(solution);

            checkerService.check(solution);
        }
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplicationBuilder()
            .sources(WorkerRunner.class)
            .web(WebApplicationType.NONE)
            .build();

        app.run(args);
    }
}

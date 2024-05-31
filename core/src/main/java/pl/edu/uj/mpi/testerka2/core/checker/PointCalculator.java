package pl.edu.uj.mpi.testerka2.core.checker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.uj.mpi.testerka2.core.entities.Solution;
import pl.edu.uj.mpi.testerka2.core.entities.SolutionResult;
import pl.edu.uj.mpi.testerka2.core.entities.TestCase;
import pl.edu.uj.mpi.testerka2.core.repositories.SolutionResultRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by shybovycha on 30/05/16.
 */
@Service
public class PointCalculator {
    private final SolutionResultRepository solutionResultRepository;

    @Autowired
    public PointCalculator(SolutionResultRepository solutionResultRepository) {
        this.solutionResultRepository = solutionResultRepository;
    }

    public int getPointsFor(Solution solution) {
        List<SolutionResult> results = (List<SolutionResult>) solutionResultRepository.findAll();

        Map<TestCase, List<SolutionResult>> solutionsByTestCase = results.stream()
                .filter(r -> r.getSolution().getStatus() == Solution.Status.PASSED_CORRECT)
                .collect(Collectors.groupingBy(SolutionResult::getTestCase));

        double avgPercent = solutionsByTestCase
                .entrySet()
                .stream()
                .mapToDouble(e -> {
                    Optional<SolutionResult> solutionResultOpt = solution.getResults().stream()
                            .filter(r -> r.getTestCase().getId() == e.getKey().getId())
                            .findFirst();

                    int points = solutionResultOpt.map(SolutionResult::getPoints).orElse(0);

                    int minPoints = e.getValue().stream().mapToInt(SolutionResult::getPoints).min().orElse(0);

                    return (double) minPoints / (double) points;
                })
                .average()
                .orElse(0);

        return (int) (avgPercent * 100);
    }
}

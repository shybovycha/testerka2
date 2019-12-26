package pl.edu.uj.mpi.testerka2.core.checker;

import pl.edu.uj.mpi.testerka2.core.entities.Solution;
import pl.edu.uj.mpi.testerka2.core.entities.SolutionResult;
import pl.edu.uj.mpi.testerka2.core.repositories.SolutionResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shybovycha on 30/05/16.
 */
@Service
public class PointCalculator {
    @Autowired
    protected SolutionResultRepository solutionResultRepository;

    public PointCalculator() {}

    public int getPointsFor(Solution solution) {
        List<SolutionResult> results = (List<SolutionResult>) solutionResultRepository.findAll();

        double avgPercent = results.stream()
                .filter(r -> r.getSolution().getStatus() == Solution.SolutionStatus.PASSED_CORRECT)
                .collect(Collectors.groupingBy(SolutionResult::getTestCase))
                .entrySet().stream()
                .mapToDouble(e -> {
                    int points = solution.getResults().stream()
                            .filter(r -> r.getTestCase().getId() == e.getKey().getId())
                            .findFirst().get().getPoints();

                    int minPoints = e.getValue().stream().mapToInt(SolutionResult::getPoints).min().getAsInt();

                    return (double) minPoints / (double) points;
                })
                .average()
                .getAsDouble();

        return (int) (avgPercent * 100);
    }
}

package core.checker;

import core.entities.Solution;
import core.entities.SolutionResult;
import core.repositories.SolutionResultRepository;
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
                .collect(Collectors.groupingBy(SolutionResult::getTestCase))
                .entrySet().stream()
                .mapToDouble(e -> {
                    int points = solution.getResults().stream()
                            .filter(r -> r.getTestCase().getId() == e.getKey().getId())
                            .findFirst().get().getPoints();

                    int maxPoints = e.getValue().stream().mapToInt(SolutionResult::getPoints).max().getAsInt();

                    return (double) points / (double) maxPoints;
                })
                .average()
                .getAsDouble();

        return (int) (avgPercent * 100);
    }
}

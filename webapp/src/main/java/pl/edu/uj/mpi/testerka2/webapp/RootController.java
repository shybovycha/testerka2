package pl.edu.uj.mpi.testerka2.webapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.uj.mpi.testerka2.core.checker.PointCalculator;
import pl.edu.uj.mpi.testerka2.core.checker.SolutionRunner;
import pl.edu.uj.mpi.testerka2.core.entities.Solution;
import pl.edu.uj.mpi.testerka2.core.entities.SolutionResult;
import pl.edu.uj.mpi.testerka2.core.repositories.SolutionRepository;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@EnableAutoConfiguration
@EnableJpaRepositories("pl.edu.uj.mpi.testerka2.core.repositories")
@EntityScan("pl.edu.uj.mpi.testerka2.core.entities")
@ComponentScan(basePackages = {"pl.edu.uj.mpi.testerka2.core"})
@Controller
public class RootController {
    private static final Logger LOG = LoggerFactory.getLogger(RootController.class);

    private final SolutionRepository solutionRepository;
    private final List<SolutionRunner> availableRunners;
    private final PointCalculator pointCalculator;

    @Autowired
    public RootController(
        SolutionRepository solutionRepository,
        List<SolutionRunner> availableRunners,
        PointCalculator pointCalculator
    ) {
        this.solutionRepository = solutionRepository;
        this.availableRunners = availableRunners;
        this.pointCalculator = pointCalculator;
    }

    @RequestMapping("/")
    String home(Model model) {
        List<Solution> solutions = StreamSupport.stream(solutionRepository.findAll().spliterator(), false)
                .filter(s -> s.getStatus() == Solution.Status.PASSED_CORRECT)
                .sorted(Comparator.comparingInt(s -> s.getResults().stream()
                        .map(SolutionResult::getPoints).mapToInt(Integer::intValue).sum()))
                .sorted((s1, s2) -> Long.compare(s2.getCreatedAt().getTime(), s1.getCreatedAt().getTime()))
                .collect(Collectors.toList());

        solutions.forEach(s -> s.setPoints(pointCalculator.getPointsFor(s)));

        model.addAttribute("allSolutions", solutions);

        model.addAttribute("solution", new Solution());
        model.addAttribute("runners", availableRunners);

        return "index";
    }

    @RequestMapping(value = "/solution/{id}")
    String viewSolution(@PathVariable("id") Long solutionId, Model model) {
        Optional<Solution> solution = solutionRepository.findById(solutionId);

        model.addAttribute("solution", solution.orElse(null));

        return "view-solution";
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    String submitSolution(@RequestParam("source") MultipartFile sourceFile,
                          @RequestParam("language") String language,
                          @RequestParam("author") String author) {
        Solution solution = new Solution();

        if (!sourceFile.isEmpty()) {
            StringBuilder source = new StringBuilder();

            try (Scanner scanner = new Scanner(sourceFile.getInputStream())) {
                while (scanner.hasNextLine()) {
                    source.append(scanner.nextLine());
                    source.append("\n");
                }
            } catch (IOException e) {
                LOG.error("Can not submit solution", e);
            }

            solution.setSource(source.toString());
        }

        solution.setAuthor(author);
        solution.setLanguage(language);

        solutionRepository.save(solution);

        return String.format("redirect:/solution/%d", solution.getId());
    }

    @RequestMapping("/results")
    String results(Model model) {
        List<Solution> solutions = StreamSupport.stream(solutionRepository.findAll().spliterator(), false)
                .filter(s -> s.getStatus() == Solution.Status.PASSED_CORRECT)
                .sorted(Comparator.comparingInt(s -> s.getResults().stream()
                        .map(SolutionResult::getPoints).mapToInt(Integer::intValue).sum()))
                .sorted((s1, s2) -> Long.compare(s2.getCreatedAt().getTime(), s1.getCreatedAt().getTime()))
                .toList();

        solutions.forEach(s -> s.setPoints(pointCalculator.getPointsFor(s)));

        Map<String, Solution> solutionMap = new TreeMap<>();

        for (Solution s : solutions) {
            Solution prev = solutionMap.get(s.getAuthor());

            if (prev == null || s.getPoints() > prev.getPoints()) {
                solutionMap.put(s.getAuthor(), s);
            }
        }

        List<Solution> bestSolutions = solutionMap.values()
                .stream()
                .sorted(Comparator.comparing(Solution::getAuthor))
                .collect(Collectors.toList());

        model.addAttribute("allSolutions", bestSolutions);

        return "results";
    }

    public static void main(String[] args) {
        SpringApplication.run(RootController.class, args);
    }
}

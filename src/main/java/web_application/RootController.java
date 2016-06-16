package web_application;

import core.checker.PointCalculator;
import core.checker.SolutionRunner;
import core.entities.Solution;
import core.entities.SolutionResult;
import core.repositories.SolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@EnableAutoConfiguration
@EnableJpaRepositories("core.repositories")
@EntityScan("core.entities")
@ComponentScan(basePackages = {"core"})
@Controller
public class RootController {
    @Autowired
    SolutionRepository solutionRepository;

    @Autowired
    List<SolutionRunner> availableRunners;

    @Autowired
    PointCalculator pointCalculator;

    @RequestMapping("/")
    String home(Model model) {
        List<Solution> solutions = StreamSupport.stream(solutionRepository.findAll().spliterator(), false)
                .filter(s -> s.getStatus() == Solution.SolutionStatus.PASSED_CORRECT)
                .sorted((s1, s2) ->
                        Integer.compare(
                                s1.getResults().stream()
                                        .map(SolutionResult::getPoints)
                                        .collect(Collectors.summingInt(Integer::intValue)),
                                s2.getResults().stream()
                                        .map(SolutionResult::getPoints)
                                        .collect(Collectors.summingInt(Integer::intValue))))
                .sorted((s1, s2) -> Long.compare(s2.getCreatedAt().getTime(), s1.getCreatedAt().getTime()))
                .collect(Collectors.toList());

        solutions.stream().forEach(s -> s.setPoints(pointCalculator.getPointsFor(s)));

        model.addAttribute("allSolutions", solutions);

        model.addAttribute("solution", new Solution());
        model.addAttribute("runners", availableRunners);

        return "index";
    }

    @RequestMapping(value = "/solution/{id}")
    String viewSolution(@PathVariable("id") Long solutionId, Model model) {
        Solution solution = solutionRepository.findOne(solutionId);

        model.addAttribute("solution", solution);

        return "view-solution";
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    String submitSolution(@RequestParam("source") MultipartFile sourceFile,
                          @RequestParam() String language,
                          @RequestParam("author") String author) {
        Solution solution = new Solution();

        if (!sourceFile.isEmpty()) {
            StringBuilder source = new StringBuilder();

            try {
                Scanner scanner = new Scanner(sourceFile.getInputStream());

                while (scanner.hasNextLine()) {
                    source.append(scanner.nextLine());
                    source.append("\n");
                }
            } catch (IOException e) {
                // TODO: logger
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
                .filter(s -> s.getStatus() == Solution.SolutionStatus.PASSED_CORRECT)
                .sorted((s1, s2) ->
                        Integer.compare(
                                s1.getResults().stream()
                                        .map(SolutionResult::getPoints)
                                        .collect(Collectors.summingInt(Integer::intValue)),
                                s2.getResults().stream()
                                        .map(SolutionResult::getPoints)
                                        .collect(Collectors.summingInt(Integer::intValue))))
                .sorted((s1, s2) -> Long.compare(s2.getCreatedAt().getTime(), s1.getCreatedAt().getTime()))
                .collect(Collectors.toList());

        solutions.stream().forEach(s -> s.setPoints(pointCalculator.getPointsFor(s)));

        Map<String, Solution> bestSolutions = new HashMap<>();

        for (Solution s : solutions) {
            Solution prev = bestSolutions.get(s.getAuthor());

            if ((prev == null) || (prev != null && s.getPoints() > prev.getPoints())) {
                bestSolutions.put(s.getAuthor(), s);
            }
        }

        model.addAttribute("allSolutions", bestSolutions.values());

        return "results";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(RootController.class, args);
    }
}

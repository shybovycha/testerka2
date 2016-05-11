package web_application;

import checker.entities.Solution;
import checker.repositories.SolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Scanner;

@EnableAutoConfiguration
@EnableJpaRepositories("checker.repositories")
@EntityScan("checker.entities")
@Controller
public class RootController {
    @Autowired
    SolutionRepository solutionRepository;

    @RequestMapping("/")
    String home(Model model) {
        model.addAttribute("solution", new Solution());

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

                while (scanner.hasNext())
                    source.append(scanner.nextLine());
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

    public static void main(String[] args) throws Exception {
        SpringApplication.run(RootController.class, args);
    }
}

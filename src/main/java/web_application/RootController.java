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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@EnableAutoConfiguration
@EnableJpaRepositories("checker.repositories")
@EntityScan("checker.entities")
@Controller
public class RootController {
    @Autowired
    SolutionRepository solutionRepository;

    @RequestMapping("/")
    String home() {
        return "index";
    }

    @RequestMapping(value = "/solution")
    String viewSolution(@RequestParam("id") Long solutionId, Model model) {
        Solution solution = solutionRepository.findOne(solutionId);

        model.addAttribute("solution", solution);

        return "view-solution";
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    ModelAndView submitSolution(@ModelAttribute Solution solution, ModelMap model) {
        solutionRepository.save(solution);

        model.addAttribute("solutionId", solution.getId());

        return new ModelAndView("redirect:/solution", model);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(RootController.class, args);
    }
}

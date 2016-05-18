package core.solution_runners;

import core.checker.SolutionRunner;
import core.entities.Solution;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by shybovycha on 12/05/16.
 */
@Service
public class RubySolutionRunner extends SolutionRunner {
    public RubySolutionRunner() {}

    @Override
    protected ProcessBuilder getRunProcessBuilder(Solution solution) {
        String sourceFileName = "main.rb";

        writeSolutionToFile(solution, sourceFileName);

        ProcessBuilder pb = new ProcessBuilder("ruby", "main.rb");
        pb.directory(new File(this.getSolutionDir(solution)));

        return pb;
    }

    @Override
    protected String getAcceptedLanguage() {
        return "ruby";
    }

    @Override
    protected String getDescription() {
        return "Ruby";
    }
}

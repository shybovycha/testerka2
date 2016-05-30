package core.solution_runners;

import core.checker.SolutionRunner;
import core.entities.Solution;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by shybovycha on 12/05/16.
 */
@Service
public class Python3SolutionRunner extends SolutionRunner {
    public Python3SolutionRunner() {}

    @Override
    protected ProcessBuilder getRunProcessBuilder(Solution solution) {
        String sourceFileName = "main.py";

        writeSolutionToFile(solution, sourceFileName);

        ProcessBuilder pb = new ProcessBuilder("python3", "main.py");
        pb.directory(new File(this.getSolutionDir(solution)));

        return pb;
    }

    @Override
    public String getAcceptedLanguage() {
        return "python3";
    }

    @Override
    public String getDescription() {
        return "Python 3";
    }
}

package core.solution_runners;

import core.checker.CompiledSolutionRunner;
import core.entities.Solution;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by shybovycha on 12/05/16.
 */
@Service
public class KotlinSolutionRunner extends CompiledSolutionRunner {
    public KotlinSolutionRunner() {
    }

    @Override
    protected ProcessBuilder getCompilerProcessBuilder(Solution solution) {
        String sourceFileName = "main.kt";

        writeSolutionToFile(solution, sourceFileName);

        ProcessBuilder pb = new ProcessBuilder("kotlinc", "-include-runtime", sourceFileName, "-d", "main.jar");
        pb.directory(new File(this.getSolutionDir(solution)));

        return pb;
    }

    @Override
    protected ProcessBuilder getRunProcessBuilder(Solution solution) {
        ProcessBuilder pb = new ProcessBuilder("java", "-jar", "main.jar");
        pb.directory(new File(this.getSolutionDir(solution)));

        return pb;
    }

    @Override
    protected String getAcceptedLanguage() {
        return "kotlin";
    }

    @Override
    protected String getDescription() {
        return "Kotlin 1.0";
    }
}

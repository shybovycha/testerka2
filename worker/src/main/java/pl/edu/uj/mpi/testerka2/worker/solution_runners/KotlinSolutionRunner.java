package pl.edu.uj.mpi.testerka2.worker.solution_runners;

import pl.edu.uj.mpi.testerka2.core.checker.CompiledSolutionRunner;
import pl.edu.uj.mpi.testerka2.core.entities.Solution;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by shybovycha on 12/05/16.
 */
@Service
public class KotlinSolutionRunner extends CompiledSolutionRunner {
    public KotlinSolutionRunner() {}

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
    public String getAcceptedLanguage() {
        return "kotlin";
    }

    @Override
    public String getDescription() {
        return "Kotlin 1.0";
    }
}

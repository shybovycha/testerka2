package pl.edu.uj.mpi.testerka2.solution_runners;

import pl.edu.uj.mpi.testerka2.api.checker.CompiledSolutionRunner;
import pl.edu.uj.mpi.testerka2.api.checker.exceptions.SolutionRuntimeException;
import pl.edu.uj.mpi.testerka2.api.entities.Solution;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by shybovycha on 12/05/16.
 */
@Service
public class KotlinSolutionRunner extends CompiledSolutionRunner {
    public KotlinSolutionRunner() {}

    @Override
    protected ProcessBuilder getCompilerProcessBuilder(Solution solution) throws SolutionRuntimeException {
        writeSolutionToFile(solution);

        ProcessBuilder pb = new ProcessBuilder("kotlinc", "-include-runtime", getSourceFilename(), "-d", "main.jar");
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

    @Override
    protected String getSourceFilename() {
        return "Main.kt";
    }
}

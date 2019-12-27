package pl.edu.uj.mpi.testerka2.worker.solution_runners;

import pl.edu.uj.mpi.testerka2.core.entities.Solution;
import pl.edu.uj.mpi.testerka2.core.checker.CompiledSolutionRunner;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by shybovycha on 12/05/16.
 */
@Service
public class CsharpSolutionRunner extends CompiledSolutionRunner {
    public CsharpSolutionRunner() {}

    @Override
    protected ProcessBuilder getCompilerProcessBuilder(Solution solution) {
        String sourceFileName = "Main.cs";

        writeSolutionToFile(solution, sourceFileName);

        ProcessBuilder pb = new ProcessBuilder("mcs", "-o", "main", sourceFileName);
        pb.directory(new File(this.getSolutionDir(solution)));

        return pb;
    }

    @Override
    protected ProcessBuilder getRunProcessBuilder(Solution solution) {
        ProcessBuilder pb = new ProcessBuilder("mono", "main");
        pb.directory(new File(this.getSolutionDir(solution)));

        return pb;
    }

    @Override
    public String getAcceptedLanguage() {
        return "csharp";
    }

    @Override
    public String getDescription() {
        return "C# (Mono)";
    }
}

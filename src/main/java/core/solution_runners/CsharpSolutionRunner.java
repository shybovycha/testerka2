package core.solution_runners;

import core.entities.Solution;
import core.checker.CompiledSolutionRunner;
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
    protected String getAcceptedLanguage() {
        return "csharp";
    }

    @Override
    protected String getDescription() {
        return "C# (Mono)";
    }
}

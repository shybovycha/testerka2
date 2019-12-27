package pl.edu.uj.mpi.testerka2.worker.solution_runners;

import pl.edu.uj.mpi.testerka2.core.entities.Solution;
import pl.edu.uj.mpi.testerka2.core.checker.CompiledSolutionRunner;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by shybovycha on 12/05/16.
 */
@Service
public class JavaSolutionRunner extends CompiledSolutionRunner {
    public JavaSolutionRunner() {}

    @Override
    protected ProcessBuilder getCompilerProcessBuilder(Solution solution) {
        String sourceFileName = "Main.java";

        solution.setSource(solution.getSource().replaceFirst("\\bpackage\\s+\\w+;", ""));

        writeSolutionToFile(solution, sourceFileName);

        ProcessBuilder pb = new ProcessBuilder("javac", "-source", "1.8", "-target", "1.8", sourceFileName);
        pb.directory(new File(this.getSolutionDir(solution)));

        return pb;
    }

    @Override
    protected ProcessBuilder getRunProcessBuilder(Solution solution) {
        ProcessBuilder pb = new ProcessBuilder("java", "Main");
        pb.directory(new File(this.getSolutionDir(solution)));

        return pb;
    }

    @Override
    public String getAcceptedLanguage() {
        return "java";
    }

    @Override
    public String getDescription() {
        return "Java (Oracle)";
    }
}

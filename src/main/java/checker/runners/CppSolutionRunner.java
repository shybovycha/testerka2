package checker.runners;

import checker.entities.Solution;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.PrintWriter;

/**
 * Created by shybovycha on 12/05/16.
 */
@Service
public class CppSolutionRunner extends CompiledSolutionRunner {
    public CppSolutionRunner() {}

    @Override
    protected ProcessBuilder getCompilerProcessBuilder(Solution solution) {
        String sourceFileName = "main.cpp";
        String sourceFilePath = String.format("%s/%s", this.getSolutionDir(solution), sourceFileName);

        try {
            new File(this.getSolutionDir(solution)).mkdirs();

            File f = new File(sourceFilePath);
            PrintWriter writer = new PrintWriter(f);

            writer.write(solution.getSource());

            writer.close();
        } catch (Exception e) {
            // TODO: logger
        }

        ProcessBuilder pb = new ProcessBuilder("g++", "-o", "main", sourceFileName);
        pb.directory(new File(this.getSolutionDir(solution)));

        return pb;
    }

    @Override
    protected ProcessBuilder getRunProcessBuilder(Solution solution) {
        ProcessBuilder pb = new ProcessBuilder("./main");
        pb.directory(new File(this.getSolutionDir(solution)));

        return pb;
    }

    @Override
    protected String getAcceptedLanguage() {
        return "cpp";
    }
}

package checker.runners;

import checker.entities.Solution;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by shybovycha on 12/05/16.
 */
@Service
public class CppSolutionRunner extends CompiledSolutionRunner {
    public CppSolutionRunner() {
    }

    @Override
    protected ProcessBuilder getCompilerProcessBuilder(Solution solution) {
        String sourceFileName = "main.cpp";
        String sourceFilePath = String.format("%s/%s", this.getSolutionDir(solution), sourceFileName);

        writeSolutionToFile(solution, sourceFilePath);

        ProcessBuilder pb = new ProcessBuilder("g++", "-o", "main", sourceFileName, "-std=c++11");
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

    @Override
    protected String getDescription() {
        return "C++ (GCC)";
    }
}

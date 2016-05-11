package checker.runners;

import checker.entities.Solution;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.PrintWriter;

/**
 * Created by shybovycha on 12/05/16.
 */
@Service
public class JavaSolutionRunner extends CompiledSolutionRunner {
    public JavaSolutionRunner() {}

    @Override
    protected ProcessBuilder getCompilerProcessBuilder(Solution solution) {
        String sourceFileName = "Main.java";
        String sourceFilePath = String.format("%s/%s", this.getSolutionDir(solution), sourceFileName);

        try {
            new File(this.getSolutionDir(solution)).mkdirs();

            File f = new File(sourceFilePath);
            PrintWriter writer = new PrintWriter(f);

            writer.write(solution.getSource().replaceFirst("\\bpackage\\s+\\w+;", ""));

            writer.close();
        } catch (Exception e) {
            // TODO: logger
        }

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
    protected String getAcceptedLanguage() {
        return "java";
    }

    protected String getSolutionDir(Solution solution) {
        return String.format("solutions/%d", solution.getId());
    }
}

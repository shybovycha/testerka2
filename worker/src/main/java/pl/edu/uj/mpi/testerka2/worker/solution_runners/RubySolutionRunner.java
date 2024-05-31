package pl.edu.uj.mpi.testerka2.worker.solution_runners;

import pl.edu.uj.mpi.testerka2.core.checker.SolutionRunner;
import pl.edu.uj.mpi.testerka2.core.checker.exceptions.SolutionRuntimeException;
import pl.edu.uj.mpi.testerka2.core.entities.Solution;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by shybovycha on 12/05/16.
 */
@Service
public class RubySolutionRunner extends SolutionRunner {
    public RubySolutionRunner() {}

    @Override
    protected ProcessBuilder getRunProcessBuilder(Solution solution) throws SolutionRuntimeException {
        writeSolutionToFile(solution);

        ProcessBuilder pb = new ProcessBuilder("ruby", getSourceFilename());
        pb.directory(new File(this.getSolutionDir(solution)));

        return pb;
    }

    @Override
    public String getAcceptedLanguage() {
        return "ruby";
    }

    @Override
    public String getDescription() {
        return "Ruby";
    }

    @Override
    protected String getSourceFilename() {
        return "main.rb";
    }
}

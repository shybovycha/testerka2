package checker;

import checker.entities.Solution;

import java.io.IOException;

/**
 * Created by shybovycha on 10/05/16.
 */
public abstract class CompiledSolution extends RunnableSolution {
    protected String binaryFile;

    public CompiledSolution(Solution source) {
        super(source);
    }

    public CompiledSolution(String sourceFile) {
        super(sourceFile);

        this.binaryFile = null;
    }

    @Override
    public String getOutputFor(String input) {
        if (this.binaryFile == null)
            this.compile();

        return this.runBinary(input);
    }

    protected void compile() {
        try {
            ProcessBuilder pb = this.getCompileProcessBuilder();

            pb.start();
            pb.redirectErrorStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract ProcessBuilder getCompileProcessBuilder();
}
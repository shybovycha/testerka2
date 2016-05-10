package checker;

import java.io.IOException;

/**
 * Created by shybovycha on 10/05/16.
 */
public abstract class CompiledSolution extends RunnableSolution {
    protected String binaryFile;

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
        Process process = null;

        try {
            process = this.getRunProcessBuilder().start();

            this.binaryFile = String.format("solution_%d.bin", this.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract ProcessBuilder getCompileProcessBuilder();
}
package checker;

/**
 * Created by shybovycha on 10/05/16.
 */
class PythonSolution extends RunnableSolution {
    public PythonSolution(String sourceFile) {
        super(sourceFile);

        this.language = "python";
    }

    @Override
    protected ProcessBuilder getRunProcessBuilder() {
        // run python process, pass it input to stdin and return its stdout
        return null;
    }
}

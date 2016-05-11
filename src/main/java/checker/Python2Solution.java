package checker;

import checker.entities.Solution;

/**
 * Created by shybovycha on 10/05/16.
 */
class Python2Solution extends RunnableSolution {
    public Python2Solution(Solution source) {
        super(source);
    }

    public Python2Solution(String sourceFile) {
        super(sourceFile);
    }

    @Override
    public String getAcceptedLanguage() {
        return "python2";
    }

    @Override
    protected ProcessBuilder getRunProcessBuilder() {
        // run python process, pass it input to stdin and return its stdout
        return null;
    }
}

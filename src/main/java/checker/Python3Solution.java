package checker;

import checker.entities.Solution;

/**
 * Created by shybovycha on 10/05/16.
 */
class Python3Solution extends RunnableSolution {
    public Python3Solution(Solution source) {
        super(source);
    }

    public Python3Solution(String sourceFile) {
        super(sourceFile);
    }

    @Override
    public String getAcceptedLanguage() {
        return "python3";
    }

    @Override
    protected ProcessBuilder getRunProcessBuilder() {
        // run python process, pass it input to stdin and return its stdout
        return null;
    }
}

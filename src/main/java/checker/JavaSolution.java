package checker;

/**
 * Created by shybovycha on 10/05/16.
 */
class JavaSolution extends CompiledSolution {
    public JavaSolution(String source) {
        super(source);

        this.language = "java";
    }

    @Override
    protected ProcessBuilder getCompileProcessBuilder() {
        // run javac process and set this.binaryFile

        // 1. replace package name in source
        // 2. write source to file
        // 3. compile source
        // 4. store class name in binaryFile

        String sourceFile = String.format("solution_%s.src", this.getId());

        return new ProcessBuilder("javac", sourceFile);
    }

    @Override
    protected ProcessBuilder getRunProcessBuilder() {
        // run java process, pass it input to stdin and return its stdout
        return new ProcessBuilder("java", binaryFile);
    }
}

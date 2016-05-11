package checker;

import checker.entities.Solution;

import java.io.File;
import java.io.PrintWriter;

/**
 * Created by shybovycha on 10/05/16.
 */
class JavaSolution extends CompiledSolution {
    public JavaSolution(String source) {
        super(source);
    }

    public JavaSolution(Solution source) {
        super(source);
    }

    @Override
    public String getAcceptedLanguage() {
        return "java";
    }

    @Override
    protected ProcessBuilder getCompileProcessBuilder() {
        // run javac process and set this.binaryFile

        // 1. replace package name in source
        // 2. write source to file
        // 3. compile source
        // 4. store class name in binaryFile

        String sourceFileName = "Main.java";
        String sourceFilePath = String.format("%s/%s", this.getSolutionDir(), sourceFileName);

        try {
            new File(this.getSolutionDir()).mkdirs();

            File f = new File(sourceFilePath);
            PrintWriter writer = new PrintWriter(f);

            writer.write(this.getSource().replaceFirst("\\bpackage\\s+\\w+;", ""));

            writer.close();
        } catch (Exception e) {
            // TODO: logger
        }

        this.binaryFile = "Main";

        ProcessBuilder pb = new ProcessBuilder("javac", "-source", "1.8", "-target", "1.8", sourceFileName);
        pb.directory(new File(this.getSolutionDir()));

        return pb;
    }

    @Override
    protected ProcessBuilder getRunProcessBuilder() {
        String solutionDir = String.format("solutions/solution_%s", this.getId());

        ProcessBuilder pb = new ProcessBuilder("java", this.binaryFile);
        pb.directory(new File(solutionDir));

        return pb;
    }

    protected String getSolutionDir() {
        return String.format("solutions/solution_%s", this.getId());
    }
}

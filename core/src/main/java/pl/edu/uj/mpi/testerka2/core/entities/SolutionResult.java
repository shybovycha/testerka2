package pl.edu.uj.mpi.testerka2.core.entities;

import javax.persistence.*;

/**
 * Created by shybovycha on 10/05/16.
 */
@Entity
public class SolutionResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private TestCase testCase;

    @Lob
    private String output;

    private Integer points;

    @ManyToOne
    private Solution solution;

    @Enumerated(EnumType.STRING)
    protected SolutionResultStatus status;

    public SolutionResult() {}

    public SolutionResult(Solution solution, TestCase testCase) {
        this.solution = solution;
        this.testCase = testCase;
        this.status = SolutionResultStatus.PENDING;
    }

    public SolutionResult(Solution solution, TestCase testCase, SolutionResultStatus status) {
        this.solution = solution;
        this.testCase = testCase;
        this.status = status;
    }

    public TestCase getTestCase() {
        return testCase;
    }

    public void setTestCase(TestCase testCase) {
        this.testCase = testCase;
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    public SolutionResultStatus getStatus() {
        return status;
    }

    public void setStatus(SolutionResultStatus status) {
        this.status = status;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}

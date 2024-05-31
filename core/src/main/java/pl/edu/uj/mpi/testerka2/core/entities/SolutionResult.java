package pl.edu.uj.mpi.testerka2.core.entities;

import javax.persistence.*;

/**
 * Created by shybovycha on 10/05/16.
 */
@Entity
public class SolutionResult {
    public enum Status {
        PENDING, CHECKING, REJECTED, PASSED_CORRECT, PASSED_INCORRECT, RUN_ERROR, TIMEOUT
    }

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
    protected SolutionResult.Status status;

    public SolutionResult() {}

    public SolutionResult(Solution solution, TestCase testCase) {
        this.solution = solution;
        this.testCase = testCase;
        this.status = SolutionResult.Status.PENDING;
    }

    public SolutionResult(Solution solution, TestCase testCase, SolutionResult.Status status) {
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

    public SolutionResult.Status getStatus() {
        return status;
    }

    public void setStatus(SolutionResult.Status status) {
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

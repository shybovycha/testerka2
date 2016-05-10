package checker.entities;

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

    @OneToOne
    private Solution solution;

    private boolean passed;

    public SolutionResult(Solution solution, TestCase testCase, boolean passed) {
        this.solution = solution;
        this.testCase = testCase;
        this.passed = passed;
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

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}

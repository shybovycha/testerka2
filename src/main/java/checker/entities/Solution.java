package checker.entities;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by shybovycha on 10/05/16.
 */
@Entity
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    protected String source;
    protected String language;

    public enum SolutionStatus { PENDING, CHECKING, DONE };

    protected SolutionStatus status;

    public Solution() {
        this.status = SolutionStatus.PENDING;
        this.language = "UNKNOWN";
    }

    public Solution(String source) {
        this.source = source;
        this.status = SolutionStatus.PENDING;
        this.language = "UNKNOWN";
    }

    public String run(String input) {
        // TODO: timeout check
        String result = null;

        try {
            result = this.getOutputFor(input);
        } catch (Exception e) {
            // TODO: logger
        }

        return result;
    }

    public long getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public String getLanguage() {
        return language;
    }

    public SolutionStatus getStatus() {
        return status;
    }

    public void setStatus(SolutionStatus status) {
        this.status = status;
    }

    protected String getOutputFor(String input) throws Exception {
        throw new NotImplementedException();
    }
}

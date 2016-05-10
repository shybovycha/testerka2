package checker.entities;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.persistence.*;

/**
 * Created by shybovycha on 10/05/16.
 */
@Entity
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    protected String author;

    @Lob
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

    public void setSource(String source) {
        this.source = source;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public SolutionStatus getStatus() {
        return status;
    }

    public void setStatus(SolutionStatus status) {
        this.status = status;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    protected String getOutputFor(String input) throws Exception {
        throw new NotImplementedException();
    }
}

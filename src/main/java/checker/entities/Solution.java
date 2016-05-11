package checker.entities;

import javax.persistence.*;
import java.util.List;

/**
 * Created by shybovycha on 10/05/16.
 */
@Entity
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    protected String author;
    protected String moduleName;

    @Lob
    protected String source;

    protected String language;

    public enum SolutionStatus { PENDING, CHECKING, REJECTED, PASSED_CORRECT, PASSED_INCORRECT };

    protected SolutionStatus status;

    @OneToMany
    protected List<SolutionResult> results;

    public Solution() {
        this.status = SolutionStatus.PENDING;
        this.language = "UNKNOWN";
    }

    public Solution(String source) {
        this.source = source;
        this.status = SolutionStatus.PENDING;
        this.language = this.getAcceptedLanguage();
    }

    public Solution(Solution source) {
        this.id = source.id;
        this.author = source.author;
        this.language = this.getAcceptedLanguage();
        this.moduleName = source.moduleName;
        this.source = source.source;
        this.status = source.status;
    }

    public String getAcceptedLanguage() {
        return "UNKNOWN";
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

    public String getStatusString() {
        if (status == SolutionStatus.PENDING)
            return "Waiting for check";

        if (status == SolutionStatus.CHECKING)
            return "Checking...";

        if (status == SolutionStatus.REJECTED)
            return "Rejected (due to internal system error)";

        if (status == SolutionStatus.PASSED_CORRECT)
            return "Correct";

        if (status == SolutionStatus.PASSED_INCORRECT)
            return "Incorrect";

        return "Unknown O_o";
    }
}

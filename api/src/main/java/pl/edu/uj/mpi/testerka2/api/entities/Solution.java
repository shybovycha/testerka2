package pl.edu.uj.mpi.testerka2.api.entities;

import jakarta.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by shybovycha on 10/05/16.
 */
@Entity
public class Solution {
    public enum Status {
        PENDING, CHECKING, REJECTED, PASSED_CORRECT, PASSED_INCORRECT, RUN_ERROR, TIMEOUT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    protected String author;
    protected String moduleName;

    @Lob
    protected String source;

    @Lob
    protected String errorMessage;

    protected String language;

    @Transient
    protected int points;

    @Enumerated(EnumType.STRING)
    protected Solution.Status status;

    @OneToMany(targetEntity = SolutionResult.class, mappedBy = "solution", fetch = FetchType.EAGER)
    protected List<SolutionResult> results;

    @Column(updatable = false)
    private Date createdAt;

    public Solution() {
        this.status = Solution.Status.PENDING;
        this.language = "UNKNOWN";
    }

    public Solution(String source) {
        this.source = source;
        this.status = Solution.Status.PENDING;
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

    @PrePersist
    private void setCreatedAt() {
        this.createdAt = new Date();
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

    public Solution.Status getStatus() {
        return status;
    }

    public void setStatus(Solution.Status status) {
        this.status = status;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStatusString() {
        return switch (status) {
            case PENDING -> "Waiting for check";
            case CHECKING -> "Checking...";
            case REJECTED -> "Rejected (due to internal system error)";
            case PASSED_CORRECT -> "Correct";
            case PASSED_INCORRECT -> "Incorrect";
            case RUN_ERROR -> "Errors while running occurred";
            default -> "Unknown O_o";
        };

    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<SolutionResult> getResults() {
        return results;
    }

    public void setResults(List<SolutionResult> results) {
        this.results = results;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getCreatedAtStr() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return fmt.format(createdAt);
    }

    public void setPoints(int value) {
        this.points = value;
    }

    public Integer getPoints() {
        // return this.getResults().stream().map(SolutionResult::getPoints).collect(Collectors.summingInt(Integer::intValue));
        return this.points;
    }
}

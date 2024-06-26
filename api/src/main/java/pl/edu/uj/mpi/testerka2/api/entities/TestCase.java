package pl.edu.uj.mpi.testerka2.api.entities;

import jakarta.persistence.*;

/**
 * Created by shybovycha on 10/05/16.
 */
@Entity
public class TestCase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Lob
    private String input;

    public TestCase() {}

    public TestCase(String input) {
        this.input = input;
    }

    public String getInput() {
        return this.input;
    }

    public long getId() {
        return id;
    }
}

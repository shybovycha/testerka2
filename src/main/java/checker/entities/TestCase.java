package checker.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by shybovycha on 10/05/16.
 */
@Entity
public class TestCase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String input;

    public TestCase() {}

    public TestCase(String input) {
        this.input = input;
    }

    public String getInput() {
        return this.input;
    }
}

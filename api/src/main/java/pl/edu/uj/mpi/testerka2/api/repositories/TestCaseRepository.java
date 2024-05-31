package pl.edu.uj.mpi.testerka2.api.repositories;

import pl.edu.uj.mpi.testerka2.api.entities.TestCase;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by shybovycha on 10/05/16.
 */
public interface TestCaseRepository extends CrudRepository<TestCase, Long> {
}

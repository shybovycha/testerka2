package pl.edu.uj.mpi.testerka2.core.repositories;

import pl.edu.uj.mpi.testerka2.core.entities.TestCase;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by shybovycha on 10/05/16.
 */
public interface TestCaseRepository extends CrudRepository<TestCase, Long> {
}

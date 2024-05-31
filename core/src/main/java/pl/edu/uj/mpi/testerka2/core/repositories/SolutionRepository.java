package pl.edu.uj.mpi.testerka2.core.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.edu.uj.mpi.testerka2.core.entities.Solution;

import java.util.List;

/**
 * Created by shybovycha on 10/05/16.
 */
public interface SolutionRepository extends CrudRepository<Solution, Long> {
    List<Solution> findByStatus(Solution.Status status);
}

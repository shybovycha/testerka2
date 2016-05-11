package checker;

import checker.entities.Solution;
import org.springframework.stereotype.Component;

/**
 * Created by shybovycha on 11/05/16.
 */
@Component
public class SolutionFactory {
    public SolutionFactory() {}

    public RunnableSolution forLanguage(Solution solution) {
        if (solution.getLanguage().equals("java"))
            return new JavaSolution(solution); else
        if (solution.getLanguage().equals("python2"))
            return new Python2Solution(solution); else
        if (solution.getLanguage().equals("python3"))
            return new Python3Solution(solution);

        return null;
    }
}

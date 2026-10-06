package geosmart.route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DestroyOperator {

    public Solution destroy(Solution solution, int numberToRemove) {

        Solution destroyedSolution = solution.copy();

        List<Assignment> assignments =
                new ArrayList<>(destroyedSolution.getAssignments());

        Collections.shuffle(assignments);

        int removeCount =
                Math.min(numberToRemove, assignments.size());

        for (int i = 0; i < removeCount; i++) {

            destroyedSolution.removeAssignment(
                    assignments.get(i)
            );
        }

        return destroyedSolution;
    }
}
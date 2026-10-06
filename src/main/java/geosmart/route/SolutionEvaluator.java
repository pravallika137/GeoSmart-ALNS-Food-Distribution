package geosmart.route;

import java.util.List;

public class SolutionEvaluator {

    public double calculateTotalDistance(
            List<Assignment> assignments) {

        double totalDistance = 0;

        for (Assignment assignment : assignments) {
            totalDistance += assignment.getTotalDistance();
        }

        return totalDistance;
    }

    public double calculateTotalDuration(
            List<Assignment> assignments) {

        double totalDuration = 0;

        for (Assignment assignment : assignments) {
            totalDuration += assignment.getTotalDuration();
        }

        return totalDuration;
    }

    public void printSolutionCost(
            List<Assignment> assignments) {

        double totalDistance =
                calculateTotalDistance(assignments);

        double totalDuration =
                calculateTotalDuration(assignments);

        System.out.println();
        System.out.println("========== SOLUTION COST ==========");

        System.out.println(
                "Total assignments: "
                        + assignments.size()
        );

        System.out.println(
                "Total distance: "
                        + totalDistance
                        + " meters"
        );

        System.out.println(
                "Total duration: "
                        + totalDuration
                        + " seconds"
        );

        System.out.println(
                "Total distance: "
                        + (totalDistance / 1000.0)
                        + " km"
        );

        System.out.println(
                "Total duration: "
                        + (totalDuration / 3600.0)
                        + " hours"
        );

        System.out.println(
                "==================================="
        );
    }
}
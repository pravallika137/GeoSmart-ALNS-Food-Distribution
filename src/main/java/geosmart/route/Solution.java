package geosmart.route;

import java.util.ArrayList;
import java.util.List;

public class Solution {

    private List<Assignment> assignments;

    public Solution() {
        assignments = new ArrayList<>();
    }

    // Add an assignment
    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }

    // Remove an assignment
    public void removeAssignment(Assignment assignment) {
        assignments.remove(assignment);
    }

    // Get all assignments
    public List<Assignment> getAssignments() {
        return assignments;
    }

    // Set assignments
    public void setAssignments(
            List<Assignment> assignments) {

        this.assignments =
                new ArrayList<>(assignments);
    }

    // Calculate total distance
    public double getTotalDistance() {

        double totalDistance = 0;

        for (Assignment assignment : assignments) {

            totalDistance +=
                    assignment.getTotalDistance();
        }

        return totalDistance;
    }

    // Calculate total duration
    public double getTotalDuration() {

        double totalDuration = 0;

        for (Assignment assignment : assignments) {

            totalDuration +=
                    assignment.getTotalDuration();
        }

        return totalDuration;
    }

    // Get number of assignments
    public int getAssignmentCount() {

        return assignments.size();
    }

    // Create a copy of this solution
    public Solution copy() {

        Solution copiedSolution =
                new Solution();

        copiedSolution.setAssignments(
                this.assignments
        );

        return copiedSolution;
    }

    // Display solution
    public void printSolution() {

        System.out.println();

        System.out.println(
                "========== SOLUTION =========="
        );

        for (Assignment assignment :
                assignments) {

            System.out.println(
                    assignment
            );
        }

        System.out.println(
                "=============================="
        );

        System.out.println(
                "Total assignments: "
                        + getAssignmentCount()
        );

        System.out.println(
                "Total distance: "
                        + getTotalDistance()
                        + " meters"
        );

        System.out.println(
                "Total duration: "
                        + getTotalDuration()
                        + " seconds"
        );
    }
}
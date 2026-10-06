package geosmart.route;

import java.util.List;

public class ALNSTest {

    public static void main(String[] args) {

        // Load donors

        List<Donor> donors =
                DonorLoader.loadDonors(
                        "data/donors.csv"
                );

        // Load recipients

        List<Recipient> recipients =
                RecipientLoader.loadRecipients(
                        "data/recipients.csv"
                );

        // Load delivery agents

        List<DeliveryAgent> agents =
                DeliveryAgentLoader.loadAgents(
                        "data/delivery_agents.csv"
                );

        // Load travel matrix

        TravelMatrix travelMatrix =
                new TravelMatrix(
                        "data/travel_matrix.csv"
                );

        // Build initial solution

        InitialSolution initialSolution =
                new InitialSolution();

        initialSolution.build(
                donors,
                recipients,
                agents,
                travelMatrix
        );

        // Convert initial solution into Solution

        Solution solution =
                new Solution();

        for (Assignment assignment :
                initialSolution.getAssignments()) {

            solution.addAssignment(
                    assignment
            );
        }

        // Print initial solution

        System.out.println();

        System.out.println(
                "========== INITIAL SOLUTION =========="
        );

        solution.printSolution();

        // Create ALNS

        ALNS alns =
                new ALNS(
                        travelMatrix
                );

        // Run ALNS

        Solution bestSolution =
                alns.optimize(
                        solution,
                        10,
                        3,
                        agents,
                        recipients
                );

        // Print best solution

        System.out.println();

        System.out.println(
                "========== BEST SOLUTION =========="
        );

        bestSolution.printSolution();

        // Calculate final objective cost

        ObjectiveFunction objectiveFunction =
                new ObjectiveFunction();

        double finalCost =
                objectiveFunction.calculateCost(
                        bestSolution,
                        recipients
                );

        System.out.println();

        System.out.println(
                "========== FINAL OBJECTIVE COST =========="
        );

        System.out.println(
                "Objective cost: "
                        + finalCost
        );

        System.out.println(
                "=========================================="
        );

        // Performance Analysis

        PerformanceAnalyzer analyzer =
                new PerformanceAnalyzer();

        analyzer.analyze(
                solution,
                bestSolution,
                recipients
        );

        // Basic Solution Validation

        SolutionValidator validator =
                new SolutionValidator(
                        travelMatrix
                );

        boolean valid =
                validator.validate(
                        bestSolution,
                        recipients,
                        agents
                );

        // Chronological Route Validation

        ChronologicalRouteValidator
                chronologicalValidator =
                new ChronologicalRouteValidator(
                        travelMatrix
                );

        boolean chronologicalValid =
                chronologicalValidator.validate(
                        bestSolution,
                        recipients,
                        agents
                );

        // Final Project Status

        System.out.println();

        System.out.println(
                "========== FINAL PROJECT STATUS =========="
        );

        if (valid && chronologicalValid) {

            System.out.println(
                    "Optimized solution is VALID."
            );

            System.out.println(
                    "All checked constraints are satisfied."
            );

        } else {

            System.out.println(
                    "Optimized solution requires further validation."
            );

        }

        System.out.println(
                "=========================================="
        );
    }
}
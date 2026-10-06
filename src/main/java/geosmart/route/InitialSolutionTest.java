package geosmart.route;

import java.util.List;

public class InitialSolutionTest {

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

        // Create initial solution
        InitialSolution solution =
                new InitialSolution();

        // Build initial solution
        solution.build(
                donors,
                recipients,
                agents,
                travelMatrix
        );

        // Display initial solution
        System.out.println();
        System.out.println(
                "========== INITIAL SOLUTION =========="
        );

        for (Assignment assignment :
                solution.getAssignments()) {

            System.out.println(
                    assignment
            );
        }

        System.out.println(
                "======================================"
        );

        System.out.println(
                "Total assignments: "
                        + solution
                        .getAssignments()
                        .size()
        );

        // ==========================================
        // EVALUATE INITIAL SOLUTION
        // ==========================================

        SolutionEvaluator solutionEvaluator =
                new SolutionEvaluator();

        solutionEvaluator.printSolutionCost(
                solution.getAssignments()
        );
    }
}
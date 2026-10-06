package geosmart.route;

import java.util.List;

public class ObjectiveFunctionTest {

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

        // Convert initial solution into Solution object

        Solution solution =
                new Solution();

        for (Assignment assignment :
                initialSolution.getAssignments()) {

            solution.addAssignment(
                    assignment
            );
        }

        // Create objective function

        ObjectiveFunction objectiveFunction =
                new ObjectiveFunction();

        // Calculate total cost

        double cost =
                objectiveFunction.calculateCost(
                        solution,
                        recipients
                );

        System.out.println();

        System.out.println(
                "========== OBJECTIVE FUNCTION =========="
        );

        System.out.println(
                "Total distance: "
                        + solution.getTotalDistance()
                        + " meters"
        );

        System.out.println(
                "Objective cost: "
                        + cost
        );

        System.out.println(
                "========================================"
        );
    }
}
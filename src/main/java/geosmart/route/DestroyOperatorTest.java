package geosmart.route;

import java.util.List;

public class DestroyOperatorTest {

    public static void main(String[] args) {

        List<Donor> donors =
                DonorLoader.loadDonors("data/donors.csv");

        List<Recipient> recipients =
                RecipientLoader.loadRecipients("data/recipients.csv");

        List<DeliveryAgent> agents =
                DeliveryAgentLoader.loadAgents(
                        "data/delivery_agents.csv"
                );

        TravelMatrix travelMatrix =
                new TravelMatrix("data/travel_matrix.csv");

        InitialSolution initialSolution =
                new InitialSolution();

        initialSolution.build(
                donors,
                recipients,
                agents,
                travelMatrix
        );

        Solution solution =
                new Solution();

        for (Assignment assignment :
                initialSolution.getAssignments()) {

            solution.addAssignment(assignment);
        }

        System.out.println();
        System.out.println(
                "========== ORIGINAL SOLUTION =========="
        );

        solution.printSolution();

        DestroyOperator destroyOperator =
                new DestroyOperator();

        Solution destroyedSolution =
                destroyOperator.destroy(
                        solution,
                        3
                );

        System.out.println();
        System.out.println(
                "========== DESTROYED SOLUTION =========="
        );

        destroyedSolution.printSolution();
    }
}
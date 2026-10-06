package geosmart.route;

import java.util.List;

public class RepairOperatorTest {

    public static void main(String[] args) {

        // Load data

        List<Donor> donors =
                DonorLoader.loadDonors(
                        "data/donors.csv"
                );

        List<Recipient> recipients =
                RecipientLoader.loadRecipients(
                        "data/recipients.csv"
                );

        List<DeliveryAgent> agents =
                DeliveryAgentLoader.loadAgents(
                        "data/delivery_agents.csv"
                );

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

        Solution originalSolution =
                new Solution();

        for (Assignment assignment :
                initialSolution.getAssignments()) {

            originalSolution.addAssignment(
                    assignment
            );
        }

        // Print original solution

        System.out.println();

        System.out.println(
                "========== ORIGINAL SOLUTION =========="
        );

        originalSolution.printSolution();

        // Destroy

        DestroyOperator destroyOperator =
                new DestroyOperator();

        Solution destroyedSolution =
                destroyOperator.destroy(
                        originalSolution,
                        3
                );

        // Print destroyed solution

        System.out.println();

        System.out.println(
                "========== DESTROYED SOLUTION =========="
        );

        destroyedSolution.printSolution();

        // Repair

        RepairOperator repairOperator =
                new RepairOperator(
                        travelMatrix
                );

        Solution repairedSolution =
                repairOperator.repair(
                        originalSolution,
                        destroyedSolution,
                        agents
                );

        // Print repaired solution

        System.out.println();

        System.out.println(
                "========== REPAIRED SOLUTION =========="
        );

        repairedSolution.printSolution();
    }
}
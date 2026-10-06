package geosmart.route;

import java.util.List;

public class AssignmentTest {

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

        // Load travel matrix

        TravelMatrix matrix =
                new TravelMatrix(
                        "data/travel_matrix.csv"
                );

        // Select one donor

        Donor donor = donors.get(0);

        // Select one recipient

        Recipient recipient =
                recipients.get(0);

        // Select one agent

        DeliveryAgent agent =
                agents.get(0);

        // Create assignment

        Assignment assignment =
                new Assignment(
                        donor,
                        recipient,
                        agent,
                        20
                );

        // Evaluate assignment

        AssignmentEvaluator evaluator =
                new AssignmentEvaluator(matrix);

        evaluator.evaluate(assignment);

        // Display result

        System.out.println(
                "Assignment:"
        );

        System.out.println(
                assignment
        );
    }
}
package geosmart.route;

import java.util.List;

public class FeasibilityTest {

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


        // Load agents

        List<DeliveryAgent> agents =
                DeliveryAgentLoader.loadAgents(
                        "data/delivery_agents.csv"
                );


        // Load travel matrix

        TravelMatrix matrix =
                new TravelMatrix(
                        "data/travel_matrix.csv"
                );


        // Select data

        Donor donor =
                donors.get(0);

        Recipient recipient =
                recipients.get(0);

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


        // Check feasibility

        FeasibilityChecker checker =
                new FeasibilityChecker(matrix);


        boolean feasible =
                checker.isFeasible(
                        assignment
                );


        System.out.println();

        System.out.println(
                "Assignment: "
                        + donor.getDonorId()
                        + " → "
                        + agent.getAgentId()
                        + " → "
                        + recipient.getRecipientId()
        );


        System.out.println(
                "Feasible: "
                        + feasible
        );
    }
}
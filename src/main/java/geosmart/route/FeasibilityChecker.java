package geosmart.route;

public class FeasibilityChecker {

    private TravelMatrix travelMatrix;

    public FeasibilityChecker(TravelMatrix travelMatrix) {
        this.travelMatrix = travelMatrix;
    }

    public boolean isFeasible(Assignment assignment) {

        Donor donor = assignment.getDonor();
        Recipient recipient = assignment.getRecipient();
        DeliveryAgent agent = assignment.getAgent();

        int quantity = assignment.getQuantity();

        // -------------------------------------------------
        // 1. Check donor quantity
        // -------------------------------------------------

        if (quantity > donor.getAvailableQuantity()) {

            System.out.println(
                    "Not feasible: Donor does not have enough food."
            );

            return false;
        }

        // -------------------------------------------------
        // 2. Check vehicle capacity
        // -------------------------------------------------

        if (quantity > agent.getCapacity()) {

            System.out.println(
                    "Not feasible: Vehicle capacity is insufficient."
            );

            return false;
        }

        // -------------------------------------------------
        // 3. Get current agent location
        // -------------------------------------------------

        String agentLocation =
                agent.getCurrentLocationId();

        String donorLocation =
                donor.getLocationId();

        String recipientLocation =
                recipient.getLocationId();

        // -------------------------------------------------
        // 4. Get travel duration:
        // Agent current location -> Donor
        // -------------------------------------------------

        double pickupDuration =
                travelMatrix.getDuration(
                        agentLocation,
                        donorLocation
                );

        // -------------------------------------------------
        // 5. Get travel duration:
        // Donor -> Recipient
        // -------------------------------------------------

        double deliveryDuration =
                travelMatrix.getDuration(
                        donorLocation,
                        recipientLocation
                );

        // -------------------------------------------------
        // 6. Agent's current time
        // -------------------------------------------------

        double agentCurrentTime =
                convertTimeToSeconds(
                        agent.getCurrentTime()
                );

        // -------------------------------------------------
        // 7. Agent availability end time
        // -------------------------------------------------

        double agentAvailableUntil =
                convertTimeToSeconds(
                        agent.getAvailableUntil()
                );

        // -------------------------------------------------
        // 8. Donor pickup-ready time
        // -------------------------------------------------

        double pickupReadyTime =
                convertTimeToSeconds(
                        donor.getPickupReadyTime()
                );

        // -------------------------------------------------
        // 9. Agent reaches donor
        // -------------------------------------------------

        double agentArrivalAtDonor =
                agentCurrentTime
                        + pickupDuration;

        // -------------------------------------------------
        // 10. If agent reaches donor before food is ready,
        //     agent waits until pickup-ready time.
        // -------------------------------------------------

        double actualPickupTime =
                Math.max(
                        agentArrivalAtDonor,
                        pickupReadyTime
                );

        // -------------------------------------------------
        // 11. Agent must reach donor before expiry.
        // -------------------------------------------------

        double expiryTime =
                convertTimeToSeconds(
                        donor.getExpiryTime()
                );

        if (actualPickupTime > expiryTime) {

            System.out.println(
                    "Not feasible: Food is already expired "
                            + "before pickup."
            );

            return false;
        }

        // -------------------------------------------------
        // 12. Calculate delivery arrival time
        // -------------------------------------------------

        double deliveryArrivalTime =
                actualPickupTime
                        + deliveryDuration;

        // -------------------------------------------------
        // 13. Check food expiry
        // -------------------------------------------------

        if (deliveryArrivalTime > expiryTime) {

            System.out.println(
                    "Not feasible: Food expires before delivery."
            );

            return false;
        }

        // -------------------------------------------------
        // 14. Check recipient deadline
        // -------------------------------------------------

        double recipientDeadline =
                convertTimeToSeconds(
                        recipient.getLatestDeliveryTime()
                );

        if (deliveryArrivalTime > recipientDeadline) {

            System.out.println(
                    "Not feasible: Recipient deadline missed."
            );

            return false;
        }

        // -------------------------------------------------
        // 15. Check agent availability end time
        // -------------------------------------------------

        if (deliveryArrivalTime > agentAvailableUntil) {

            System.out.println(
                    "Not feasible: Agent availability ended."
            );

            return false;
        }

        // -------------------------------------------------
        // Everything passed
        // -------------------------------------------------

        return true;
    }

    // -----------------------------------------------------
    // Convert HH:mm or HH:mm:ss into seconds
    // -----------------------------------------------------

    private double convertTimeToSeconds(String time) {

        String[] parts = time.split(":");

        int hours =
                Integer.parseInt(parts[0]);

        int minutes =
                Integer.parseInt(parts[1]);

        int seconds = 0;

        if (parts.length >= 3) {

            seconds =
                    Integer.parseInt(parts[2]);
        }

        return hours * 3600
                + minutes * 60
                + seconds;
    }
}
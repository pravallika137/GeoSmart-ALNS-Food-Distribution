package geosmart.route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InitialSolution {

    private List<Assignment> assignments;

    public InitialSolution() {

        assignments = new ArrayList<>();
    }

    public void build(
            List<Donor> donors,
            List<Recipient> recipients,
            List<DeliveryAgent> agents,
            TravelMatrix travelMatrix) {

        FeasibilityChecker checker =
                new FeasibilityChecker(travelMatrix);

        AssignmentEvaluator evaluator =
                new AssignmentEvaluator(travelMatrix);

        // -------------------------------------------------
        // Store remaining donor quantity
        // -------------------------------------------------

        Map<String, Integer> remainingDonorQuantity =
                new HashMap<>();

        for (Donor donor : donors) {

            remainingDonorQuantity.put(
                    donor.getDonorId(),
                    donor.getAvailableQuantity()
            );
        }

        // -------------------------------------------------
        // Store remaining recipient demand
        // -------------------------------------------------

        Map<String, Integer> remainingRecipientDemand =
                new HashMap<>();

        for (Recipient recipient : recipients) {

            remainingRecipientDemand.put(
                    recipient.getRecipientId(),
                    recipient.getRequiredQuantity()
            );
        }

        // -------------------------------------------------
        // Process recipients one by one
        // -------------------------------------------------

        for (Recipient recipient : recipients) {

            String recipientId =
                    recipient.getRecipientId();

            int remainingDemand =
                    remainingRecipientDemand.get(
                            recipientId
                    );

            // -------------------------------------------------
            // Continue until recipient demand is satisfied
            // -------------------------------------------------

            while (remainingDemand > 0) {

                boolean assigned = false;

                // -------------------------------------------------
                // Try every donor
                // -------------------------------------------------

                for (Donor donor : donors) {

                    String donorId =
                            donor.getDonorId();

                    int donorRemaining =
                            remainingDonorQuantity.get(
                                    donorId
                            );

                    // No food left
                    if (donorRemaining <= 0) {
                        continue;
                    }

                    // Quantity required from this donor
                    int quantity =
                            Math.min(
                                    donorRemaining,
                                    remainingDemand
                            );

                    // -------------------------------------------------
                    // Try every delivery agent
                    // -------------------------------------------------

                    for (DeliveryAgent agent : agents) {

                        // Agent must currently be available
                        if (!agent.isAvailable()) {
                            continue;
                        }

                        // Vehicle must have enough capacity
                        if (agent.getCapacity() < quantity) {
                            continue;
                        }

                        Assignment assignment =
                                new Assignment(
                                        donor,
                                        recipient,
                                        agent,
                                        quantity
                                );

                        // -------------------------------------------------
                        // Check feasibility
                        // -------------------------------------------------

                        boolean feasible =
                                checker.isFeasible(
                                        assignment
                                );

                        if (!feasible) {
                            continue;
                        }

                        // -------------------------------------------------
                        // Evaluate assignment
                        // -------------------------------------------------

                        evaluator.evaluate(
                                assignment
                        );

                        assignments.add(
                                assignment
                        );

                        // -------------------------------------------------
                        // Reduce donor food
                        // -------------------------------------------------

                        donorRemaining =
                                donorRemaining - quantity;

                        remainingDonorQuantity.put(
                                donorId,
                                donorRemaining
                        );

                        // -------------------------------------------------
                        // Reduce recipient demand
                        // -------------------------------------------------

                        remainingDemand =
                                remainingDemand - quantity;

                        remainingRecipientDemand.put(
                                recipientId,
                                remainingDemand
                        );

                        // -------------------------------------------------
                        // Agent starts delivery
                        // -------------------------------------------------

                        agent.setStatus(
                                AgentStatus.ASSIGNED
                        );

                        System.out.println();

                        System.out.println(
                                "Assigned "
                                        + quantity
                                        + " meals: "
                                        + donorId
                                        + " → "
                                        + agent.getAgentId()
                                        + " → "
                                        + recipientId
                        );

                        System.out.println(
                                "Agent starting location: "
                                        + agent.getCurrentLocationId()
                        );

                        System.out.println(
                                "Agent starting time: "
                                        + agent.getCurrentTime()
                        );

                        // -------------------------------------------------
                        // Simulate completion of delivery
                        // -------------------------------------------------

                        completeDelivery(
                                assignment,
                                agent,
                                travelMatrix
                        );

                        assigned = true;

                        break;
                    }

                    if (assigned) {
                        break;
                    }
                }

                // -------------------------------------------------
                // No feasible assignment
                // -------------------------------------------------

                if (!assigned) {

                    System.out.println();

                    System.out.println(
                            "Could not satisfy remaining demand "
                                    + "for recipient "
                                    + recipientId
                                    + ": "
                                    + remainingDemand
                                    + " meals"
                    );

                    break;
                }
            }
        }
    }

    // =========================================================
    // Complete the delivery and make the agent available again
    // =========================================================

    private void completeDelivery(
            Assignment assignment,
            DeliveryAgent agent,
            TravelMatrix travelMatrix) {

        Donor donor =
                assignment.getDonor();

        Recipient recipient =
                assignment.getRecipient();

        // -------------------------------------------------
        // Agent -> Donor
        // -------------------------------------------------

        double pickupDuration =
                travelMatrix.getDuration(
                        agent.getCurrentLocationId(),
                        donor.getLocationId()
                );

        double currentTime =
                convertTimeToSeconds(
                        agent.getCurrentTime()
                );

        double pickupArrivalTime =
                currentTime
                        + pickupDuration;

        // -------------------------------------------------
        // Food pickup ready time
        // -------------------------------------------------

        double pickupReadyTime =
                convertTimeToSeconds(
                        donor.getPickupReadyTime()
                );

        // Agent may have to wait
        double actualPickupTime =
                Math.max(
                        pickupArrivalTime,
                        pickupReadyTime
                );

        // -------------------------------------------------
        // Donor -> Recipient
        // -------------------------------------------------

        double deliveryDuration =
                travelMatrix.getDuration(
                        donor.getLocationId(),
                        recipient.getLocationId()
                );

        double deliveryArrivalTime =
                actualPickupTime
                        + deliveryDuration;

        // -------------------------------------------------
        // Update agent status
        // -------------------------------------------------

        agent.setStatus(
                AgentStatus.IN_TRANSIT
        );

        // -------------------------------------------------
        // Delivery completed
        // -------------------------------------------------

        agent.setStatus(
                AgentStatus.DELIVERED
        );

        // Agent is now physically at recipient
        agent.setCurrentLocationId(
                recipient.getLocationId()
        );

        // Agent becomes available at delivery time
        agent.setCurrentTime(
                convertSecondsToTime(
                        deliveryArrivalTime
                )
        );

        // Agent can now receive another assignment
        agent.setStatus(
                AgentStatus.AVAILABLE
        );

        System.out.println(
                "Delivered to "
                        + recipient.getRecipientId()
        );

        System.out.println(
                "Agent "
                        + agent.getAgentId()
                        + " is now AVAILABLE"
        );

        System.out.println(
                "New location: "
                        + agent.getCurrentLocationId()
        );

        System.out.println(
                "Available again at: "
                        + agent.getCurrentTime()
        );
    }

    // =========================================================
    // Convert HH:mm or HH:mm:ss into seconds
    // =========================================================

    private double convertTimeToSeconds(
            String time) {

        String[] parts =
                time.split(":");

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

    // =========================================================
    // Convert seconds into HH:mm:ss
    // =========================================================

    private String convertSecondsToTime(
            double totalSeconds) {

        int seconds =
                (int) Math.ceil(totalSeconds);

        int hours =
                seconds / 3600;

        int remaining =
                seconds % 3600;

        int minutes =
                remaining / 60;

        int finalSeconds =
                remaining % 60;

        return String.format(
                "%02d:%02d:%02d",
                hours,
                minutes,
                finalSeconds
        );
    }

    public List<Assignment> getAssignments() {

        return assignments;
    }
}
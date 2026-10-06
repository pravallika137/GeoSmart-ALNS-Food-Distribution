package geosmart.route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepairOperator {

    private TravelMatrix travelMatrix;

    public RepairOperator(
            TravelMatrix travelMatrix) {

        this.travelMatrix =
                travelMatrix;
    }

    public Solution repair(
            Solution originalSolution,
            Solution destroyedSolution,
            List<DeliveryAgent> agents) {

        Solution repairedSolution =
                destroyedSolution.copy();

        // Find assignments removed during destruction

        List<Assignment> removedAssignments =
                new ArrayList<>();

        for (Assignment assignment :
                originalSolution.getAssignments()) {

            if (!destroyedSolution
                    .getAssignments()
                    .contains(assignment)) {

                removedAssignments.add(
                        assignment
                );
            }
        }

        // Store temporary state for every agent

        Map<String, String> agentLocations =
                new HashMap<>();

        Map<String, String> agentTimes =
                new HashMap<>();

        for (DeliveryAgent agent :
                agents) {

            agentLocations.put(
                    agent.getAgentId(),
                    agent.getCurrentLocationId()
            );

            agentTimes.put(
                    agent.getAgentId(),
                    agent.getCurrentTime()
            );
        }

        // Repair one assignment at a time

        for (Assignment removedAssignment :
                removedAssignments) {

            Assignment bestAssignment =
                    null;

            double bestDistance =
                    Double.MAX_VALUE;

            String bestAgentId =
                    null;

            String bestNewLocation =
                    null;

            String bestNewTime =
                    null;

            // Try every agent

            for (DeliveryAgent agent :
                    agents) {

                String agentId =
                        agent.getAgentId();

                String agentLocation =
                        agentLocations.get(
                                agentId
                        );

                String agentTime =
                        agentTimes.get(
                                agentId
                        );

                Assignment candidate =
                        new Assignment(
                                removedAssignment.getDonor(),
                                removedAssignment.getRecipient(),
                                agent,
                                removedAssignment.getQuantity()
                        );

                String donorLocation =
                        removedAssignment
                                .getDonor()
                                .getLocationId();

                String recipientLocation =
                        removedAssignment
                                .getRecipient()
                                .getLocationId();

                // Calculate travel distance

                double pickupDistance =
                        travelMatrix.getDistance(
                                agentLocation,
                                donorLocation
                        );

                double deliveryDistance =
                        travelMatrix.getDistance(
                                donorLocation,
                                recipientLocation
                        );

                // Calculate travel duration

                double pickupDuration =
                        travelMatrix.getDuration(
                                agentLocation,
                                donorLocation
                        );

                double deliveryDuration =
                        travelMatrix.getDuration(
                                donorLocation,
                                recipientLocation
                        );

                candidate.setPickupDistance(
                        pickupDistance
                );

                candidate.setDeliveryDistance(
                        deliveryDistance
                );

                candidate.setPickupDuration(
                        pickupDuration
                );

                candidate.setDeliveryDuration(
                        deliveryDuration
                );

                candidate.calculateTotals();

                /*
                 * Calculate when the agent reaches
                 * the donor.
                 */

                double agentStartTime =
                        convertTimeToSeconds(
                                agentTime
                        );

                double pickupReadyTime =
                        convertTimeToSeconds(
                                removedAssignment
                                        .getDonor()
                                        .getPickupReadyTime()
                        );

                double arrivalAtDonor =
                        agentStartTime
                                + pickupDuration;

                /*
                 * Agent may have to wait if it reaches
                 * the donor before the food is ready.
                 */

                double actualPickupTime =
                        Math.max(
                                arrivalAtDonor,
                                pickupReadyTime
                        );

                /*
                 * Calculate delivery arrival time.
                 */

                double deliveryArrivalTime =
                        actualPickupTime
                                + deliveryDuration;

                /*
                 * Check basic feasibility.
                 */

                boolean feasible =
                        isFeasible(
                                removedAssignment,
                                agent,
                                agentStartTime,
                                actualPickupTime,
                                deliveryArrivalTime
                        );

                if (!feasible) {
                    continue;
                }

                /*
                 * Choose the agent that gives the
                 * shortest total distance.
                 */

                if (candidate.getTotalDistance()
                        < bestDistance) {

                    bestDistance =
                            candidate.getTotalDistance();

                    bestAssignment =
                            candidate;

                    bestAgentId =
                            agentId;

                    bestNewLocation =
                            recipientLocation;

                    bestNewTime =
                            convertSecondsToTime(
                                    deliveryArrivalTime
                            );
                }
            }

            /*
             * If a feasible agent was found,
             * add the assignment and update the
             * temporary state of that agent.
             */

            if (bestAssignment != null) {

                repairedSolution.addAssignment(
                        bestAssignment
                );

                agentLocations.put(
                        bestAgentId,
                        bestNewLocation
                );

                agentTimes.put(
                        bestAgentId,
                        bestNewTime
                );
            }
        }

        return repairedSolution;
    }

    private boolean isFeasible(
            Assignment assignment,
            DeliveryAgent agent,
            double agentStartTime,
            double actualPickupTime,
            double deliveryArrivalTime) {

        Donor donor =
                assignment.getDonor();

        Recipient recipient =
                assignment.getRecipient();

        int quantity =
                assignment.getQuantity();

        // Check donor quantity

        if (quantity >
                donor.getAvailableQuantity()) {

            return false;
        }

        // Check vehicle capacity

        if (quantity >
                agent.getCapacity()) {

            return false;
        }

        // Check agent availability

        double agentAvailableFrom =
                convertTimeToSeconds(
                        agent.getAvailableFrom()
                );

        double agentAvailableUntil =
                convertTimeToSeconds(
                        agent.getAvailableUntil()
                );

        if (agentStartTime <
                agentAvailableFrom) {

            return false;
        }

        if (deliveryArrivalTime >
                agentAvailableUntil) {

            return false;
        }

        // Check food expiry

        double expiryTime =
                convertTimeToSeconds(
                        donor.getExpiryTime()
                );

        if (deliveryArrivalTime >
                expiryTime) {

            return false;
        }

        // Check recipient deadline

        double deadline =
                convertTimeToSeconds(
                        recipient.getLatestDeliveryTime()
                );

        if (deliveryArrivalTime >
                deadline) {

            return false;
        }

        return true;
    }

    private double convertTimeToSeconds(
            String time) {

        String[] parts =
                time.split(":");

        int hours =
                Integer.parseInt(
                        parts[0]
                );

        int minutes =
                Integer.parseInt(
                        parts[1]
                );

        int seconds = 0;

        if (parts.length == 3) {

            seconds =
                    Integer.parseInt(
                            parts[2]
                    );
        }

        return hours * 3600
                + minutes * 60
                + seconds;
    }

    private String convertSecondsToTime(
            double totalSeconds) {

        int seconds =
                (int) Math.round(
                        totalSeconds
                );

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
}
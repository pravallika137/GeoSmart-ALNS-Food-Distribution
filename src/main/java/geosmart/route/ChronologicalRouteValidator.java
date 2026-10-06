package geosmart.route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChronologicalRouteValidator {

    private TravelMatrix travelMatrix;

    public ChronologicalRouteValidator(
            TravelMatrix travelMatrix) {

        this.travelMatrix = travelMatrix;
    }

    public boolean validate(
            Solution solution,
            List<Recipient> recipients,
            List<DeliveryAgent> agents) {

        boolean valid = true;

        System.out.println();
        System.out.println(
                "===== CHRONOLOGICAL ROUTE VALIDATION ====="
        );

        /*
         * Store assignments separately for each agent.
         */

        Map<String, List<Assignment>> agentAssignments =
                new HashMap<>();

        for (Assignment assignment :
                solution.getAssignments()) {

            String agentId =
                    assignment
                            .getAgent()
                            .getAgentId();

            agentAssignments
                    .computeIfAbsent(
                            agentId,
                            key -> new ArrayList<>()
                    )
                    .add(assignment);
        }

        /*
         * Validate every agent route.
         */

        for (DeliveryAgent agent : agents) {

            String agentId =
                    agent.getAgentId();

            List<Assignment> assignments =
                    agentAssignments.getOrDefault(
                            agentId,
                            new ArrayList<>()
                    );

            if (assignments.isEmpty()) {
                continue;
            }

            System.out.println();
            System.out.println(
                    "Agent " + agentId
            );

            String currentLocation =
                    agent.getLocationId();

            double currentTime =
                    convertTimeToSeconds(
                            agent.getAvailableFrom()
                    );

            /*
             * Check assignments in the order in which
             * they appear in the solution.
             */

            for (Assignment assignment :
                    assignments) {

                Donor donor =
                        assignment.getDonor();

                Recipient recipient =
                        assignment.getRecipient();

                int quantity =
                        assignment.getQuantity();

                String donorLocation =
                        donor.getLocationId();

                String recipientLocation =
                        recipient.getLocationId();

                /*
                 * Check vehicle capacity.
                 */

                if (quantity >
                        agent.getCapacity()) {

                    System.out.println(
                            "INVALID: "
                                    + agentId
                                    + " capacity exceeded for "
                                    + donor.getDonorId()
                                    + " → "
                                    + recipient.getRecipientId()
                    );

                    valid = false;
                }

                /*
                 * Travel from current location
                 * to donor.
                 */

                double pickupDuration =
                        travelMatrix.getDuration(
                                currentLocation,
                                donorLocation
                        );

                double arrivalAtDonor =
                        currentTime
                                + pickupDuration;

                /*
                 * Food ready time.
                 */

                double pickupReadyTime =
                        convertTimeToSeconds(
                                donor.getPickupReadyTime()
                        );

                /*
                 * If agent reaches before food is
                 * ready, agent waits.
                 */

                double actualPickupTime =
                        Math.max(
                                arrivalAtDonor,
                                pickupReadyTime
                        );

                /*
                 * Check donor expiry before pickup.
                 */

                double expiryTime =
                        convertTimeToSeconds(
                                donor.getExpiryTime()
                        );

                if (actualPickupTime >
                        expiryTime) {

                    System.out.println(
                            "INVALID: Food from "
                                    + donor.getDonorId()
                                    + " is expired before pickup."
                    );

                    valid = false;
                }

                /*
                 * Travel from donor to recipient.
                 */

                double deliveryDuration =
                        travelMatrix.getDuration(
                                donorLocation,
                                recipientLocation
                        );

                double deliveryArrivalTime =
                        actualPickupTime
                                + deliveryDuration;

                /*
                 * Check food expiry at delivery.
                 */

                if (deliveryArrivalTime >
                        expiryTime) {

                    System.out.println(
                            "INVALID: Food from "
                                    + donor.getDonorId()
                                    + " expires before delivery to "
                                    + recipient.getRecipientId()
                    );

                    valid = false;
                }

                /*
                 * Check recipient deadline.
                 */

                double recipientDeadline =
                        convertTimeToSeconds(
                                recipient
                                        .getLatestDeliveryTime()
                        );

                if (deliveryArrivalTime >
                        recipientDeadline) {

                    System.out.println(
                            "INVALID: "
                                    + recipient.getRecipientId()
                                    + " deadline missed by "
                                    + agentId
                    );

                    valid = false;
                }

                /*
                 * Check agent availability end time.
                 */

                double agentAvailableUntil =
                        convertTimeToSeconds(
                                agent.getAvailableUntil()
                        );

                if (deliveryArrivalTime >
                        agentAvailableUntil) {

                    System.out.println(
                            "INVALID: Agent "
                                    + agentId
                                    + " exceeds availability."
                    );

                    valid = false;
                }

                /*
                 * Print route information.
                 */

                System.out.println(
                        currentLocation
                                + " → "
                                + donorLocation
                                + " → "
                                + recipientLocation
                                + " | "
                                + donor.getDonorId()
                                + " → "
                                + recipient.getRecipientId()
                                + " | "
                                + quantity
                                + " meals"
                );

                System.out.println(
                        "  Start time        : "
                                + convertSecondsToTime(
                                        currentTime
                                )
                );

                System.out.println(
                        "  Pickup time       : "
                                + convertSecondsToTime(
                                        actualPickupTime
                                )
                );

                System.out.println(
                        "  Delivery time     : "
                                + convertSecondsToTime(
                                        deliveryArrivalTime
                                )
                );

                System.out.println(
                        "  Recipient deadline: "
                                + recipient
                                        .getLatestDeliveryTime()
                );

                /*
                 * After delivery, the agent is now
                 * located at the recipient location.
                 */

                currentLocation =
                        recipientLocation;

                currentTime =
                        deliveryArrivalTime;
            }
        }

        /*
         * Check recipient demand.
         */

        System.out.println();
        System.out.println(
                "----- Demand Validation -----"
        );

        Map<String, Integer> delivered =
                new HashMap<>();

        for (Assignment assignment :
                solution.getAssignments()) {

            String recipientId =
                    assignment
                            .getRecipient()
                            .getRecipientId();

            int current =
                    delivered.getOrDefault(
                            recipientId,
                            0
                    );

            delivered.put(
                    recipientId,
                    current
                            + assignment.getQuantity()
            );
        }

        for (Recipient recipient :
                recipients) {

            int deliveredQuantity =
                    delivered.getOrDefault(
                            recipient.getRecipientId(),
                            0
                    );

            int required =
                    recipient.getRequiredQuantity();

            int unmet =
                    Math.max(
                            0,
                            required
                                    - deliveredQuantity
                    );

            System.out.println(
                    recipient.getRecipientId()
                            + " | Required: "
                            + required
                            + " | Delivered: "
                            + deliveredQuantity
                            + " | Unmet: "
                            + unmet
            );

            if (unmet > 0) {

                valid = false;
            }
        }

        /*
         * Final result.
         */

        System.out.println();

        if (valid) {

            System.out.println(
                    "CHRONOLOGICAL VALIDATION: VALID"
            );

            System.out.println(
                    "All checked chronological constraints are satisfied."
            );

        } else {

            System.out.println(
                    "CHRONOLOGICAL VALIDATION: INVALID"
            );

            System.out.println(
                    "One or more chronological constraints are violated."
            );
        }

        System.out.println(
                "==========================================="
        );

        return valid;
    }

    private double convertTimeToSeconds(
            String time) {

        String[] parts =
                time.split(":");

        int hours =
                Integer.parseInt(parts[0]);

        int minutes =
                Integer.parseInt(parts[1]);

        int seconds = 0;

        if (parts.length == 3) {

            seconds =
                    Integer.parseInt(parts[2]);
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
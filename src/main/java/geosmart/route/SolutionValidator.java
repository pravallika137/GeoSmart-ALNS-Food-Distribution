package geosmart.route;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolutionValidator {

    private TravelMatrix travelMatrix;

    public SolutionValidator(TravelMatrix travelMatrix) {
        this.travelMatrix = travelMatrix;
    }

    public boolean validate(
            Solution solution,
            List<Recipient> recipients,
            List<DeliveryAgent> agents) {

        boolean valid = true;

        System.out.println();
        System.out.println(
                "========== SOLUTION VALIDATION =========="
        );

        /*
         * Track how much food has been delivered
         * to every recipient.
         */

        Map<String, Integer> deliveredToRecipient =
                new HashMap<>();

        /*
         * Track how much food has been taken
         * from every donor.
         */

        Map<String, Integer> takenFromDonor =
                new HashMap<>();

        /*
         * Validate every assignment.
         */

        for (Assignment assignment :
                solution.getAssignments()) {

            Donor donor =
                    assignment.getDonor();

            Recipient recipient =
                    assignment.getRecipient();

            DeliveryAgent agent =
                    assignment.getAgent();

            int quantity =
                    assignment.getQuantity();

            /*
             * 1. Check quantity.
             */

            if (quantity <= 0) {

                System.out.println(
                        "INVALID: Assignment has "
                                + "invalid quantity: "
                                + quantity
                );

                valid = false;
            }

            /*
             * 2. Check vehicle capacity.
             */

            if (quantity > agent.getCapacity()) {

                System.out.println(
                        "INVALID: "
                                + assignment
                                + " exceeds vehicle capacity."
                );

                valid = false;
            }

            /*
             * Track donor quantity.
             */

            int currentDonorQuantity =
                    takenFromDonor.getOrDefault(
                            donor.getDonorId(),
                            0
                    );

            takenFromDonor.put(
                    donor.getDonorId(),
                    currentDonorQuantity + quantity
            );

            /*
             * Track recipient quantity.
             */

            int currentRecipientQuantity =
                    deliveredToRecipient.getOrDefault(
                            recipient.getRecipientId(),
                            0
                    );

            deliveredToRecipient.put(
                    recipient.getRecipientId(),
                    currentRecipientQuantity + quantity
            );

            /*
             * 3. Check travel route.
             */

            String agentLocation =
                    agent.getLocationId();

            String donorLocation =
                    donor.getLocationId();

            String recipientLocation =
                    recipient.getLocationId();

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

            /*
             * 4. Calculate pickup time.
             */

            double agentStartTime =
                    convertTimeToSeconds(
                            agent.getAvailableFrom()
                    );

            double pickupReadyTime =
                    convertTimeToSeconds(
                            donor.getPickupReadyTime()
                    );

            double arrivalAtDonor =
                    agentStartTime
                            + pickupDuration;

            double actualPickupTime =
                    Math.max(
                            arrivalAtDonor,
                            pickupReadyTime
                    );

            /*
             * 5. Calculate delivery time.
             */

            double deliveryArrivalTime =
                    actualPickupTime
                            + deliveryDuration;

            /*
             * 6. Check food expiry.
             */

            double expiryTime =
                    convertTimeToSeconds(
                            donor.getExpiryTime()
                    );

            if (deliveryArrivalTime >
                    expiryTime) {

                System.out.println(
                        "INVALID: "
                                + assignment
                                + " violates food expiry."
                );

                valid = false;
            }

            /*
             * 7. Check recipient deadline.
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
                                + assignment
                                + " misses recipient deadline."
                );

                valid = false;
            }

            /*
             * 8. Check agent availability.
             */

            double agentAvailableUntil =
                    convertTimeToSeconds(
                            agent.getAvailableUntil()
                    );

            if (deliveryArrivalTime >
                    agentAvailableUntil) {

                System.out.println(
                        "INVALID: "
                                + assignment
                                + " exceeds agent availability."
                );

                valid = false;
            }
        }

        /*
         * 9. Check donor food availability.
         */

        for (Assignment assignment :
                solution.getAssignments()) {

            Donor donor =
                    assignment.getDonor();

            int taken =
                    takenFromDonor.getOrDefault(
                            donor.getDonorId(),
                            0
                    );

            if (taken >
                    donor.getAvailableQuantity()) {

                System.out.println(
                        "INVALID: Donor "
                                + donor.getDonorId()
                                + " provides "
                                + taken
                                + " meals but only has "
                                + donor.getAvailableQuantity()
                                + " meals."
                );

                valid = false;
            }
        }

        /*
         * 10. Check recipient demand.
         */

        for (Recipient recipient :
                recipients) {

            int delivered =
                    deliveredToRecipient.getOrDefault(
                            recipient.getRecipientId(),
                            0
                    );

            int required =
                    recipient.getRequiredQuantity();

            if (delivered < required) {

                int unmet =
                        required - delivered;

                System.out.println(
                        "WARNING: Recipient "
                                + recipient.getRecipientId()
                                + " has "
                                + unmet
                                + " unmet meals."
                );

                valid = false;
            }
        }

        /*
         * Print recipient summary.
         */

        System.out.println();

        System.out.println(
                "----- Recipient Demand Check -----"
        );

        for (Recipient recipient :
                recipients) {

            int delivered =
                    deliveredToRecipient.getOrDefault(
                            recipient.getRecipientId(),
                            0
                    );

            int required =
                    recipient.getRequiredQuantity();

            System.out.println(
                    recipient.getRecipientId()
                            + " | Required: "
                            + required
                            + " | Delivered: "
                            + delivered
                            + " | Unmet: "
                            + Math.max(
                                    0,
                                    required - delivered
                            )
            );
        }

        /*
         * Final result.
         */

        System.out.println();

        if (valid) {

            System.out.println(
                    "FINAL VALIDATION RESULT: VALID"
            );

            System.out.println(
                    "All checked constraints are satisfied."
            );

        } else {

            System.out.println(
                    "FINAL VALIDATION RESULT: INVALID"
            );

            System.out.println(
                    "One or more constraints are violated."
            );
        }

        System.out.println(
                "=========================================="
        );

        return valid;
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
}
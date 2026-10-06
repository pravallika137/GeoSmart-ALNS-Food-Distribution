package geosmart.route;

import java.util.List;

public class ObjectiveFunction {

    // Penalty for every unit of food that is not delivered
    private static final double UNMET_DEMAND_PENALTY = 10000.0;

    // Penalty for a delivery that violates a deadline
    private static final double DEADLINE_VIOLATION_PENALTY = 20000.0;

    public double calculateCost(
            Solution solution,
            List<Recipient> recipients) {

        double totalDistance =
                solution.getTotalDistance();

        double unmetDemandPenalty =
                calculateUnmetDemandPenalty(
                        solution,
                        recipients
                );

        double deadlinePenalty =
                calculateDeadlinePenalty(
                        solution
                );

        return totalDistance
                + unmetDemandPenalty
                + deadlinePenalty;
    }

    private double calculateUnmetDemandPenalty(
            Solution solution,
            List<Recipient> recipients) {

        double penalty = 0;

        for (Recipient recipient : recipients) {

            int deliveredQuantity = 0;

            for (Assignment assignment :
                    solution.getAssignments()) {

                if (assignment
                        .getRecipient()
                        .getRecipientId()
                        .equals(
                                recipient.getRecipientId()
                        )) {

                    deliveredQuantity +=
                            assignment.getQuantity();
                }
            }

            int unmetQuantity =
                    recipient.getRequiredQuantity()
                            - deliveredQuantity;

            if (unmetQuantity > 0) {

                penalty +=
                        unmetQuantity
                                * UNMET_DEMAND_PENALTY;
            }
        }

        return penalty;
    }

    private double calculateDeadlinePenalty(
            Solution solution) {

        double penalty = 0;

        for (Assignment assignment :
                solution.getAssignments()) {

            Recipient recipient =
                    assignment.getRecipient();

            double deliveryDuration =
                    assignment.getDeliveryDuration();

            double pickupTime =
                    convertTimeToSeconds(
                            assignment
                                    .getDonor()
                                    .getPickupReadyTime()
                    );

            double deliveryArrivalTime =
                    pickupTime
                            + deliveryDuration;

            double deadline =
                    convertTimeToSeconds(
                            recipient
                                    .getLatestDeliveryTime()
                    );

            if (deliveryArrivalTime > deadline) {

                penalty +=
                        DEADLINE_VIOLATION_PENALTY;
            }
        }

        return penalty;
    }

    private double convertTimeToSeconds(
            String time) {

        String[] parts =
                time.split(":");

        int hours =
                Integer.parseInt(parts[0]);

        int minutes =
                Integer.parseInt(parts[1]);

        return hours * 3600
                + minutes * 60;
    }
}
package geosmart.route;

public class AssignmentEvaluator {

    private TravelMatrix travelMatrix;

    public AssignmentEvaluator(
            TravelMatrix travelMatrix) {

        this.travelMatrix = travelMatrix;
    }

    public void evaluate(
            Assignment assignment) {

        String agentLocation =
                assignment.getAgent().getLocationId();

        String donorLocation =
                assignment.getDonor().getLocationId();

        String recipientLocation =
                assignment.getRecipient().getLocationId();

        double pickupDistance =
                travelMatrix.getDistance(
                        agentLocation,
                        donorLocation
                );

        double pickupDuration =
                travelMatrix.getDuration(
                        agentLocation,
                        donorLocation
                );

        double deliveryDistance =
                travelMatrix.getDistance(
                        donorLocation,
                        recipientLocation
                );

        double deliveryDuration =
                travelMatrix.getDuration(
                        donorLocation,
                        recipientLocation
                );

        assignment.setPickupDistance(
                pickupDistance
        );

        assignment.setPickupDuration(
                pickupDuration
        );

        assignment.setDeliveryDistance(
                deliveryDistance
        );

        assignment.setDeliveryDuration(
                deliveryDuration
        );

        assignment.calculateTotals();
    }
}
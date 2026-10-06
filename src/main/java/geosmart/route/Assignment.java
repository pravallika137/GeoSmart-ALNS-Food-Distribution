package geosmart.route;

public class Assignment {

    private Donor donor;
    private Recipient recipient;
    private DeliveryAgent agent;

    private int quantity;

    private double pickupDistance;
    private double deliveryDistance;

    private double pickupDuration;
    private double deliveryDuration;

    private double totalDistance;
    private double totalDuration;

    public Assignment(
            Donor donor,
            Recipient recipient,
            DeliveryAgent agent,
            int quantity) {

        this.donor = donor;
        this.recipient = recipient;
        this.agent = agent;
        this.quantity = quantity;
    }

    public Donor getDonor() {
        return donor;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public DeliveryAgent getAgent() {
        return agent;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPickupDistance() {
        return pickupDistance;
    }

    public double getDeliveryDistance() {
        return deliveryDistance;
    }

    public double getPickupDuration() {
        return pickupDuration;
    }

    public double getDeliveryDuration() {
        return deliveryDuration;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public double getTotalDuration() {
        return totalDuration;
    }

    public void setPickupDistance(double pickupDistance) {
        this.pickupDistance = pickupDistance;
    }

    public void setDeliveryDistance(double deliveryDistance) {
        this.deliveryDistance = deliveryDistance;
    }

    public void setPickupDuration(double pickupDuration) {
        this.pickupDuration = pickupDuration;
    }

    public void setDeliveryDuration(double deliveryDuration) {
        this.deliveryDuration = deliveryDuration;
    }

    public void calculateTotals() {

        totalDistance =
                pickupDistance + deliveryDistance;

        totalDuration =
                pickupDuration + deliveryDuration;
    }

    @Override
    public String toString() {

        return donor.getDonorId()
                + " → "
                + agent.getAgentId()
                + " → "
                + recipient.getRecipientId()
                + " | Quantity: "
                + quantity
                + " | Distance: "
                + totalDistance
                + " m"
                + " | Duration: "
                + totalDuration
                + " sec";
    }
}
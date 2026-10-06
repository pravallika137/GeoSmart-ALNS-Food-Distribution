package geosmart.route;

public class Recipient {

    private String recipientId;
    private String locationId;
    private int requiredQuantity;
    private String priority;
    private String requestTime;
    private String latestDeliveryTime;

    public Recipient(
            String recipientId,
            String locationId,
            int requiredQuantity,
            String priority,
            String requestTime,
            String latestDeliveryTime) {

        this.recipientId = recipientId;
        this.locationId = locationId;
        this.requiredQuantity = requiredQuantity;
        this.priority = priority;
        this.requestTime = requestTime;
        this.latestDeliveryTime = latestDeliveryTime;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public String getLocationId() {
        return locationId;
    }

    public int getRequiredQuantity() {
        return requiredQuantity;
    }

    public String getPriority() {
        return priority;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public String getLatestDeliveryTime() {
        return latestDeliveryTime;
    }

    @Override
    public String toString() {

        return recipientId
                + " | Location: "
                + locationId
                + " | Required: "
                + requiredQuantity
                + " | Priority: "
                + priority
                + " | Request: "
                + requestTime
                + " | Latest Delivery: "
                + latestDeliveryTime;
    }
}
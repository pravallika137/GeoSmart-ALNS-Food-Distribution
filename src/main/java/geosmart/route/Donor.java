package geosmart.route;

public class Donor {

    private String donorId;
    private String locationId;
    private String foodType;
    private int availableQuantity;
    private String pickupReadyTime;
    private String expiryTime;

    public Donor(
            String donorId,
            String locationId,
            String foodType,
            int availableQuantity,
            String pickupReadyTime,
            String expiryTime) {

        this.donorId = donorId;
        this.locationId = locationId;
        this.foodType = foodType;
        this.availableQuantity = availableQuantity;
        this.pickupReadyTime = pickupReadyTime;
        this.expiryTime = expiryTime;
    }

    public String getDonorId() {
        return donorId;
    }

    public String getLocationId() {
        return locationId;
    }

    public String getFoodType() {
        return foodType;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public String getPickupReadyTime() {
        return pickupReadyTime;
    }

    public String getExpiryTime() {
        return expiryTime;
    }

    @Override
    public String toString() {

        return donorId
                + " | Location: "
                + locationId
                + " | Food: "
                + foodType
                + " | Quantity: "
                + availableQuantity
                + " | Pickup: "
                + pickupReadyTime
                + " | Expiry: "
                + expiryTime;
    }
}
package geosmart.route;

public class DeliveryAgent {

    private String agentId;
    private String locationId;
    private String vehicleType;
    private int capacity;
    private String availableFrom;
    private String availableUntil;

    // Current state of the agent
    private String currentLocationId;
    private String currentTime;
    private AgentStatus status;

    public DeliveryAgent(
            String agentId,
            String locationId,
            String vehicleType,
            int capacity,
            String availableFrom,
            String availableUntil) {

        this.agentId = agentId;
        this.locationId = locationId;
        this.vehicleType = vehicleType;
        this.capacity = capacity;
        this.availableFrom = availableFrom;
        this.availableUntil = availableUntil;

        // Initially agent is at starting location
        this.currentLocationId = locationId;

        // Initially agent becomes available at availableFrom
        this.currentTime = availableFrom;

        // Initially available
        this.status = AgentStatus.AVAILABLE;
    }

    public String getAgentId() {
        return agentId;
    }

    public String getLocationId() {
        return locationId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getAvailableFrom() {
        return availableFrom;
    }

    public String getAvailableUntil() {
        return availableUntil;
    }

    public String getCurrentLocationId() {
        return currentLocationId;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public AgentStatus getStatus() {
        return status;
    }

    public void setCurrentLocationId(String currentLocationId) {
        this.currentLocationId = currentLocationId;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public void setStatus(AgentStatus status) {
        this.status = status;
    }

    public boolean isAvailable() {
        return status == AgentStatus.AVAILABLE;
    }

    @Override
    public String toString() {

        return agentId
                + " | "
                + vehicleType
                + " | Capacity: "
                + capacity
                + " | Location: "
                + currentLocationId
                + " | Time: "
                + currentTime
                + " | Status: "
                + status;
    }
}
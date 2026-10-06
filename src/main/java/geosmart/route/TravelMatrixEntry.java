package geosmart.route;

public class TravelMatrixEntry {

    private String fromLocationId;
    private String toLocationId;
    private double distanceMeters;
    private double durationSeconds;

    public TravelMatrixEntry(
            String fromLocationId,
            String toLocationId,
            double distanceMeters,
            double durationSeconds) {

        this.fromLocationId = fromLocationId;
        this.toLocationId = toLocationId;
        this.distanceMeters = distanceMeters;
        this.durationSeconds = durationSeconds;
    }

    public String getFromLocationId() {
        return fromLocationId;
    }

    public String getToLocationId() {
        return toLocationId;
    }

    public double getDistanceMeters() {
        return distanceMeters;
    }

    public double getDurationSeconds() {
        return durationSeconds;
    }
}
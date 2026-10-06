package geosmart.route;

public class Location {

    private String locationId;
    private String name;
    private String locationType;
    private double latitude;
    private double longitude;

    public Location(
            String locationId,
            String name,
            String locationType,
            double latitude,
            double longitude) {

        this.locationId = locationId;
        this.name = name;
        this.locationType = locationType;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLocationId() {
        return locationId;
    }

    public String getName() {
        return name;
    }

    public String getLocationType() {
        return locationType;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {

        return locationId
                + " - "
                + name
                + " ("
                + latitude
                + ", "
                + longitude
                + ")";
    }
}
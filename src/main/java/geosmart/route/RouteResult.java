package geosmart.route;

public class RouteResult {

    private double distance;
    private double duration;

    public RouteResult(double distance, double duration) {
        this.distance = distance;
        this.duration = duration;
    }

    public double getDistance() {
        return distance;
    }

    public double getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "Distance: " + distance + " meters, "
                + "Duration: " + duration + " seconds";
    }
}
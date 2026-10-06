package geosmart.route;

import java.util.List;

public class TravelMatrixGenerator {

    public static void main(String[] args) {

        List<Location> locations =
                LocationDataReader.readLocations(
                        "data/locations.csv"
                );

        OSRMRouteService routeService =
                new OSRMRouteService();

        for (Location from : locations) {

            for (Location to : locations) {

                if (from.getLocationId()
                        .equals(to.getLocationId())) {

                    continue;
                }

                RouteResult result =
                        routeService.getRoute(from, to);

                System.out.println(
                        from.getLocationId()
                        + " → "
                        + to.getLocationId()
                        + " | Distance: "
                        + result.getDistance()
                        + " meters"
                        + " | Duration: "
                        + result.getDuration()
                        + " seconds"
                );
            }
        }
    }
}
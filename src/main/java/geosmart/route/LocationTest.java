package geosmart.route;

import java.util.List;

public class LocationTest {

    public static void main(String[] args) {

        List<Location> locations =
                LocationLoader.loadLocations(
                        "data/locations.csv"
                );

        System.out.println(
                "Total locations: "
                        + locations.size()
        );

        for (Location location : locations) {

            System.out.println(location);
        }
    }
}
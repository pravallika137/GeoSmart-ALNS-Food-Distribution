package geosmart.route;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class LocationDataReader {

    public static List<Location> readLocations(String filePath) {

        List<Location> locations = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;

            // Skip the CSV header
            reader.readLine();

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                String locationId = data[0];
                String name = data[1];
                String locationType = data[2];
                double latitude = Double.parseDouble(data[3]);
                double longitude = Double.parseDouble(data[4]);

                Location location = new Location(
                        locationId,
                        name,
                        locationType,
                        latitude,
                        longitude
                );

                locations.add(location);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return locations;
    }
    public static void main(String[] args) {

        List<Location> locations =
                readLocations("data/locations.csv");

        for (Location location : locations) {
            System.out.println(location);
        }
    }
}
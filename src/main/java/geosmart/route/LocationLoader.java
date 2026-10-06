package geosmart.route;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocationLoader {

    public static List<Location> loadLocations(
            String filePath) {

        List<Location> locations =
                new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(
                             new FileReader(filePath))) {

            String line;

            // Skip header
            reader.readLine();

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = line.split(",");

                String locationId = values[0];
                String name = values[1];
                String locationType = values[2];

                double latitude =
                        Double.parseDouble(values[3]);

                double longitude =
                        Double.parseDouble(values[4]);

                Location location =
                        new Location(
                                locationId,
                                name,
                                locationType,
                                latitude,
                                longitude
                        );

                locations.add(location);
            }

        } catch (IOException e) {

            System.out.println(
                    "Error reading locations: "
                            + e.getMessage()
            );
        }

        return locations;
    }
}
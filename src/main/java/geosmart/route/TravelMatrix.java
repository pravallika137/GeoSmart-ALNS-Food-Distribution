package geosmart.route;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TravelMatrix {

    private final Map<String, Double> distanceMatrix;
    private final Map<String, Double> durationMatrix;

    public TravelMatrix(String filePath) {

        distanceMatrix = new HashMap<>();
        durationMatrix = new HashMap<>();

        loadMatrix(filePath);
    }

    private void loadMatrix(String filePath) {

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(filePath))) {

            String line;

            // Skip CSV header
            reader.readLine();

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = line.split(",");

                String fromLocation = values[0];
                String toLocation = values[1];

                double distance =
                        Double.parseDouble(values[2]);

                double duration =
                        Double.parseDouble(values[3]);

                String key =
                        fromLocation + "_" + toLocation;

                distanceMatrix.put(key, distance);
                durationMatrix.put(key, duration);
            }

        } catch (IOException e) {

            System.out.println(
                    "Error reading travel matrix: "
                            + e.getMessage()
            );
        }
    }

    public double getDistance(
            String fromLocation,
            String toLocation) {

        if (fromLocation.equals(toLocation)) {
            return 0.0;
        }

        String key =
                fromLocation + "_" + toLocation;

        return distanceMatrix.getOrDefault(key, -1.0);
    }

    public double getDuration(
            String fromLocation,
            String toLocation) {

        if (fromLocation.equals(toLocation)) {
            return 0.0;
        }

        String key =
                fromLocation + "_" + toLocation;

        return durationMatrix.getOrDefault(key, -1.0);
    }
}
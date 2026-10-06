package geosmart.route;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DonorLoader {

    public static List<Donor> loadDonors(
            String filePath) {

        List<Donor> donors =
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

                String donorId = values[0];
                String locationId = values[1];
                String foodType = values[2];

                int availableQuantity =
                        Integer.parseInt(values[3]);

                String pickupReadyTime = values[4];
                String expiryTime = values[5];

                Donor donor =
                        new Donor(
                                donorId,
                                locationId,
                                foodType,
                                availableQuantity,
                                pickupReadyTime,
                                expiryTime
                        );

                donors.add(donor);
            }

        } catch (IOException e) {

            System.out.println(
                    "Error reading donors: "
                            + e.getMessage()
            );
        }

        return donors;
    }
}
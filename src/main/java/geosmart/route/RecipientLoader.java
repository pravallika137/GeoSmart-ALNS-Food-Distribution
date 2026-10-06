package geosmart.route;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecipientLoader {

    public static List<Recipient> loadRecipients(
            String filePath) {

        List<Recipient> recipients =
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

                String recipientId = values[0];
                String locationId = values[1];

                int requiredQuantity =
                        Integer.parseInt(values[2]);

                String priority = values[3];
                String requestTime = values[4];
                String latestDeliveryTime = values[5];

                Recipient recipient =
                        new Recipient(
                                recipientId,
                                locationId,
                                requiredQuantity,
                                priority,
                                requestTime,
                                latestDeliveryTime
                        );

                recipients.add(recipient);
            }

        } catch (IOException e) {

            System.out.println(
                    "Error reading recipients: "
                            + e.getMessage()
            );
        }

        return recipients;
    }
}
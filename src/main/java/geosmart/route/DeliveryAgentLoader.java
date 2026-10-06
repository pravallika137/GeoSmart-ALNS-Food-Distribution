package geosmart.route;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeliveryAgentLoader {

    public static List<DeliveryAgent> loadAgents(
            String filePath) {

        List<DeliveryAgent> agents =
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

                String agentId = values[0];
                String locationId = values[1];
                String vehicleType = values[2];

                int capacity =
                        Integer.parseInt(values[3]);

                String availableFrom = values[4];
                String availableUntil = values[5];

                DeliveryAgent agent =
                        new DeliveryAgent(
                                agentId,
                                locationId,
                                vehicleType,
                                capacity,
                                availableFrom,
                                availableUntil
                        );

                agents.add(agent);
            }

        } catch (IOException e) {

            System.out.println(
                    "Error reading delivery agents: "
                            + e.getMessage()
            );
        }

        return agents;
    }
}
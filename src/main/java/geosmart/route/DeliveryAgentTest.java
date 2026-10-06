package geosmart.route;

import java.util.List;

public class DeliveryAgentTest {

    public static void main(String[] args) {

        List<DeliveryAgent> agents =
                DeliveryAgentLoader.loadAgents(
                        "data/delivery_agents.csv"
                );

        for (DeliveryAgent agent : agents) {

            System.out.println(agent);
        }
    }
}
package geosmart.route;

import java.util.List;
import java.util.Random;

public class ALNS {

    private DestroyOperator destroyOperator;
    private RepairOperator repairOperator;
    private ObjectiveFunction objectiveFunction;

    private Random random;

    public ALNS(TravelMatrix travelMatrix) {

        destroyOperator =
                new DestroyOperator();

        repairOperator =
                new RepairOperator(
                        travelMatrix
                );

        objectiveFunction =
                new ObjectiveFunction();

        random =
                new Random();
    }

    public Solution optimize(
            Solution initialSolution,
            int iterations,
            int numberToRemove,
            List<DeliveryAgent> agents,
            List<Recipient> recipients) {

        Solution currentSolution =
                initialSolution.copy();

        Solution bestSolution =
                initialSolution.copy();

        double temperature = 10000.0;

        double coolingRate = 0.95;

        for (int i = 1; i <= iterations; i++) {

            // Step 1: Destroy part of the current solution

            Solution destroyedSolution =
                    destroyOperator.destroy(
                            currentSolution,
                            numberToRemove
                    );

            // Step 2: Repair the destroyed solution

            Solution repairedSolution =
                    repairOperator.repair(
                            currentSolution,
                            destroyedSolution,
                            agents
                    );

            // Step 3: Calculate objective costs

            double currentCost =
                    objectiveFunction.calculateCost(
                            currentSolution,
                            recipients
                    );

            double repairedCost =
                    objectiveFunction.calculateCost(
                            repairedSolution,
                            recipients
                    );

            // Step 4: Calculate cost difference

            double difference =
                    repairedCost - currentCost;

            // Step 5: Decide whether to accept

            boolean accept = false;

            // Better solution

            if (difference < 0) {

                accept = true;

            } else {

                // Worse solution

                double probability =
                        Math.exp(
                                -difference
                                        / temperature
                        );

                double randomValue =
                        random.nextDouble();

                if (randomValue < probability) {

                    accept = true;
                }
            }

            // Step 6: Update current solution

            if (accept) {

                currentSolution =
                        repairedSolution;
            }

            // Step 7: Calculate current objective cost

            double currentObjectiveCost =
                    objectiveFunction.calculateCost(
                            currentSolution,
                            recipients
                    );

            // Step 8: Update best solution

            double bestObjectiveCost =
                    objectiveFunction.calculateCost(
                            bestSolution,
                            recipients
                    );

            if (currentObjectiveCost
                    < bestObjectiveCost) {

                bestSolution =
                        currentSolution.copy();
            }

            // Step 9: Reduce temperature

            temperature =
                    temperature * coolingRate;

            // Step 10: Print iteration information

            System.out.println(
                    "Iteration "
                            + i
                            + " | Current Cost: "
                            + currentObjectiveCost
                            + " | Best Cost: "
                            + objectiveFunction.calculateCost(
                                    bestSolution,
                                    recipients
                            )
                            + " | Temperature: "
                            + temperature
            );
        }

        return bestSolution;
    }
}
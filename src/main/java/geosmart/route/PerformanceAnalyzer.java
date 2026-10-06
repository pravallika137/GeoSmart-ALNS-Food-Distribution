package geosmart.route;

import java.util.List;

public class PerformanceAnalyzer {

    public void analyze(
            Solution initialSolution,
            Solution optimizedSolution,
            List<Recipient> recipients) {

        double initialDistance =
                initialSolution.getTotalDistance();

        double optimizedDistance =
                optimizedSolution.getTotalDistance();

        double distanceSaved =
                initialDistance - optimizedDistance;

        double distanceImprovement =
                (distanceSaved / initialDistance) * 100;

        double initialDuration =
                initialSolution.getTotalDuration();

        double optimizedDuration =
                optimizedSolution.getTotalDuration();

        double durationSaved =
                initialDuration - optimizedDuration;

        double durationImprovement =
                (durationSaved / initialDuration) * 100;

        int initialAssignments =
                initialSolution.getAssignmentCount();

        int optimizedAssignments =
                optimizedSolution.getAssignmentCount();

        int totalRequiredFood = 0;

        int totalDeliveredFood = 0;

        for (Recipient recipient : recipients) {

            totalRequiredFood +=
                    recipient.getRequiredQuantity();

            for (Assignment assignment :
                    optimizedSolution.getAssignments()) {

                if (assignment
                        .getRecipient()
                        .getRecipientId()
                        .equals(
                                recipient.getRecipientId()
                        )) {

                    totalDeliveredFood +=
                            assignment.getQuantity();
                }
            }
        }

        int unmetDemand =
                Math.max(
                        0,
                        totalRequiredFood
                                - totalDeliveredFood
                );

        double demandFulfillment =
                totalRequiredFood == 0
                        ? 0
                        : ((double) totalDeliveredFood
                        / totalRequiredFood) * 100;

        ObjectiveFunction objectiveFunction =
                new ObjectiveFunction();

        double objectiveCost =
                objectiveFunction.calculateCost(
                        optimizedSolution,
                        recipients
                );

        System.out.println();

        System.out.println(
                "========== PERFORMANCE ANALYSIS =========="
        );

        System.out.printf(
                "Initial distance      : %.2f meters%n",
                initialDistance
        );

        System.out.printf(
                "Optimized distance    : %.2f meters%n",
                optimizedDistance
        );

        System.out.printf(
                "Distance saved        : %.2f meters%n",
                distanceSaved
        );

        System.out.printf(
                "Distance improvement  : %.2f%%%n",
                distanceImprovement
        );

        System.out.println();

        System.out.printf(
                "Initial duration      : %.2f seconds%n",
                initialDuration
        );

        System.out.printf(
                "Optimized duration    : %.2f seconds%n",
                optimizedDuration
        );

        System.out.printf(
                "Time saved            : %.2f seconds%n",
                durationSaved
        );

        System.out.printf(
                "Time improvement      : %.2f%%%n",
                durationImprovement
        );

        System.out.println();

        System.out.println(
                "Initial assignments   : "
                        + initialAssignments
        );

        System.out.println(
                "Optimized assignments : "
                        + optimizedAssignments
        );

        System.out.println();

        System.out.println(
                "Total required food   : "
                        + totalRequiredFood
                        + " meals"
        );

        System.out.println(
                "Total delivered food  : "
                        + totalDeliveredFood
                        + " meals"
        );

        System.out.println(
                "Unmet demand          : "
                        + unmetDemand
                        + " meals"
        );

        System.out.printf(
                "Demand fulfillment    : %.2f%%%n",
                demandFulfillment
        );

        System.out.println();

        System.out.printf(
                "Final objective cost  : %.2f%n",
                objectiveCost
        );

        System.out.println(
                "==========================================="
        );
    }
}
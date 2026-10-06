package geosmart.route;

public class TravelMatrixTest {

    public static void main(String[] args) {

        TravelMatrix matrix =
                new TravelMatrix(
                        "data/travel_matrix.csv"
                );

        double distance =
                matrix.getDistance("L1", "L3");

        double duration =
                matrix.getDuration("L1", "L3");

        System.out.println(
                "L1 → L3"
        );

        System.out.println(
                "Distance: "
                        + distance
                        + " meters"
        );

        System.out.println(
                "Duration: "
                        + duration
                        + " seconds"
        );
    }
}
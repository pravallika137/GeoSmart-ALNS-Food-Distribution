package geosmart.route;

import java.util.List;

public class DonorTest {

    public static void main(String[] args) {

        List<Donor> donors =
                DonorLoader.loadDonors(
                        "data/donors.csv"
                );

        System.out.println(
                "Total donors: "
                        + donors.size()
        );

        for (Donor donor : donors) {

            System.out.println(donor);
        }
    }
}
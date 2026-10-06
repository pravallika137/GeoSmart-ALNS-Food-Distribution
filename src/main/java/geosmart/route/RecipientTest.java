package geosmart.route;

import java.util.List;

public class RecipientTest {

    public static void main(String[] args) {

        List<Recipient> recipients =
                RecipientLoader.loadRecipients(
                        "data/recipients.csv"
                );

        System.out.println(
                "Total recipients: "
                        + recipients.size()
        );

        for (Recipient recipient : recipients) {

            System.out.println(recipient);
        }
    }
}
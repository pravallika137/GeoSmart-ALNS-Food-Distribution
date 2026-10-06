package geosmart.route;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm");

    public static String addSeconds(
            String time,
            double seconds) {

        LocalTime localTime =
                LocalTime.parse(time, FORMATTER);

        long wholeSeconds =
                Math.round(seconds);

        LocalTime newTime =
                localTime.plusSeconds(wholeSeconds);

        return newTime.format(FORMATTER);
    }
}
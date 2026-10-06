package geosmart.route;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OSRMRouteService {

    private static final String OSRM_BASE_URL =
            "https://router.project-osrm.org/route/v1/driving/";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OSRMRouteService() {
        httpClient = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();
    }

    public RouteResult getRoute(Location from, Location to) {

        String url = OSRM_BASE_URL
                + from.getLongitude() + "," + from.getLatitude()
                + ";"
                + to.getLongitude() + "," + to.getLatitude()
                + "?overview=false";

        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    httpClient.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            JsonNode root = objectMapper.readTree(response.body());

            String code = root.get("code").asText();

            if (!"Ok".equals(code)) {
                throw new RuntimeException(
                        "OSRM request failed: " + code
                );
            }

            JsonNode route = root.get("routes").get(0);

            double distance = route.get("distance").asDouble();
            double duration = route.get("duration").asDouble();

            return new RouteResult(distance, duration);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error while getting route from OSRM",
                    e
            );
        }
    }
    public static void main(String[] args) {

        var locations = LocationDataReader.readLocations(
                "data/locations.csv"
        );

        Location l1 = locations.get(0);
        Location l2 = locations.get(1);

        OSRMRouteService routeService = new OSRMRouteService();

        RouteResult result = routeService.getRoute(l1, l2);

        System.out.println("From: " + l1.getName());
        System.out.println("To: " + l2.getName());
        System.out.println(result);
    }
}
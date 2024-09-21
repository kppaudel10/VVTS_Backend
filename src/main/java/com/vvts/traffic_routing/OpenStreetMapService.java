package com.vvts.traffic_routing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenStreetMapService {
    private static final String OSRM_API_URL = "http://localhost:5000/route/v1/driving/";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;

    public OpenStreetMapService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String findShortestRoute(String origin, String destination) throws Exception {
        // Example origin and destination format: "lon1,lat1;lon2,lat2"
        String coordinates = origin + ";" + destination;
        String url = OSRM_API_URL + coordinates + "?overview=full&geometries=polyline";

        // Make the HTTP request to the OSRM API
        String response = restTemplate.getForObject(url, String.class);

        // Parse the JSON response to extract route information
        JsonNode jsonNode = objectMapper.readTree(response);
        JsonNode routes = jsonNode.get("routes");

        if (routes.isArray() && routes.size() > 0) {
            // Extract the first route's polyline (shortest route)
            JsonNode route = routes.get(0);
            return route.get("geometry").asText();  // This contains the encoded polyline for the route
        }
        return null;  // No route found
    }
}

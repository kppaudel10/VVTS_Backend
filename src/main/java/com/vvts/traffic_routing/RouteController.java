package com.vvts.traffic_routing;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shortest-route")
public class RouteController {
    private final OpenStreetMapService openStreetMapService;

    public RouteController(OpenStreetMapService openStreetMapService) {
        this.openStreetMapService = openStreetMapService;
    }

    @GetMapping("/find")
    public String getShortestRoute(@RequestParam String origin, @RequestParam String destination) {
        try {
            return openStreetMapService.findShortestRoute(origin, destination);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}

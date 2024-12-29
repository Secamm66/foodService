package ru.ershov.project.deliveryservice.util;

public class DistanceCalculator {

    private static final double EARTH_RADIUS = 6371.0;

    public static Double calculateDistance(String startCoordinates, String endCoordinates) {

        double startLat = Double.parseDouble(startCoordinates.split(",")[0]);
        double startLon = Double.parseDouble(startCoordinates.split(",")[1]);
        double endLat = Double.parseDouble(endCoordinates.split(",")[0]);
        double endLon = Double.parseDouble(endCoordinates.split(",")[1]);

        double startLatRad = Math.toRadians(startLat);
        double startLonRad = Math.toRadians(startLon);
        double endLatRad = Math.toRadians(endLat);
        double endLonRad = Math.toRadians(endLon);

        double dLat = endLatRad - startLatRad;
        double dLon = endLonRad - startLonRad;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(startLatRad) * Math.cos(endLatRad) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = EARTH_RADIUS * c;

        return Math.round(distance * 100.0) / 100.0;
    }
}
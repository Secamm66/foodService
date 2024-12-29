package ru.ershov.project.orderservice.util;

import java.util.Locale;
import java.util.Random;

public class RandomCoordinatesGenerator {

    private static final double MIN_LATITUDE = 56.1000;
    private static final double MAX_LATITUDE = 56.5000;
    private static final double MIN_LONGITUDE = 43.5000;
    private static final double MAX_LONGITUDE = 44.5000;

    private static final Random random = new Random();

    public static String generateCoordinates() {
        double latitude = MIN_LATITUDE + (MAX_LATITUDE - MIN_LATITUDE) * random.nextDouble();
        double longitude = MIN_LONGITUDE + (MAX_LONGITUDE - MIN_LONGITUDE) * random.nextDouble();

        String formattedLatitude = String.format(Locale.US, "%.4f", latitude);
        String formattedLongitude = String.format(Locale.US, "%.4f", longitude);

        return formattedLatitude + "," + formattedLongitude;
    }
}
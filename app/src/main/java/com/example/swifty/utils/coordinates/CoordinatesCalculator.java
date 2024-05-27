package com.example.swifty.utils.coordinates;

/*
* This class is a utility class for calculating the midpoint between two coordinates.
* It has two static methods: calculateMidpoint and calculateDistance.
* */

public class CoordinatesCalculator {

    public static double[] calculateMidpoint(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Haversine formula
        double dLon = lon2Rad - lon1Rad;
        double Bx = Math.cos(lat2Rad) * Math.cos(dLon);
        double By = Math.cos(lat2Rad) * Math.sin(dLon);
        double lat3Rad = Math.atan2(Math.sin(lat1Rad) + Math.sin(lat2Rad), Math.sqrt((Math.cos(lat1Rad) + Bx) * (Math.cos(lat1Rad) + Bx) + By * By));
        double lon3Rad = lon1Rad + Math.atan2(By, Math.cos(lat1Rad) + Bx);

        return new double[]{Math.toDegrees(lat3Rad), Math.toDegrees(lon3Rad)};
    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in kilometers

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}

package app.utils;

import app.polygon.Coordinate;

/**
 * Created by ahmetkucuk on 01/10/15.
 */
public class Utilities {

    private static final String DELIMETER_COOR = ",";
    private static final String DELIMETER_POINT = " ";


    public static Coordinate[] parseCoordinatesString(String coordinateString) {
        String[] numbers = coordinateString.split(DELIMETER_COOR);
        Coordinate[] coordinates = new Coordinate[numbers.length];

        for(int i = 0; i < numbers.length; i++) {
            String[] coordinate = numbers[i].split(DELIMETER_POINT);
            coordinates[i] = new Coordinate(Double.parseDouble(coordinate[0]), Double.parseDouble(coordinate[1]));
        }

        return coordinates;
    }

    public static int calculatePixelValues(EventType eventType, double value) {
        return (int)(((4096/2) + value) / eventType.getCDELT());
    }
}

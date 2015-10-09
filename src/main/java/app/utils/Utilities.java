package app.utils;

import app.models.Coordinate;
import app.models.EventType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

    public static String executeCommand(String[] cmd) {

        String s = null;

        try {

            Process p = Runtime.getRuntime().exec(cmd);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

        } catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
        }
        return s;
    }
}

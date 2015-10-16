package app.utils;

import app.models.Coordinate;
import app.models.Event;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public static Date getDateFromString(String dateString) {

        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-DD'T'HH:mm:ss");

        try {
            Date date = formatter.parse(dateString);
            return date;
        } catch (ParseException e) {
            //e.printStackTrace();
        }
        return null;
    }

    public static String getStringFromDate(Date date) {

        SimpleDateFormat formatter1 = new SimpleDateFormat("YYYY-MM-DD'T'HH:mm:ss'Z'");
        //SimpleDateFormat formatter2 = new SimpleDateFormat("HH:MM:SS");
        return formatter1.format(date);
    }

    public static List<Event> getEventByTime(String inputFile, String eventTime) throws ParseException {

        List result = new ArrayList();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("DD/MM/YYYY HH:mm:ss");
        Date date = dateFormatter.parse(eventTime);
        System.out.println(date);
        EventFileReader.init(inputFile);
        Event event = null;
        while((event = EventFileReader.getInstance().next()) != null) {
            if(event.getStartDate().getTime() == date.getTime()) {
                System.out.println("Time: " + event);
                result.add(event);
            }
        }

        return result;
    }

    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
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

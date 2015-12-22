package app.utils;

import app.models.Coordinate;
import app.models.Event;
import app.models.EventType;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ahmetkucuk on 01/10/15.
 */
public class Utilities {

    private static final String DELIMETER_COOR = ",";
    private static final String DELIMETER_POINT = " ";


    public static Set<String> getDownloadedFileNames(String fileName) {


        FileInputStream fStream1 = null;
        BufferedReader reader = null;
        Set<String> set = new HashSet<>();
        if(!isFileExists(fileName)) return set;

        try {
            fStream1 = new FileInputStream(fileName);
            DataInputStream in = new DataInputStream(fStream1);
            reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while((line = reader.readLine()) != null) {
                set.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return set;
    }

    public static boolean isFileExists(String filePathString) {
        File f = new File(filePathString);
        return f.exists() && !f.isDirectory();
    }

    public static Coordinate[] parseCoordinatesString(String coordinateString) {
        String[] numbers = coordinateString.split(DELIMETER_COOR);
        Coordinate[] coordinates = new Coordinate[numbers.length];

        for(int i = 0; i < numbers.length; i++) {
            String[] coordinate = numbers[i].split(DELIMETER_POINT);
            coordinates[i] = new Coordinate(Double.parseDouble(coordinate[0]), Double.parseDouble(coordinate[1]));
        }

        return coordinates;
    }

    public static Date getDateFromString(String dateString) throws ParseException{

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd'T'HH:mm:ss");
        Date date = formatter.parse(dateString);
        return date;
    }

    public static String getImageSubPath(Date date, String measurement) {
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
        return "SDO/AIA/" + measurement + "/" + formatter1.format(date) + "/";
    }

    public static String getStringFromDate(Date date) {

        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        //SimpleDateFormat formatter2 = new SimpleDateFormat("HH:MM:SS");
        return formatter1.format(date);
    }

    public static List<Event> getEventByTime(String inputFile, String eventTime) throws ParseException {

        List result = new ArrayList();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = dateFormatter.parse(eventTime);
        EventReader reader = new EventReader(inputFile);
        Event event = null;
        while((event = reader.next()) != null) {
            if(event.getStartDate().getTime() == date.getTime()) {
                result.add(event);
            }
        }

        return result;
    }

    public static Event getEventById(String inputFile, int id) {

        EventReader reader = new EventReader(inputFile);
        Event event;
        while((event = reader.next()) != null) {
            if(event.getId() == id)
                break;
        }

        return event;
    }


    static final Map<String, Integer> map = new HashMap<>();
    static final Map<String, Integer> mapofPossibleInputs = new HashMap<>();

    public static int mapToPrimaryMeasurement(String measurement, EventType eventType) {
        if(map.size() == 0)  {

            map.put("AIA 171, AIA 193", 171);
            map.put("AR", EventType.AR.getMeasurement());
            map.put("CH", EventType.CH.getMeasurement());
            map.put("SG", EventType.SG.getMeasurement());
            map.put("FL", EventType.FL.getMeasurement());
            map.put("AIA 193", 193);
            map.put("131_THIN", 131);
            map.put("94_THIN", 94);
            map.put("131_THICK", 131);
            map.put("94_THICK", 94);
        }
        return (map.containsKey(measurement)) ? map.get(measurement) : eventType.getMeasurement();

    }

    public static String polygonToString(Event event) {
        String pixelPolygon = "";
        Coordinate[] coordinates = event.getCoordinates();
        for(int i = 0; i < coordinates.length; i++) {
            pixelPolygon = (i != 0) ? pixelPolygon + "," + coordinates[i].toString() : coordinates[i].toString();
        }
        return pixelPolygon;
    }

    public static String eventToString(Event event) {

        String pixelPolygon = polygonToString(event);
        int measurement = isNumeric(event.getMeasurement()) ? Integer.parseInt(event.getMeasurement()) : mapToPrimaryMeasurement(event.getMeasurement(), event.getEventType());
        return event.getId() + "\t" + event.getEventType().toString() + "\t" + event.getStartDateString() + "\t" + event.getEndDateString() + "\t" + measurement + "\t" + pixelPolygon.trim() + "\n";
    }

    public static String secondaryEventToString(Event event) {

        String pixelPolygon = polygonToString(event);
        return event.getId() + "\t" + event.getEventType().toString() + "\t" + event.getStartDateString() + "\t" + event.getEndDateString() + "\t" + event.getEventType().getSecondaryMeasurement() + "\t" + pixelPolygon.trim() + "\n";
    }

    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    public static boolean hasRealMeasurementValue(String measurement) {
        if(isNumeric(measurement)) return true;

        if(map.size() == 0)  {

            mapofPossibleInputs.put("AIA 171, AIA 193", 171);
            mapofPossibleInputs.put("AIA 193", 193);
            mapofPossibleInputs.put("131_THIN", 131);
            mapofPossibleInputs.put("94_THIN", 94);
            mapofPossibleInputs.put("131_THICK", 131);
            mapofPossibleInputs.put("94_THICK", 94);

        }
        return mapofPossibleInputs.containsKey(measurement);
    }
}

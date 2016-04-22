package app.utils;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

    public static Geometry parseCoordinatesString(String coordinateString) {

        WKTReader reader = new WKTReader();

        try {
            Geometry g = reader.read(coordinateString);
            return g;

        } catch (com.vividsolutions.jts.io.ParseException e) {
            e.printStackTrace();
        }

        return null;
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

    public static String getImageSubPath(String dateString, String measurement) throws ParseException {
        Date date = getDateFromString(dateString);
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
        return "SDO/AIA/" + measurement + "/" + formatter1.format(date) + "/";
    }

    public static String getStringFromDate(Date date) {

        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        //SimpleDateFormat formatter2 = new SimpleDateFormat("HH:MM:SS");
        return formatter1.format(date);
    }

    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

}

package app;

import app.polygon.DrawPolygonOnImage;
import app.utils.Constants;
import app.utils.EventType;

/**
 * Created by ahmetkucuk on 27/09/15.
 */
public class Runner {

    //Directory support should be added.
    public static final String INPUT_FILE_NAME = "/Users/ahmetkucuk/Documents/Research/DNNProject/formattedop/FL/RECORD/FL_Records.txt";

    public static void main(String[] args) throws Exception {
//        new JP2Downloader().downloadFromInputFile(INPUT_FILE_NAME, Constants.JP2_IMAGE_FILE_LOCATION, 5, 0);
        Event event = new Event();
        event.setEventType(EventType.FL);
        event.setCoordinateString("72.300664 8.155728,89.27228 8.720186,89.041483 12.962522,75.414519 12.913597,72.300664 8.155728");
        event.setImageFileString(Constants.JPEG_IMAGE_FILE_LOCATION + "FL_1_date_2012-01-03T22-09-27Z_cid_131.jpg");
        event.setDate("2012-01-03T22:09:27");

        new DrawPolygonOnImage().drawPolygon(event);
    }
}

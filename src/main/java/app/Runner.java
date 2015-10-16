package app;

import app.core.DrawPolygonOnImage;
import app.core.JP2Downloader;
import app.models.EventType;
import app.service.DrawPolygonService;
import app.service.JP2DownloaderService;
import app.utils.Constants;

/**
 * Created by ahmetkucuk on 27/09/15.
 */
public class Runner {

    //Directory support should be added.
    public static final String INPUT_FILE_NAME = "/Users/ahmetkucuk/Documents/Research/DNNProject/formattedop/SG/RECORD/SG_Records.txt";

    public static final String INPUT_FILE_NAME_META = "/Users/ahmetkucuk/Documents/Research/DNNProject/formattedop/%s/RECORD/%s_Records.txt";

    public static void main(String[] args) throws Exception {

//        System.out.println(CoordinateSystemConverter.convertHPCToPixXY(new Coordinate(-39.662966, -28.024797)));
//        System.out.println(CoordinateSystemConverter.convertHPCToPixXY(new Coordinate(-27.6345, -15.292138)));

//        System.out.println(Utilities.getDateFromString("2012-01-02T00:00:00"));

        EventType eventType = EventType.AR;
        int limit = 5;
        int waitBetween = 10;

//        downloadByEventType(eventType, "S", limit, waitBetween);
//        downloadByEventType(eventType, "M", limit, waitBetween);
//        downloadByEventType(eventType, "E", limit, waitBetween);

        drawByEventType(eventType, "S", limit);
        drawByEventType(eventType, "M", limit);
        drawByEventType(eventType, "E", limit);


    }


    /**
     * This method is implemented for test purpose
     */
    public static void downloadByEventType(EventType eventType, String eventTimeType, int limit, int waitBetween) {
        new JP2Downloader().downloadFromInputFile(String.format(Constants.INPUT_FILE_NAME_META, eventType.toString(), eventType.toString()),
                eventTimeType, String.format(Constants.OUTPUT_FILE_NAME_META, eventType.toString()), limit, waitBetween);
    }

    public static void drawByEventType(EventType eventType, String eventTimeType, int limit) {
        new DrawPolygonOnImage().draw(String.format(Constants.INPUT_FILE_NAME_META, eventType.toString(), eventType.toString()),
                eventTimeType, String.format(Constants.JPEG_IMAGE_FILE_LOCATION_META, eventType.toString()), limit);
    }
}

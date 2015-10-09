package app;

import app.downloader.JP2Downloader;
import app.polygon.DrawPolygonOnImage;
import app.utils.Constants;
import app.utils.Utilities;

/**
 * Created by ahmetkucuk on 27/09/15.
 */
public class Runner {

    //Directory support should be added.
    public static final String INPUT_FILE_NAME = "/Users/ahmetkucuk/Documents/Research/DNNProject/formattedop/FL/RECORD/FL_Records.txt";

    public static void main(String[] args) throws Exception {

//        System.out.println(Utilities.getDateFromString("2012-01-02T00:00:00"));
//        new JP2Downloader().downloadFromInputFile(INPUT_FILE_NAME, Constants.JP2_IMAGE_FILE_LOCATION, 5, 10);
        new DrawPolygonOnImage().draw(INPUT_FILE_NAME, "S", Constants.JPEG_IMAGE_FILE_LOCATION, 5, "jpg");
        new DrawPolygonOnImage().draw(INPUT_FILE_NAME, "M", Constants.JPEG_IMAGE_FILE_LOCATION, 5, "jpg");
        new DrawPolygonOnImage().draw(INPUT_FILE_NAME, "E", Constants.JPEG_IMAGE_FILE_LOCATION, 5, "jpg");
    }
}

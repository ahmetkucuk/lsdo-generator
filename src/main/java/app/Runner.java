package app;

import app.downloader.JP2Downloader;
import app.polygon.DrawPolygonOnImage;
import app.utils.Constants;

/**
 * Created by ahmetkucuk on 27/09/15.
 */
public class Runner {

    //Directory support should be added.
    public static final String INPUT_FILE_NAME = "/Users/ahmetkucuk/Documents/Research/DNNProject/formattedop/FL/RECORD/FL_Records.txt";

    public static void main(String[] args) throws Exception {

//        new JP2Downloader().downloadFromInputFile(INPUT_FILE_NAME, Constants.JP2_IMAGE_FILE_LOCATION, 5, 0);
        new DrawPolygonOnImage().draw(INPUT_FILE_NAME, Constants.JPEG_IMAGE_FILE_LOCATION, 5, "png");
    }
}

package app.utils;

/**
 * Created by ahmetkucuk on 27/09/15.
 */
public class Constants {



    public static final String IMAGE_DOWNLOAD_URL = "http://gs671-suske.ndc.nasa.gov/api/v1/getJP2Image/?date=%s&instrument=AIA&observatory=SDO&detector=AIA&measurement=%s";
    public static final String IMAGE_JPIP_URI_DOWNLOAD_URL = "http://gs671-suske.ndc.nasa.gov/api/v1/getJP2Image/?date=%s&instrument=AIA&observatory=SDO&detector=AIA&measurement=%s&jpip=true";


    String convertImages = "sips -s format jpeg ./*.jp2 --out jpegs";
}

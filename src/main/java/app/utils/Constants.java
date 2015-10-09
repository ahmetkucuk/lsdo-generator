package app.utils;

/**
 * Created by ahmetkucuk on 27/09/15.
 */
public class Constants {

    public static class FieldNames {

        public static final String START_DATE = "event_starttime";
        public static final String END_DATE = "event_endtime";
        public static final String CHANNEL_ID = "obs_channelid";
        public static final String POLYGON = "hgs_bbox";
        public static final String EVENT_TYPE = "event_type";
    }

    public static class CDELT {
        public static final double AR_CDELT = 0.599733;
        public static final double CH_CDELT = 0.599733;//Not sure about that
        public static final double FL_CDELT = 0.599733;
        public static final double SG_CDELT = 0.599733;
    }

    public static class Measurement {
        public static final int AR_ME = 193;
        public static final int CH_ME = 193;//Not sure about that
        public static final int FL_ME = 131;
        public static final int SG_ME = 131;
    }

    public static final String JP2_IMAGE_FILE_LOCATION = "/Users/ahmetkucuk/Documents/Developer/java/QueryHEK/Image/";
    public static final String JPEG_IMAGE_FILE_LOCATION = "/Users/ahmetkucuk/Documents/Developer/java/QueryHEK/Image/jpegs/";

    public static final String IMAGE_DOWNLOAD_URL = "http://gs671-suske.ndc.nasa.gov/api/v1/getJP2Image/?date=%s&instrument=AIA&observatory=SDO&detector=AIA&measurement=%s";



    String goToDir = "cd " + Constants.JP2_IMAGE_FILE_LOCATION;
    String convertImages = "sips -s format jpeg ./*.jp2 --out jpegs";
}

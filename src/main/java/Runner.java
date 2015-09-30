/**
 * Created by ahmetkucuk on 27/09/15.
 */
public class Runner {

    //Directory support should be added.
    public static final String INPUT_FILE_NAME = "/Users/ahmetkucuk/Documents/Research/DNNProject/formattedop/FL/RECORD/FL_Records.txt";

    public static void main(String[] args) throws Exception {
//        new Downloader().downloadFromInputFile(INPUT_FILE_NAME, Constants.IMAGE_FILE_LOCATION, 5, 0);
        new DrawPolygon().drawPolygon(Constants.IMAGE_FILE_LOCATION + "sample.jp2", "POLYGON((20 30,200 350,250 400,300 250,20 30))");
    }
}

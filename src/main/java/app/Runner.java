package app;

import app.models.Tuple2;
import app.service.JP2DownloaderService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmetkucuk on 27/09/15.
 */
public class Runner {

    public static final String EVENT_INPUT_FILE = "/Users/ahmetkucuk/Documents/Research/EVENTS_DATASET/HPC/Final_Data/Primary/primary.txt";
    public static final String FINAL_DATA_IMAGE_OUTPUT = "/Users/ahmetkucuk/Documents/";

    static String[] localArguments = new String[] {EVENT_INPUT_FILE, FINAL_DATA_IMAGE_OUTPUT, "5", "45630", "-p"};

    public static boolean isLocal = true;

    public static void main(String[] args) throws Exception {


        List<Tuple2<Integer, String>> listToDownload = new ArrayList<>();

        listToDownload.add(new Tuple2<>(171, "2014-01-01T23:59:59Z"));
        new JP2DownloaderService().downloadImageList(listToDownload, FINAL_DATA_IMAGE_OUTPUT, 10, 0, false);

//        isLocal = isMac(System.getProperty("os.name").toLowerCase());
//
//        if(isLocal) {
//            downloadImages(localArguments);
//        } else {
//            downloadImages(args);
//        }
    }


    public static boolean isParallel(String[] arguments) {
        if(arguments != null && arguments.length == 5) {
            if(arguments[4].equalsIgnoreCase("-p")) return true;
        }
        return false;
    }

    public static boolean isMac(String osName) {

        return (osName.indexOf("mac") >= 0);

    }

    public static void downloadFinalImages() {

//        new JP2DownloaderService().downloadImageFromFile(FINAL_DATA_OUTPUT_WITH_FILE_NAME, "S", FINAL_DATA_IMAGE_OUTPUT, 100, 150000);

    }


}

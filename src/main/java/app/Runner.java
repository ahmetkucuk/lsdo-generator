package app;

import app.service.DrawPolygonService;
import app.service.JP2DownloaderService;

/**
 * Created by ahmetkucuk on 27/09/15.
 */
public class Runner {

    public static final String EVENT_INPUT_FILE = "/Users/ahmetkucuk/Documents/Research/EVENTS_DATASET/HPC/Final_Data/Primary/primary.txt";
    public static final String EVENT_SECONDARY_INPUT_FILE = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/events_secondary.txt";
    public static final String FINAL_DATA_IMAGE_OUTPUT = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/images/";
    public static final String JPEG_IMAGES_DIRECTORY = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/images/SDO/jpegs/";

    static String[] localArguments = new String[] {EVENT_INPUT_FILE, FINAL_DATA_IMAGE_OUTPUT, "5", "45630", "-p"};

    public static boolean isLocal = true;

    public static void main(String[] args) throws Exception {


        //drawImages(EVENT_INPUT_FILE, "2014_11_22__07_59_47_34__SDO_AIA_AIA_171", JPEG_IMAGES_DIRECTORY);


//        isLocal = isMac(System.getProperty("os.name").toLowerCase());
//
//        if(isLocal) {
//            downloadImages(localArguments);
//        } else {
//            downloadImages(args);
//        }
    }


    public static void downloadImages(String[] args) {

        System.out.println("Start with args: \t" + args[0] + "\t" + args[1] + "\t" + args[2] + "\t" + args[3]);
        long startTime = System.currentTimeMillis();
        new JP2DownloaderService().downloadImageFromFile(args[0], args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]), isParallel(args));
        long endTime = System.currentTimeMillis() - startTime;
        System.out.println("End. Duration: " + (endTime/ (1000 * 60)));
    }

    public static void drawImages(String inputFile, String imageFileName, String imageDirectory) {
        new DrawPolygonService().drawByEventImageName(inputFile, imageFileName, imageDirectory);
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

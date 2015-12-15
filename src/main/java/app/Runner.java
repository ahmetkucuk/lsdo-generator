package app;

import app.service.JP2DownloaderService;

/**
 * Created by ahmetkucuk on 27/09/15.
 */
public class Runner {

    public static final String EVENT_INPUT_FILE = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/events.txt";
    public static final String EVENT_SECONDARY_INPUT_FILE = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/events_secondary.txt";
    public static final String FINAL_DATA_IMAGE_OUTPUT = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/images/";

    static String[] localArguments = new String[] {EVENT_SECONDARY_INPUT_FILE, FINAL_DATA_IMAGE_OUTPUT, "10", "0"};

    public static boolean isLocal = true;

    public static void main(String[] args) throws Exception {

        isLocal = isMac(System.getProperty("os.name").toLowerCase());

        if(isLocal) {
            args = localArguments;
        }

        System.out.println("Start with args: \t" + args[0] + "\t" + args[1] + "\t" + args[2] + "\t" + args[3]);
        long startTime = System.currentTimeMillis();
        new JP2DownloaderService().downloadImageFromFile(args[0], args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]), 0);
        long endTime = System.currentTimeMillis() - startTime;
        System.out.println("End. Duration: " + (endTime/ (1000 * 60)));
    }

    public static boolean isMac(String osName) {

        return (osName.indexOf("mac") >= 0);

    }

    public static void downloadFinalImages() {

//        new JP2DownloaderService().downloadImageFromFile(FINAL_DATA_OUTPUT_WITH_FILE_NAME, "S", FINAL_DATA_IMAGE_OUTPUT, 100, 150000);

    }


}

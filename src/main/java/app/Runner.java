package app;

import app.core.DrawPolygonOnImage;
import app.core.JP2Downloader;
import app.core.URIFinder;
import app.models.Coordinate;
import app.models.Event;
import app.models.EventType;
import app.service.clean.BadRecordCleaner;
import app.service.JP2DownloaderService;
import app.service.clean.RecordCleaner;
import app.utils.*;
import org.apache.log4j.Logger;

import java.awt.*;
import java.text.ParseException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by ahmetkucuk on 27/09/15.
 */
public class Runner {

    public static final String EVENT_INPUT_FILE = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/events.txt";
    public static final String EVENT_SECONDARY_INPUT_FILE = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/events_secondary.txt";
    public static final String FINAL_DATA_IMAGE_OUTPUT = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/images/";

    static String[] arg = new String[] {EVENT_INPUT_FILE, FINAL_DATA_IMAGE_OUTPUT, "100", "10000"};
    public static void main(String[] args) throws Exception {

        long startTime = System.currentTimeMillis();
        new JP2DownloaderService().downloadImageFromFile(args[0], args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        long endTime = System.currentTimeMillis() - startTime;
        System.out.println("Duration: " + (endTime/ (1000 * 60)));
    }

    public static void downloadFinalImages() {

//        new JP2DownloaderService().downloadImageFromFile(FINAL_DATA_OUTPUT_WITH_FILE_NAME, "S", FINAL_DATA_IMAGE_OUTPUT, 100, 150000);

    }


}

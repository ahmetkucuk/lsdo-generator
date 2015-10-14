package app.service;

import app.core.JP2Downloader;
import app.models.Event;
import app.utils.Utilities;

import java.text.ParseException;

/**
 * Created by ahmetkucuk on 13/10/15.
 */
public class JP2DownloaderService {

    public void downloadImageFromFile(String inputFile, String outputFileDir, int numberOfItemToDownload) {

        new JP2Downloader().downloadFromInputFile(inputFile, outputFileDir, numberOfItemToDownload, 10);

    }

    public void downloadByTime(String inputFile, String outputfileLocaiton, String time) {

        Event event = null;
        try {
            event = Utilities.getEventByTime(inputFile, time);
            if(event != null) {
                new JP2Downloader().downloadForEvent(event, "S", outputfileLocaiton);
            } else {
                System.out.println("Could not find event for time: " + time);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}

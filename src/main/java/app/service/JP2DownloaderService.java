package app.service;

import app.core.JP2Downloader;
import app.models.Event;
import app.utils.Utilities;

import java.text.ParseException;
import java.util.List;

/**
 * Created by ahmetkucuk on 13/10/15.
 */
public class JP2DownloaderService {

    public void downloadImageFromFile(String inputFile, String eventTimeType, String outputFileDir, int numberOfItemToDownload, int offset) {

        new JP2Downloader().downloadFromInputFile(inputFile, eventTimeType, outputFileDir, numberOfItemToDownload, offset, 10);

    }

    public void downloadByTime(String inputFile, String outputFileLocation, String time) {

        try {
            List<Event> events = Utilities.getEventByTime(inputFile, time);
            if(events.size() == 0) {
                if(events.size() > 1) {
                    System.out.println("More than one event found");
                }
                new JP2Downloader().downloadForEvent(events.get(0), "S", outputFileLocation);
            } else {
                System.out.println("Could not find event for time: " + time);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}

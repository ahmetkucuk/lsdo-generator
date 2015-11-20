package app.service;

import app.core.JP2Downloader;
import app.models.Event;
import app.utils.EventReader;
import app.utils.Utilities;

import java.text.ParseException;
import java.util.List;

/**
 * Created by ahmetkucuk on 13/10/15.
 */
public class JP2DownloaderService {

    public void downloadImageFromFile(String inputFile, String outputFileDir, int numberOfItemToDownload, int offset) {

        new JP2Downloader().downloadFromInputFile(inputFile, outputFileDir, numberOfItemToDownload, offset, 10);

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

    public void downloadById(String inputFile, String outputFileLocation, int id) {

        EventReader reader = new EventReader(inputFile);
        Event event = null;
        while((event = reader.next()) != null) {
            if(event.getId() == id)
                break;
        }

        if(event != null) {
            new JP2Downloader().downloadForEvent(event, "S", outputFileLocation);
        } else {
            System.out.println("Could not find event for time: " + id);
        }
    }

}

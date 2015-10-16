package app.service;

import app.core.DrawPolygonOnImage;
import app.core.JP2Downloader;
import app.models.Event;
import app.utils.Constants;
import app.utils.Utilities;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ahmetkucuk on 13/10/15.
 */
public class DrawPolygonService {

    public void drawByFileName(String inputFile, String eventTimeType, String outputFile, int numberOfImageToDraw) {
        new DrawPolygonOnImage().draw(inputFile, eventTimeType, outputFile, numberOfImageToDraw);
    }

    public void drawByEventTime(String inputFile, String eventTimeType, String outputFileDirectory, String time) {

        try {
            List<Event> events = Utilities.getEventByTime(inputFile, time);
            if(events.size() != 0) {
                if(events.size() > 1) {
                    System.out.println("More than one event found");
                }
                for(Event e: events) {
                    System.out.println(Arrays.toString(e.getCoordinates()));
                }
                new DrawPolygonOnImage().drawPolygon(events.get(0), eventTimeType, outputFileDirectory);
            } else {
                System.out.println("Could not find event for time: " + time);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}

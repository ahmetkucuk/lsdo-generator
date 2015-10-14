package app.service;

import app.core.DrawPolygonOnImage;
import app.core.JP2Downloader;
import app.models.Event;
import app.utils.Constants;
import app.utils.Utilities;

import java.text.ParseException;
import java.util.Arrays;

/**
 * Created by ahmetkucuk on 13/10/15.
 */
public class DrawPolygonService {

    public void drawByFileName(String inputFile, String outputFile, int numberOfImageToDraw) {
        new DrawPolygonOnImage().draw(inputFile, "S", outputFile, numberOfImageToDraw, "jpg");
        new DrawPolygonOnImage().draw(inputFile, "M", outputFile, numberOfImageToDraw, "jpg");
        new DrawPolygonOnImage().draw(inputFile, "E", outputFile, numberOfImageToDraw, "jpg");
    }

    public void drawByEventTime(String inputFile, String outputFileDirectory, String time) {

        Event event = null;
        try {
            event = Utilities.getEventByTime(inputFile, time);
            System.out.println(Arrays.toString(event.getCoordinates()));
            if(event != null) {
                new DrawPolygonOnImage().drawPolygon(event, outputFileDirectory, "jpg");
            } else {
                System.out.println("Could not find event for time: " + time);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}

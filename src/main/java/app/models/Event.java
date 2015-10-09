package app.models;

import app.utils.CoordinateSystemConverter;
import app.utils.Utilities;

import java.util.Arrays;

/**
 * Created by ahmetkucuk on 01/10/15.
 */
public class Event {

    private EventType eventType;
    private String coordinateString;
    private String imageFileString;
    private Coordinate[] coordinates;
    private String date;
    private String measurement;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageFileString() {
        return imageFileString;
    }

    public String getImageFilePNGString() {
        return imageFileString.replace(".jpg", ".png");
    }

    public void setImageFileString(String imageFileString) {
        this.imageFileString = imageFileString;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public String getCoordinateString() {
        return coordinateString;
    }

    public void setCoordinateString(String coordinateString) {
        this.coordinateString = coordinateString;
    }

    public Coordinate[] getCoordinates() {
        if(coordinates == null) {
            if(coordinateString == null) {
                System.err.println("Coordinate String is null");
                return null;
            }
            coordinates = Utilities.parseCoordinatesString(coordinateString);
            Coordinate[] newCoordinates = new Coordinate[coordinates.length];

            int i = 0;
            for(Coordinate c: coordinates) {
                newCoordinates[i] = CoordinateSystemConverter.convertHGSToPixXY(c);
                i++;
            }
            coordinates = newCoordinates;
        }
        return coordinates;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventType=" + eventType +
                ", coordinateString='" + coordinateString + '\'' +
                ", imageFileString='" + imageFileString + '\'' +
                ", coordinates=" + Arrays.toString(coordinates) +
                ", date='" + date + '\'' +
                ", measurement='" + measurement + '\'' +
                '}';
    }
}

package app;

import app.polygon.Coordinate;
import app.utils.EventType;
import app.utils.Utilities;

/**
 * Created by ahmetkucuk on 01/10/15.
 */
public class Event {

    private EventType eventType;
    private String coordinateString;
    private String imageFileString;
    private Coordinate[] coordinates;
    private String date;

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

    public String getCoordinateString() {
        return coordinateString;
    }

    public void setCoordinateString(String coordinateString) {
        this.coordinateString = coordinateString;
    }

    public Coordinate[] getCoordinates() {
        if(coordinates == null) {
            coordinates = Utilities.parseCoordinatesString(coordinateString);
            for(Coordinate c: coordinates) {
                c.setPixelX(Utilities.calculatePixelValues(eventType, c.getX()));
                c.setPixelY(Utilities.calculatePixelValues(eventType, c.getY()));
            }
        }
        return coordinates;
    }

}

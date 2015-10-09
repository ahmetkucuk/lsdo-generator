package app.models;

import app.utils.CoordinateSystemConverter;
import app.utils.Utilities;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by ahmetkucuk on 01/10/15.
 */
public class Event {

    private EventType eventType;
    private String coordinateString;
    private String imageFileString;
    private Coordinate[] coordinates;
    private String startDateString;
    private String endDateString;
    private Date startDate;
    private Date endDate;

    private String measurement;

    public Date getMiddleDate() {
        if(startDate == null || endDate == null) {
            System.err.println("Essential paramaters are null. This will change the behaviour of software");
            return startDate;
        }

        Date middleDate = new Date(startDate.getTime()/2 + endDate.getTime()/2);
        return  middleDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStartDateString() {
        return startDateString;
    }

    public void setStartDateString(String startDate) {
        this.startDateString = startDate;
        this.startDate = Utilities.getDateFromString(startDate);
    }

    public String getEndDateString() {
        return endDateString;
    }

    public void setEndDateString(String endDate) {
        this.endDateString = endDate;
        this.endDate = Utilities.getDateFromString(endDate);
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
                ", measurement='" + measurement + '\'' +
                '}';
    }
}

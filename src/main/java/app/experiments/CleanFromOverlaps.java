package app.experiments;

import app.models.Event;
import app.models.EventType;
import app.utils.CoordinateSystemConverter;
import app.utils.EventReader;
import app.utils.Utilities;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

import java.util.*;

/**
 * Created by ahmet on 2/17/17.
 */
public class CleanFromOverlaps {

    static WKTReader wktReader = new WKTReader();
    Map<EventType, List<Event>> cleanEventTypeListMap = new HashMap<>();

    public CleanFromOverlaps(String inputFile) {

        EventReader reader = new EventReader(inputFile);
        Event event;

        Map<EventType, List<Event>> eventTypeListMap = new HashMap<>();
        while((event = reader.next()) != null) {
            try {
                Geometry polygon = wktReader.read(event.getCoordinateString());

                if(polygon.isValid()) {

                    if(getRatioOfBbox(polygon) < 0.9) continue;

                    String frmString = event.getMeasurement() + "_" + event.getEventType() + "_" + event.getFrm();
                    if (frmString.equalsIgnoreCase("171_AR_SPoCA") ||
                            frmString.equalsIgnoreCase("131_SG_Sigmoid Sniffer") ||
                            frmString.equalsIgnoreCase("131_FL_Flare Detective - Trigger Module") ||
                            frmString.equalsIgnoreCase("193_CH_SPoCA") ) {
                        eventTypeListMap.putIfAbsent(event.getEventType(), new ArrayList<>());
                        eventTypeListMap.get(event.getEventType()).add(event);
                    }
                }

            } catch (com.vividsolutions.jts.io.ParseException e) {
                e.printStackTrace();
            }
        }

        for(EventType eventType: eventTypeListMap.keySet()) {
            cleanEventTypeListMap.put(eventType, removeSTIntersectingEvents(eventTypeListMap.get(eventType)).subList(0, 2000));
        }
    }

    public double getRatioOfBbox(Geometry g) {
        Coordinate[] coordinates = g.getEnvelope().getCoordinates();
        double x1 = coordinates[0].x;
        double y1 = coordinates[0].y;

        double x3 = coordinates[2].x;
        double y3 = coordinates[2].y;
        double w = (x3 - x1);
        double h = (y3 - y1);
        return w > h ? h/w : w/h;

    }

    public Map<EventType, List<Event>> getCleanEventTypeListMap() {
        return cleanEventTypeListMap;
    }

    public List<Event> removeSTIntersectingEvents(List<Event> events) {

        Collections.sort(events, eventAreaComparator);
        List<Event> nonIntersectingEvents = new ArrayList<>();

        for(Event e: events) {
            boolean intersecting = false;
            for(Event nonIntersecting: nonIntersectingEvents) {
                if(isIntersecting(e, nonIntersecting)) {
                    intersecting = true;
                    break;
                }
            }
            if(!intersecting) {
                nonIntersectingEvents.add(e);
            }
        }

        Collections.sort(nonIntersectingEvents, eventAreaComparator);
        return nonIntersectingEvents;
    }

    public boolean isIntersecting(Event e1, Event e2) {
        long e1Start = e1.getStartDate().getTime();
        long e1End = e1.getEndDate().getTime();
        long e2Start = e2.getStartDate().getTime();
        long e2End = e2.getEndDate().getTime();

        //Temporal overlaps not occur
        if(!(e1Start < e2End && e1End > e2Start)) {
            return false;
        }
        Geometry g1 = Utilities.parseCoordinatesString(e1.getCoordinateString());
        Geometry g2 = Utilities.parseCoordinatesString(e2.getCoordinateString());
        return g1.overlaps(g2);

    }

    Comparator<Event> eventAreaComparator =  new Comparator<Event>() {
        @Override
        public int compare(Event o1, Event o2) {
            double o1Area = Utilities.parseCoordinatesString(o1.getCoordinateString()).getArea();
            double o2Area = Utilities.parseCoordinatesString(o2.getCoordinateString()).getArea();
            if (o1Area == o2Area) return 0;
            return o1Area < o2Area ? 1 : -1;
        }
    };
}

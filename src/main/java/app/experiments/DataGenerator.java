package app.experiments;

import app.models.Event;
import app.models.EventType;
import app.utils.CoordinateSystemConverter;
import app.utils.EventReader;
import app.utils.FileWriter;
import app.utils.Utilities;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

import java.util.*;

/**
 * Created by ahmet on 2/17/17.
 */
public class DataGenerator {

    static final String inputFile = "/home/ahmet/Documents/Research/Solar/Solar-Event-Classification/Data/events_primary/primary_event_records.txt";
    String outputFile = "/home/ahmet/Documents/Research/Solar/Solar-Event-Classification/Data/experiment_events.txt";

    static Map<EventType, List<Event>> cleanEventsMap;
    public static void main(String[] args) {
//        cleanEventsMap = new CleanFromOverlaps(inputFile).getCleanEventTypeListMap();
        new QSGenerator(inputFile);
    }
}

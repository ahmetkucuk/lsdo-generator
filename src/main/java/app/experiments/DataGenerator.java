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

    static final String inputFile = "/Users/ahmetkucuk/Documents/Research/Solar_Image_Classification/Data/LSDO/Primary/primary_event_records.txt";
    static final String outputFile = "/Users/ahmetkucuk/Documents/Research/Solar_Image_Classification/Experiment_Data/experiment_events.txt";

    static Map<EventType, List<Event>> cleanEventsMap;
    public static void main(String[] args) {
        cleanEventsMap = new CleanFromOverlaps(inputFile).getCleanEventTypeListMap();
        System.out.println(cleanEventsMap.get(EventType.AR).size());
        cleanEventsMap.put(EventType.QS, new QSGenerator(inputFile).getQSEvents());

        FileWriter writer = new FileWriter(outputFile);
        writer.start();

        for(List<Event> events: cleanEventsMap.values()) {
            for(Event e: events) {
                writer.writeToFile(e.toString() + "\n");
            }
        }
        writer.finish();
    }
}

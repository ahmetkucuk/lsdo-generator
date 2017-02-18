package app.experiments;

import app.models.Event;
import app.models.EventType;
import app.models.TInterval;
import app.utils.EventReader;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;

import java.util.*;

/**
 * Created by ahmet on 2/17/17.
 */
public class QSGenerator {

    Random random = new Random();
    int buffer = 512;
    int imageRadius = 2048 - 256;

    Map<TInterval, List<Event>> intervalEventListMap = new HashMap<>();
    Map<String, Long> imageListMap = new HashMap<>();
    public QSGenerator(String inputFile) {

        EventReader reader = new EventReader(inputFile);
        Event event;

        WKTReader wktReader = new WKTReader();

//        Map<EventType, List<Event>> eventTypeListMap = new HashMap<>();
        while((event = reader.next()) != null) {
            try {
                Geometry polygon = wktReader.read(event.getCoordinateString());

                if(polygon.isValid()) {
                    intervalEventListMap.putIfAbsent(new TInterval(event.getStartDate().getTime(), event.getEndDate().getTime()), new ArrayList<>());
                    intervalEventListMap.get(new TInterval(event.getStartDate().getTime(), event.getEndDate().getTime())).add(event);

                    imageListMap.put(event.getsFileName(), event.getStartDate().getTime());
                    imageListMap.put(event.getmFileName(), event.getMiddleDate().getTime());
                    imageListMap.put(event.geteFileName(), event.getEndDate().getTime());
                }

            } catch (com.vividsolutions.jts.io.ParseException e) {
                e.printStackTrace();
            }
        }

        for(Map.Entry<String, Long> entry: imageListMap.entrySet()) {
            String image = entry.getKey();
            long imageTime = entry.getValue();

            int patchSize = random.nextInt(256 - 32) + 32;
            int nOfTrial = 1000;
            Geometry geometry = null;
            while(nOfTrial > 0) {
                geometry = createRandomGeometry(patchSize);
                if(!checkIfIntersectingWithOtherEvents(geometry, imageTime)) break;
                nOfTrial--;
            }
            System.out.println("Number of trial to find: " + nOfTrial);
            System.out.println(geometry);
        }
    }

    public boolean checkIfIntersectingWithOtherEvents(Geometry geometry, long imageTime) {

        for(Map.Entry<TInterval, List<Event>> eventsEntry: intervalEventListMap.entrySet()) {
            TInterval tInterval = eventsEntry.getKey();
            if(tInterval.contains(imageTime)) {
                for(Event e: eventsEntry.getValue()) {
                    if(e.getGeometry().overlaps(geometry)) return true;
                }
            }
        }
        return false;
    }

    public Geometry createRandomGeometry(int patchSize) {
        double randomDegree = random.nextDouble() * 2;
        int x = (int) (Math.sqrt(imageRadius - patchSize) * Math.cos(randomDegree));
        int y = (int) (Math.sqrt(imageRadius - patchSize) * Math.sin(randomDegree));
        System.out.println("x: " + x + " y: " + y);
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate[] coords = new Coordinate[2];
        coords[0] = new Coordinate(x - patchSize/2, y - patchSize/2);
        coords[1] = new Coordinate(x + patchSize/2, y + patchSize/2);
        Geometry g = geometryFactory.createMultiPoint(coords).getEnvelope();
        return g;
    }
}

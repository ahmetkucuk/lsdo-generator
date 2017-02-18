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
    int imageRadius = 2048;
    int nOfQSForEachWavelength = 1500;

    Map<TInterval, List<Event>> intervalEventListMap = new HashMap<>();
    Map<String, Long> imageListMap = new HashMap<>();

    List<Event> qsEvents = new ArrayList<>();
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
                }

            } catch (com.vividsolutions.jts.io.ParseException e) {
                e.printStackTrace();
            }
        }

        int count131 = 0;
        int count171 = 0;
        int count193 = 0;

        for(Map.Entry<String, Long> entry: imageListMap.entrySet()) {
            String image = entry.getKey();
            long imageTime = entry.getValue();

            if(!(image.contains("131") || image.contains("171") || image.contains("193"))) {
                continue;
            }

            if(count131 >= nOfQSForEachWavelength && image.contains("131")) continue;
            if(count171 >= nOfQSForEachWavelength && image.contains("171")) continue;
            if(count193 >= nOfQSForEachWavelength && image.contains("193")) continue;

            int patchSize = random.nextInt(256 - 32) + 32;
            int nOfTrial = 1000;
            Geometry geometry = null;
            while(nOfTrial > 0) {
                geometry = createRandomGeometry(patchSize);
                if(!checkIfIntersectingWithOtherEvents(geometry, imageTime)) break;
                nOfTrial--;
            }
            if (nOfTrial == 0) {
                System.out.println("Tried a lot but couldn't find!");
                continue;
            }

            Event e = createQSFromGeom(geometry, image);
            qsEvents.add(e);
            if(e.getMeasurement().equalsIgnoreCase("131")) count131++;
            if(e.getMeasurement().equalsIgnoreCase("171")) count171++;
            if(e.getMeasurement().equalsIgnoreCase("193")) count193++;
        }
    }

    public List<Event> getQSEvents() {
        return qsEvents;
    }

    public Event createQSFromGeom(Geometry geometry, String imageName) {

        Event e = new Event();
        e.setEventType(EventType.QS);
        e.setsFileName(imageName);
        e.setmFileName(imageName);
        e.seteFileName(imageName);
        e.setMeasurement(imageName.substring(imageName.lastIndexOf("_") + 1));

        e.setId("QS_EVENT_ID");
        e.setStartDateString("N/A");
        e.setEndDateString("N/A");
        e.setFrm("N/A");
        return e;
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
        double randomDegree = random.nextDouble() * Math.toRadians(360);
        double randomRadius = random.nextDouble();
        //Generate random point in a circle of center (0, 0) radius 1
        double rX = Math.sqrt(randomRadius) * Math.cos(randomDegree);
        double rY = Math.sqrt(randomRadius) * Math.sin(randomDegree);
        //Enlarge Circle to radius of image (some padding applied to original radius)
        double radius = imageRadius - 256 - patchSize;
        rX = rX * radius;
        rY = rY * radius;
        int x = (int) (rX + imageRadius);
        int y = (int) (rY + imageRadius);
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate[] coords = new Coordinate[2];
        coords[0] = new Coordinate(x - patchSize/2, y - patchSize/2);
        coords[1] = new Coordinate(x + patchSize/2, y + patchSize/2);
        Geometry g = geometryFactory.createMultiPoint(coords).getEnvelope();
        return g;
    }
}

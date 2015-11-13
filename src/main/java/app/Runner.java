package app;

import app.core.DrawPolygonOnImage;
import app.core.JP2Downloader;
import app.core.URIFinder;
import app.models.Event;
import app.models.EventType;
import app.service.clean.BadRecordCleaner;
import app.service.JP2DownloaderService;
import app.service.clean.RecordCleaner;
import app.utils.*;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by ahmetkucuk on 27/09/15.
 */
public class Runner {

    //Directory support should be added.
    public static final String INPUT_FILE_NAME = "/Users/ahmetkucuk/Documents/Research/DNNProject/formattedop/SG/RECORD/SG_Records.txt";

    public static final String INPUT_FILE_NAME_META = "/Users/ahmetkucuk/Documents/Research/DNNProject/formattedop/%s/RECORD/%s_Records.txt";

    public static final String BAD_RECORD_FILE_NAME_META = "/Users/ahmetkucuk/Documents/Research/DNNProject/formattedop/%s/BAD/%s_Bad_Records.txt";
    public static final String BAD_RECORD_OUTPUT_FILE_NAME_META = "/Users/ahmetkucuk/Documents/Research/DNNProject/formattedop/%s/BAD/%s_Bad_Records-extracted.txt";


    public static final String RECORD_FILE_NAME_META = "/Users/ahmetkucuk/Documents/Research/DNNProject/formattedop/%s/RECORD/%s_Records.txt";
    public static final String RECORD_OUTPUT_FILE_NAME_META = "/Users/ahmetkucuk/Documents/Research/DNNProject/formattedop/%s/RECORD/%s_Records-extracted.txt";

    public static final String EXTRACTED_FILE = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/%s/%s_Records.txt";
    public static final String FINAL_DATA_OUTPUT = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/events.txt";
    public static final String FINAL_DATA_OUTPUT_WITH_FILE_NAME = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/events_with_filename.txt";
    public static final String FINAL_SECONDARY_DATA_OUTPUT_WITH_FILE_NAME = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/events_secondary_with_filename.txt";
    public static final String FINAL_SECONDARY_DATA_OUTPUT = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/events_secondary.txt";
    public static final String FINAL_DATA_IMAGE_OUTPUT = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/images/";
    public static final String EXTRACTED_BAD_FILE = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/%s/%s_Bad_Records-extracted.txt";

    public static void main(String[] args) throws Exception {

//        System.out.println(CoordinateSystemConverter.convertHPCToPixXY(new Coordinate(-39.662966, -28.024797)));
//        System.out.println(CoordinateSystemConverter.convertHPCToPixXY(new Coordinate(-27.6345, -15.292138)));

//        System.out.println(Utilities.getDateFromString("2012-01-02T00:00:00"));


//        int limit = 5;
//        int waitBetween = 10;

//        downloadByEventType(eventType, "S", limit, waitBetween);
//        downloadByEventType(eventType, "M", limit, waitBetween);
//        downloadByEventType(eventType, "E", limit, waitBetween);

//        drawByEventType(eventType, "S", limit);
//        drawByEventType(eventType, "M", limit);
//        drawByEventType(eventType, "E", limit);
//        mikeTest();
//        clearAllBadRecords();
//        clearAllRecords();
//        readAllData();
        downloadFinalImages();
//        getJPIPFileName();
//        test();
//        getJPIPFileNameForSecondary();
//        fixCoordinateError();
    }

//    public static void fixCoordinateError() {
//
//        EventReader trueValues = new EventReader(FINAL_DATA_OUTPUT);
//        EventReader wrongValues = new EventReader(FINAL_DATA_OUTPUT_WITH_FILE_NAME);
//
//        Event trueEvent = null;
//        Event wrongEvent = null;
//
//        FileWriter fileWriter = new FileWriter(FINAL_2_DATA_OUTPUT_WITH_FILE_NAME);
//        fileWriter.start();
//        fileWriter.writeToFile("id\tevent_type\tstart_time\tend_time\tchannel\tbbox\tsfilename\tmfilename\tefilename\n");
//        while((trueEvent = trueValues.next()) != null && (wrongEvent = wrongValues.next()) != null) {
//            wrongEvent.setCoordinateString(trueEvent.getCoordinateString());
//            fileWriter.writeToFile(wrongEvent.toString() + "\n");
//        }
//        fileWriter.finish();
//
//    }


    public static void test() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        long startTime = System.currentTimeMillis();
        for(int i = 0; i < 250; i++) {
            executorService.submit(() -> {
                try{
                    Thread.sleep(1000);
                    System.out.println("in thread");
                }catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        System.out.println("after: " + ((System.currentTimeMillis() - startTime)/(1000)));
    }

    public static void getJPIPFileNameForSecondary() {
        new URIFinder().getJPIPUriNameFromFile(FINAL_SECONDARY_DATA_OUTPUT, FINAL_SECONDARY_DATA_OUTPUT_WITH_FILE_NAME);
    }

    public static void getJPIPFileName() {
        new URIFinder().getJPIPUriNameFromFile(FINAL_DATA_OUTPUT, FINAL_DATA_OUTPUT_WITH_FILE_NAME);
    }

    public static void downloadFinalImages() {

        long startTime = System.currentTimeMillis();
        new JP2DownloaderService().downloadImageFromFile(FINAL_DATA_OUTPUT_WITH_FILE_NAME, "S", FINAL_DATA_IMAGE_OUTPUT, 100, 100000);
        long endTime = System.currentTimeMillis() - startTime;
        System.out.println("End Time: " + endTime + " in seconds: " + (endTime/(1000)));
//        new JP2DownloaderService().downloadImageFromFile(FINAL_DATA_OUTPUT_WITH_FILE_NAME, "S", FINAL_DATA_IMAGE_OUTPUT, 100, 150000);

    }

    public static final List<Event> allEvents = new ArrayList<>();

    static Set<String> set = new HashSet<>();
    static Map<String, Set<String>> map = new HashMap<>();

    public static void readAllData() {

        int eventId = 5000000;
        for(EventType e: EventType.values()) {
            int id = eventId;
            String inputFile = String.format(EXTRACTED_FILE, e.toString(), e.toString());
            EventFileReader.init(inputFile);
            Event event;
            while((event = EventFileReader.getInstance().next()) != null) {
                event.setId(id);
                allEvents.add(event);
                id++;
            }
            String inputFileBad = String.format(EXTRACTED_BAD_FILE, e.toString(), e.toString());
            EventFileReader.init(inputFileBad);
            while((event = EventFileReader.getInstance().next()) != null) {
                event.setId(id);
                allEvents.add(event);
                id++;
            }
            eventId += 1000000;
        }

        FileWriter writer = new FileWriter(FINAL_SECONDARY_DATA_OUTPUT);
        writer.start();
        writer.writeToFile("id\tevent_type\tstart_time\tend_time\tchannel\tbbox\n");
        for(int i = 0; i < allEvents.size(); i++) {
            Event event = allEvents.get(i);
//            System.out.println(event.getMeasurement());
//            if(event.getMeasurement().equalsIgnoreCase("AR"))
//                System.out.println(event.toString());
//            if(!map.containsKey(event.getEventType().toString())) {
//                map.put(event.getEventType().toString(), new HashSet<String>());
//            }
//            map.get(event.getEventType().toString()).add(event.getMeasurement());
            if(!Utilities.hasRealMeasurementValue(event.getMeasurement())) {
                writer.writeToFile(Utilities.secondaryEventToString(event));
            }
//            writer.writeToFile(Utilities.eventToString(event));
        }

        writer.finish();
//        for(Map.Entry<String, Set<String>> entry:  map.entrySet()) {
//            System.out.println("*****" + entry.getKey());
//            Iterator<String> iterator = entry.getValue().iterator();
//            while(iterator.hasNext()) {
//                System.out.println(iterator.next());
//            }
//        }

        System.out.println("List size: " + allEvents.size());
    }

    public static void clearAllBadRecords() {

        for(EventType e: EventType.values()) {
            String inputFile = String.format(BAD_RECORD_FILE_NAME_META, e.toString(), e.toString());
            String outputFile = String.format(BAD_RECORD_OUTPUT_FILE_NAME_META, e.toString(), e.toString());
            String[] features = new String[] {"event_type", "event_starttime", "event_endtime", "obs_channelid", "hpc_bbox"};
            new BadRecordCleaner(inputFile, features, outputFile).run();
        }
    }

    public static void clearAllRecords() {
        for(EventType e: EventType.values()) {
            String inputFile = String.format(RECORD_FILE_NAME_META, e.toString(), e.toString());
            String outputFile = String.format(RECORD_OUTPUT_FILE_NAME_META, e.toString(), e.toString());
            String[] features = new String[] {"event_type", "event_starttime", "event_endtime", "obs_channelid", "hpc_bbox"};
            new RecordCleaner(inputFile, features, outputFile).run();
        }
    }


    /**
     * This method is implemented for test purpose
     */
    public static void downloadByEventType(EventType eventType, String eventTimeType, int limit, int waitBetween) {
        new JP2Downloader().downloadFromInputFile(String.format(Constants.INPUT_FILE_NAME_META, eventType.toString(), eventType.toString()),
                eventTimeType, String.format(Constants.OUTPUT_FILE_NAME_META, eventType.toString()), limit, 0, waitBetween);
    }

    public static void drawByEventType(EventType eventType, String eventTimeType, int limit) {
        new DrawPolygonOnImage().draw(String.format(Constants.INPUT_FILE_NAME_META, eventType.toString(), eventType.toString()),
                eventTimeType, String.format(Constants.JPEG_IMAGE_FILE_LOCATION_META, eventType.toString()), limit);
    }


    public static void mikeTest() throws ParseException {

        List<String> polygons = new ArrayList<>();
        polygons.add("2688 1536,2816 1536,2816 1280,2688 1280,2688 1536");
        polygons.add("1155 1065,2497 1065,2497 666,1155 666,1155 1065");
//        polygons.add("POLYGON((2832 3460,2966 3460,2966 3374,2832 3374,2832 3460))");
//        polygons.add("POLYGON((1094 3663,2621 3663,2621 2332,1094 2332,1094 3663))");

//        Coordinate[] coordinates = Utilities.parseCoordinatesString(polygons.get(1));
//        new DrawPolygonOnImage().drawPolygon(Constants.IMAGE_DIRECTORY + "2012_02_22__05_06_21_62__SDO_AIA_AIA_131.jpg", DrawPolygonOnImage.createPolygon(coordinates));


        EventType eventType = EventType.CH;
        String INPUT = String.format(Constants.INPUT_FILE_EXTRACTED_NAME_META, eventType.toString(), eventType.toString());
//        List<Event> events = Utilities.getEventByTime(INPUT, "22/02/2012 03:10:32");
        List<Event> events = Utilities.getEventByTime(INPUT, "09/05/2012 18:43:56");
        System.out.println(events);

        for(Event e: events) {
            System.out.println(e.getCoordinates());
        }
    }
}

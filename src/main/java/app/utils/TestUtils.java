package app.utils;

import app.core.DrawPolygonOnImage;
import app.core.JP2Downloader;
import app.core.URIFinder;
import app.models.Coordinate;
import app.models.Event;
import app.models.EventType;
import app.service.clean.BadRecordCleaner;
import app.service.clean.RecordCleaner;

import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by ahmetkucuk on 20/11/15.
 */
public class TestUtils {


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
    public static final String FINAL_2_DATA_OUTPUT_WITH_FILE_NAME = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/events_with_filename_deleted_duplicates.txt";
    public static final String FINAL_DATA_OUTPUT_WITH_FILE_NAME_WITHOUT_DUPLICATED_CLEAR_DATE = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/events_with_filename_deleted_duplicates_date_cleared.txt";

    public static final String FINAL_SECONDARY_DATA_OUTPUT_WITH_FILE_NAME = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/events_secondary_with_filename.txt";
    public static final String FINAL_SECONDARY_DATA_OUTPUT_WITH_FILE_NAME_WITHOUT_DUPLICATES_CLEAR_DATE = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/events_secondary_with_filename_deleted_duplicated_date_cleared.txt";
    public static final String FINAL_2_SECONDARY_DATA_OUTPUT_WITH_FILE_NAME = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/events_secondary_with_filename_deleted_duplicates.txt";
    public static final String FINAL_SECONDARY_DATA_OUTPUT = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/events_secondary.txt";
    public static final String FINAL_DATA_IMAGE_OUTPUT = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/images/";
    public static final String EXTRACTED_BAD_FILE = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/%s/%s_Bad_Records-extracted.txt";


    public static final String SERVER_FILES = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/Server/";
    public static final String EVENTS = SERVER_FILES + "events.txt";
    public static final String EVENTS_SECONDARY = SERVER_FILES + "events_secondary.txt";
    public static final String EVENTS_SECONDARY_NEW_FILENAME = SERVER_FILES + "events_secondary_new_filename.txt";

    public static final String JP2_EVENTS = SERVER_FILES + "jp2_events.txt";
    public static final String JP2_EVENTS_SECONDARY = SERVER_FILES + "jp2_events_secondary.txt";

    public static final String FILES5 = SERVER_FILES + "files5.txt";

    public static final String SIZE_OF_FILES = SERVER_FILES + "events_file_name_with_size.txt";
    public static final String SIZE_OF_FILES_SECONDARY = SERVER_FILES + "events_file_name_with_size_secondary.txt";

    public static void main(String[] args) {
//        totalDifImages(1000000);
//        findDiff(DOWNLOADED, ALL_FILES, FILES);


//        differentFileNames();
//        clearFile();

//        changeWrong();
//        count(FINAL_DATA_OUTPUT);
//        count(FINAL_SECONDARY_DATA_OUTPUT);
//        fileSize(SIZE_OF_FILES);
//        isAllExist();
//        getFileNames();
    }

    public static void getFileNames() {
        new URIFinder().getJPIPUriNameFromFile(EVENTS_SECONDARY, EVENTS_SECONDARY_NEW_FILENAME);
    }

    public static void isAllExist() {
        Set<String> events = totalDifImages(EVENTS);
        Set<String> events_secondary = totalDifImages(EVENTS_SECONDARY);
        Set<String> jp2_events = fileAsSet(JP2_EVENTS);
        Set<String> jp2_events_secondary = fileAsSet(JP2_EVENTS_SECONDARY);
        System.out.println("Events Size: " + events.size());
        System.out.println("Events Secondary Size: " + events_secondary.size());
        System.out.println("Downloaded JP2 Events Size: " + jp2_events.size());
        System.out.println("Downloaded JP2 Events Secondary Size: " + jp2_events_secondary.size());


        int countDiff1 = 0;
        for(String s : events) {
            if(!jp2_events.contains(s)) {
//                System.out.println(s);
                countDiff1++;
            }
        }

        int countDiff2 = 0;

        for(String s : events_secondary) {
            if(!jp2_events_secondary.contains(s)) {
//                System.out.println(s);
                countDiff2++;
            }
        }

        System.out.println(countDiff1 + " " + countDiff2);


    }

    public static Set<String> fileAsSet(String fileName) {

        Set<String> result = new HashSet<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            String line = null;

            int counter = 0;
            while((line = reader.readLine()) != null) {
                result.add(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void fileSize(String fileName) {

        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            String line = null;

            int counter = 0;
            while((line = reader.readLine()) != null) {
                String[] tuples = line.split(" ");
                if(!tuples[0].equalsIgnoreCase("1.1M")) {
                    System.out.println(tuples[0]);
                    System.out.println("rm " + tuples[1]);
                    counter++;
                }
            }
            System.out.println(counter);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void count(String f) {

        EventReader eventReader = new EventReader(f);
        Event e = null;

        Map<EventType, Integer> eventCount = new HashMap<>();

        int all = 0;
        while((e = eventReader.next()) != null) {
            if(!eventCount.containsKey(e.getEventType())) {
                eventCount.put(e.getEventType(), 1);
            } else {
                eventCount.put(e.getEventType(), eventCount.get(e.getEventType()) + 1);
            }
            all++;
        }
        System.out.println(all + " " + eventCount);

    }

    static class ErrorEvent {
        public int eventid;
        public String eventTimeType;
        public String downloaded;
        public String actual;
    }

    public static void changeWrong() {

        FileWriter writer = new FileWriter(SERVER_FILES + "event_secondary_2.txt");
        writer.start();
        Map<Integer, ErrorEvent> errorEventsS = new HashMap<>();
        Map<Integer, ErrorEvent> errorEventsM = new HashMap<>();
        Map<Integer, ErrorEvent> errorEventsE = new HashMap<>();
        EventReader reader = new EventReader("/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/events_secondary.txt");
        try(BufferedReader r = new BufferedReader(new FileReader(SERVER_FILES + "errors.txt"))) {

            String line = null;
            while((line = r.readLine()) != null) {
                String[] a = line.split("\t");
                ErrorEvent errorEvent = new ErrorEvent();
                errorEvent.eventid = Integer.parseInt(a[0]);
                errorEvent.eventTimeType = a[1];
                errorEvent.downloaded = a[2].substring(0, a[2].length() - 4);
                System.out.println(errorEvent.downloaded);
                errorEvent.actual = a[3];
                if(errorEvent.eventTimeType.equalsIgnoreCase("S")) {
                    errorEventsS.put(errorEvent.eventid, errorEvent);
                } else if(errorEvent.eventTimeType.equalsIgnoreCase("M")) {
                    errorEventsM.put(errorEvent.eventid, errorEvent);
                } else if(errorEvent.eventTimeType.equalsIgnoreCase("E")) {
                    errorEventsE.put(errorEvent.eventid, errorEvent);
                }
            }
            Event e = null;
            while((e = reader.next()) != null) {
                if(errorEventsS.containsKey(e.getId())) {
                    e.setsFileName(errorEventsS.get(e.getId()).downloaded);
                    writer.writeToFile(e.toString() + "\n");
                } else if(errorEventsM.containsKey(e.getId())) {
                    e.setmFileName(errorEventsM.get(e.getId()).downloaded);
                    writer.writeToFile(e.toString() + "\n");
                } else if(errorEventsE.containsKey(e.getId())) {
                    e.seteFileName(errorEventsE.get(e.getId()).downloaded);
                    writer.writeToFile(e.toString() + "\n");
                } else {
                    writer.writeToFile(e.toString() + "\n");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        writer.finish();
    }

    public static void clearFile() {

        FileWriter writer = new FileWriter(SERVER_FILES + "downloaded2.txt");
        writer.start();
        try(BufferedReader r = new BufferedReader(new FileReader(SERVER_FILES + "files_remaining.txt"))) {

            String line = null;
            while((line = r.readLine()) != null) {
                writer.writeToFile(line.substring(line.lastIndexOf("/")+1, line.length() - 4) + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        writer.finish();
    }

    public static void differentFileNames() {
        Set<String> allRequiredFiles = totalDifImages(SERVER_FILES);
        System.out.println(allRequiredFiles.size());
        Set<String> filePaths = new HashSet<>();
        try (BufferedReader r1 = new BufferedReader(new FileReader(FILES5));) {

            String line = null;
            while((line = r1.readLine()) != null) {
                String fName = line.substring(line.lastIndexOf("/") + 1);
                filePaths.add(fName);
            }
            System.out.println(filePaths.size());

            int i = 0;
            for(String s: allRequiredFiles) {
                if(!filePaths.contains(s)) {
                    System.out.println(s);
                    i++;
                }
            }
            System.out.println(i);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void findDiff(String f1, String f2, String f3) {

        Set<String> downloaded = new HashSet<>();
        Set<String> files = new HashSet<>();
        Set<String> difference = new HashSet<>();

        FileWriter fileWriter = new FileWriter(SERVER_FILES + "difference.txt");
        fileWriter.start();

        try (BufferedReader r1 = new BufferedReader(new FileReader(f1));
            BufferedReader r3 = new BufferedReader(new FileReader(f3));) {

            String line = null;
            while((line = r1.readLine()) != null) {
                downloaded.add(line +  ".jp2");
            }


            while((line = r3.readLine()) != null) {
                if(!downloaded.contains(line.substring(line.lastIndexOf("/")+1))) {
                    difference.add(line);
//                    System.out.println(line);
                    fileWriter.writeToFile("rm /data4/STORE/" + line + "\n");
                }
                files.add(line);
            }

            System.out.println(difference.size());
            System.out.println(files.size());

//            for(String s : difference) {
//                System.out.println(s);
//            }
            fileWriter.finish();

        } catch (Exception e) {

        }
    }

    public static Set<String> totalDifImages(String fileName) {
        EventReader eventReader = new EventReader(fileName);
        Set<String> set = new HashSet<>();
        Event e = null;
        int i = 0;
        while((e = eventReader.next()) != null) {
            set.add(e.getsFileName() + ".jp2");
            set.add(e.getmFileName() + ".jp2");
            set.add(e.geteFileName() + ".jp2");
            i++;
        }
        return set;
    }

    public static void drawOnImage() {
        EventReader reader = new EventReader(FINAL_SECONDARY_DATA_OUTPUT);
        Event event = null;
        int i = 0;
        List<Coordinate[]> coordinateList = new ArrayList<>();
        while((event = reader.next()) != null) {
            if((event.getId() - i) == 6049932) {
                coordinateList.add(event.getCoordinates());
                System.out.println(event.toString());
                i++;
            }
            if(i == 48) break;
        }

        DrawPolygonOnImage drawPolygonOnImage = new DrawPolygonOnImage();

        for(Coordinate[] coordinates : coordinateList) {
            Polygon p = DrawPolygonOnImage.createPolygon(coordinates);
            drawPolygonOnImage.drawPolygon(FINAL_DATA_IMAGE_OUTPUT + "jpegs/2013_02_13__12_03_47_34__SDO_AIA_AIA_171.jpg", p);
        }

    }

    public static void clearWrongDateData() {
        EventReader reader= new EventReader(FINAL_2_SECONDARY_DATA_OUTPUT_WITH_FILE_NAME);
        Event event = null;
        int longEvents = 0;


        FileWriter fileWriter = new FileWriter(FINAL_SECONDARY_DATA_OUTPUT_WITH_FILE_NAME_WITHOUT_DUPLICATES_CLEAR_DATE);
        fileWriter.start();
        fileWriter.writeToFile("id\tevent_type\tstart_time\tend_time\tchannel\tbbox\tsfilename\tmfilename\tefilename\n");

        while((event = reader.next()) != null) {
            if ((event.getEndDate().getTime() - event.getStartDate().getTime()) < 0) continue;

            //Longer than 6 days
            if ((event.getEndDate().getTime() - event.getStartDate().getTime()) > 1000 * 60 * 60 * 24 * 6) {

                longEvents++;
                continue;
            }
            fileWriter.writeToFile(event.toString() + "\n");
        }

        System.out.println("longEventCount: " + longEvents);
        fileWriter.finish();
    }

    public static void findDuplicates() {
        Set<String> set = new HashSet<>();
        EventReader reader= new EventReader(FINAL_DATA_OUTPUT_WITH_FILE_NAME);
        Event event = null;
        int numberOfDuplicates = 0;


        FileWriter fileWriter = new FileWriter(FINAL_2_DATA_OUTPUT_WITH_FILE_NAME);
        fileWriter.start();
        fileWriter.writeToFile("id\tevent_type\tstart_time\tend_time\tchannel\tbbox\tsfilename\tmfilename\tefilename\n");

        while((event = reader.next()) != null) {

            if(set.contains(event.getHash())) {
                numberOfDuplicates++;
            } else {
                fileWriter.writeToFile(event.toString() + "\n");
                set.add(event.getHash());
            }
        }
        fileWriter.finish();
        System.out.println("duplicates: " + numberOfDuplicates);
    }

    public static void fixCoordinateError() {

        EventReader trueValues = new EventReader(FINAL_SECONDARY_DATA_OUTPUT_WITH_FILE_NAME);
        EventReader wrongValues = new EventReader(FINAL_2_SECONDARY_DATA_OUTPUT_WITH_FILE_NAME);

        Event trueEvent = null;
        Event wrongEvent = null;

        FileWriter fileWriter = new FileWriter(FINAL_2_SECONDARY_DATA_OUTPUT_WITH_FILE_NAME);
        fileWriter.start();
        fileWriter.writeToFile("id\tevent_type\tstart_time\tend_time\tchannel\tbbox\tsfilename\tmfilename\tefilename\n");
        while((trueEvent = trueValues.next()) != null && (wrongEvent = wrongValues.next()) != null) {
            if(trueEvent.getId() != wrongEvent.getId()) System.out.println("Huge Problem");
            wrongEvent.setCoordinateString(trueEvent.getCoordinateString());
            fileWriter.writeToFile(wrongEvent.toString() + "\n");
        }
        fileWriter.finish();

    }


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

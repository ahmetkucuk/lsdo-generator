package app.core;

import app.models.Coordinate;
import app.models.Event;
import app.models.EventType;
import app.service.clean.BadRecordCleaner;
import app.service.clean.RecordCleaner;
import app.utils.*;

import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 *
 * There are several method that is used to clean and format the data that we have
 * Created by ahmetkucuk on 20/11/15.
 */
public class DataPreparation {

    //Gap between event and image date
    public static final int GAP = 6 * 60 * 1000 * 60;

    public static final String BAD_RECORD_FILE_NAME_META = "/Users/ahmetkucuk/Documents/Research/DNNProject/formattedop/%s/BAD/%s_Bad_Records.txt";
    public static final String BAD_RECORD_OUTPUT_FILE_NAME_META = "/Users/ahmetkucuk/Documents/Research/DNNProject/formattedop/%s/BAD/%s_Bad_Records-extracted.txt";


    public static final String RECORD_FILE_NAME_META = "/Users/ahmetkucuk/Documents/Research/DNNProject/formattedop/%s/RECORD/%s_Records.txt";
    public static final String RECORD_OUTPUT_FILE_NAME_META = "/Users/ahmetkucuk/Documents/Research/DNNProject/formattedop/%s/RECORD/%s_Records-extracted.txt";

    public static final String FINAL_DATA_OUTPUT_WITH_FILE_NAME = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/events_with_filename.txt";
    public static final String FINAL_2_DATA_OUTPUT_WITH_FILE_NAME = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/events_with_filename_deleted_duplicates.txt";

    public static final String FINAL_SECONDARY_DATA_OUTPUT_WITH_FILE_NAME_WITHOUT_DUPLICATES_CLEAR_DATE = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/events_secondary_with_filename_deleted_duplicated_date_cleared.txt";
    public static final String FINAL_2_SECONDARY_DATA_OUTPUT_WITH_FILE_NAME = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/events_secondary_with_filename_deleted_duplicates.txt";
    public static final String FINAL_SECONDARY_DATA_OUTPUT = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/events_secondary.txt";
    public static final String FINAL_DATA_IMAGE_OUTPUT = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/images/";


    public static final String SERVER_FILES = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/Server/";
    public static final String SECONDARY_FOLDER = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/Server/secondary_events/";
    public static final String EVENTS_FOLDER = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/Server/events/";
    public static final String EVENTS = SERVER_FILES + "events.txt";
    public static final String EVENTS_SECONDARY = SERVER_FILES + "events_secondary.txt";
    public static final String EVENTS_SECONDARY_NEW_FILENAME = SERVER_FILES + "events_secondary_new_filename.txt";

    public static final String JP2_EVENTS = SERVER_FILES + "jp2_events.txt";
    public static final String JP2_EVENTS_SECONDARY = SERVER_FILES + "jp2_events_secondary.txt";

    public static void main(String[] args) {

        countEventByType(SECONDARY_FOLDER + "final_secondary_events.txt");
//        generateFileNameIndex(SECONDARY_FOLDER + "jp2_files.txt", SECONDARY_FOLDER + "fileNamePaths.txt");
//        generateFileNameIndex(EVENTS_FOLDER + "jp2_files.txt", EVENTS_FOLDER + "fileNamePaths.txt");
    }

    /**
     * This method check gap between downloaded filename date and original event date
     */
    public static void testImageFileName() {
        EventReader reader = new EventReader(SECONDARY_FOLDER + "final_events_secondary.txt");

        app.utils.FileWriter writer = new app.utils.FileWriter(SECONDARY_FOLDER + "event_gap_larger_than_6.txt");
        writer.start();

        Event e = null;
        Event eMax = null;
        long max = 0;
        int count = 0;
        while((e = reader.next()) != null) {
            Date start = imageNameToDate(e.getsFileName());
            Date mid = imageNameToDate(e.getmFileName());
            Date end = imageNameToDate(e.geteFileName());
            long temp = Math.abs((start.getTime() - e.getStartDate().getTime())/(1000 * 60));
            if(max < temp) {
                max = temp;
                eMax = e;
            }
            if(Math.abs((start.getTime() - e.getStartDate().getTime())) > GAP || Math.abs((mid.getTime() - e.getMiddleDate().getTime())) > GAP || Math.abs((end.getTime() - e.getEndDate().getTime())) > GAP) {
                writer.writeToFile(e.toString() + "\n");
                count++;
            }
        }
        writer.finish();
        System.out.println(count);
        System.out.println(eMax.toString());

    }

    public static Date imageNameToDate(String imageName) {
//        String[] dates = imageName.split("_");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_M_dd'__'HH_mm_ss_SSS");
        try {
            return formatter.parse(imageName.substring(0, 23));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void getFileNames() {
        new URIFinder().getJPIPUriNameFromFile(EVENTS_SECONDARY, EVENTS_SECONDARY_NEW_FILENAME);
    }

    /**
     * This method checks if required file name in events
     * occur in the jp2 file list that we have in the server
     */
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

    public static Set<String> jp2DirToSet(String fileName) {

        Set<String> result = new HashSet<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            String line;
            while((line = reader.readLine()) != null) {
                result.add(line.substring(line.lastIndexOf("/")+1, line.length() - 4));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
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

    /**
     * Check file sizes from a file ( file generated by "find" command of linux shell
     * @param fileName
     */
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

    /**
     * Count event by type
     * @param f
     */
    public static void countEventByType(String f) {

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

    public static void generateFileNameIndex(String inputFile, String outputFile) {

        app.utils.FileWriter writer = new app.utils.FileWriter(outputFile);
        writer.start();

        try(BufferedReader r = new BufferedReader(new FileReader(inputFile))) {

            String line = null;
            while((line = r.readLine()) != null) {
                writer.writeToFile(line.substring(line.lastIndexOf("/")+1, line.length() - 4) + "\t" + line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        writer.finish();
    }

    /**
     * Find number of total different image name in a event file
     * @param fileName
     * @return
     */
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

    /**
     * Just picked random image and draw on it to see if there is a problem
     */
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

    /**
     * Remove if time difference between event start time and event end time is larger than 6 days
     */
    public static void clearWrongDateData() {
        EventReader reader= new EventReader(FINAL_2_SECONDARY_DATA_OUTPUT_WITH_FILE_NAME);
        Event event = null;
        int longEvents = 0;


        app.utils.FileWriter fileWriter = new app.utils.FileWriter(FINAL_SECONDARY_DATA_OUTPUT_WITH_FILE_NAME_WITHOUT_DUPLICATES_CLEAR_DATE);
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


        app.utils.FileWriter fileWriter = new app.utils.FileWriter(FINAL_2_DATA_OUTPUT_WITH_FILE_NAME);
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

}

package app.core;

import app.models.Event;
import app.models.EventType;
import app.utils.CoordinateSystemConverter;
import app.utils.EventReader;
import app.utils.FileWriter;
import app.utils.Utilities;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * There are several method that is used to clean and format the data that we have
 * Created by ahmetkucuk on 20/11/15.
 */
public class DataPreparation {

    //Gap between event and image date
    public static final int GAP = 6 * 60 * 1000 * 60;
    public static final int GAP_BETWEEN_TIME_AND_IMAGE = 10 * 1000 * 60;

    public static final String HEADER = "id\tevent_type\tstart_time\tend_time\tchannel\tbbox\tsfilename\tmfilename\tefilename\n";

    public static final String SERVER_FILES = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/Server/";
    public static final String SECONDARY_FOLDER = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/Server/secondary_events/";
    public static final String EVENTS_FOLDER = "/Users/ahmetkucuk/Documents/Research/DNNProject/Final_Data/Server/events/";
    public static final String EVENTS = SERVER_FILES + "events.txt";
    public static final String EVENTS_SECONDARY = SERVER_FILES + "events_secondary.txt";
    public static final String EVENTS_SECONDARY_NEW_FILENAME = SERVER_FILES + "events_secondary_new_filename.txt";

    public static final String ALREADY_EXISTING_FILES = SERVER_FILES + "mike_file_name/file_name.txt";

    public static final String JP2_EVENTS = SERVER_FILES + "jp2_events.txt";
    public static final String JP2_EVENTS_SECONDARY = SERVER_FILES + "jp2_events_secondary.txt";

    public static final String DEV_BASE = "/Users/ahmetkucuk/Documents/Developer/";
    public static final String JSON_DIR = DEV_BASE + "java/QueryHEK/Result/All";

    public static void main(String[] args) throws Exception {

        // STEP 1: extractParameters(JSON_DIR);
        // Remove invalid instances and separate primary and secondary
        //removeInvalidInstances(JSON_DIR + "/all.txt");

        // STEP 3: Use image downloading step to get image names


        // STEP 4:
        //testImageFileName("/Users/ahmetkucuk/Documents/Research/EVENTS_DATASET/HPC/primary.txt", "/Users/ahmetkucuk/Documents/Research/EVENTS_DATASET/HPC/primary_corrected_date.txt");
        // STEP 4:
        //testImageFileName("/Users/ahmetkucuk/Documents/Research/EVENTS_DATASET/HPC/secondary.txt", "/Users/ahmetkucuk/Documents/Research/EVENTS_DATASET/HPC/secondary_corrected_date.txt");

        // STEP 5:
        //convertToPixelCoordinate("/Users/ahmetkucuk/Documents/Research/EVENTS_DATASET/HPC/primary_corrected_date.txt", "/Users/ahmetkucuk/Documents/Research/EVENTS_DATASET/HPC/primary_corrected_date_pixel.txt");
        // STEP 5:
        //convertToPixelCoordinate("/Users/ahmetkucuk/Documents/Research/EVENTS_DATASET/HPC/secondary_corrected_date.txt", "/Users/ahmetkucuk/Documents/Research/EVENTS_DATASET/HPC/secondary_corrected_date_pixel.txt");

        // STEP 6:
        //removeDuplicates("/Users/ahmetkucuk/Documents/Research/EVENTS_DATASET/HPC/primary_corrected_date_pixel.txt", "/Users/ahmetkucuk/Documents/Research/EVENTS_DATASET/HPC/primary_corrected_date_pixel_noDup.txt");
        // STEP 6:
        //removeDuplicates("/Users/ahmetkucuk/Documents/Research/EVENTS_DATASET/HPC/secondary_corrected_date_pixel.txt", "/Users/ahmetkucuk/Documents/Research/EVENTS_DATASET/HPC/secondary_corrected_date_pixel_noDup.txt");

        // DATA IS READY, NOW DO STATISTICS


        String primaryFinal = "/Users/ahmetkucuk/Documents/Research/EVENTS_DATASET/HPC/Final_Data/Primary/primary.txt";
        String secondaryFinal = "/Users/ahmetkucuk/Documents/Research/EVENTS_DATASET/HPC/Final_Data/Secondary/secondary.txt";


        //For our image region tests, we extract one month events and extract image region from this data
        extractOneMonthImage(primaryFinal, "/Users/ahmetkucuk/Documents/Research/EVENTS_DATASET/HPC/primary_bbox_500_each.txt");

//        String pString = "POLYGON((-972.828 67.5258,-940.218 84.1224,-922.458 216.0732,-954.468 199.6362,-972.828 67.5258))";
//        WKTReader wktReader = new WKTReader();
//        Geometry g = wktReader.read(pString);
//
//        System.out.println(toPixelValues(g));
//        Geometry g2 = wktReader.read(toPixelValues(g));
//        Geometry g3 = wktReader.read(toPixelValues(g.getEnvelope()));
//
//        System.out.println(g2.getEnvelope());
//        System.out.println(g3);

//        System.out.println("*********** Count By Month ************");
//        doStatistics(primaryFinal);
//        System.out.println("***********************");
//        doStatistics(secondaryFinal);
//        System.out.println(totalDifImages(primaryFinal).size());
//        System.out.println("***********************");
//        System.out.println(totalDifImages(secondaryFinal).size());
//        //System.out.println(totalDifImages(secondaryFinal).size());
//        System.out.println("*********** Count Event By Type ************");
//        countEventByType(primaryFinal);
//        System.out.println("***********************");
//        countEventByType(secondaryFinal);

        //testImageFileName("/Users/ahmetkucuk/Documents/Research/EVENTS_DATASET/HPC/secondary.txt", "/Users/ahmetkucuk/Documents/Research/EVENTS_DATASET/HPC/primary_corrected_date.txt");
//        calculateUnnecassary(secondaryFinal, "/Users/ahmetkucuk/Documents/Research/EVENTS_DATASET/HPC/images_secondary_directories.txt", "/Users/ahmetkucuk/Documents/Research/EVENTS_DATASET/HPC/secondary_rm.sh");
//        finalFormat(primaryFinal, "/Users/ahmetkucuk/Documents/Research/EVENTS_DATASET/HPC/Final_Data/Primary/primary-formated.txt");
//        finalFormat(secondaryFinal, "/Users/ahmetkucuk/Documents/Research/EVENTS_DATASET/HPC/Final_Data/Secondary/secondary-formated.txt");
//        preparePathIndex("/Users/ahmetkucuk/Documents/Research/EVENTS_DATASET/HPC/primary_images_names.txt", "/Users/ahmetkucuk/Documents/Research/EVENTS_DATASET/HPC/file_name_paths_primary.txt");
//        preparePathIndex("/Users/ahmetkucuk/Documents/Research/EVENTS_DATASET/HPC/secondary_images_name.txt", "/Users/ahmetkucuk/Documents/Research/EVENTS_DATASET/HPC/file_name_paths_secondary.txt");
    }

    public static void preparePathIndex(String input, String output) throws IOException {
        String line;

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(input)));

        FileWriter writer = new FileWriter(output);
        writer.start();
        writer.writeToFile("image_name\timage_directory\n");

        Set<String> set = new HashSet<>();

        while((line = reader.readLine()) != null) {
            String imageFileName = line.substring(line.lastIndexOf("/")+1, line.lastIndexOf("."));
            String p = imageFileName + "\t" + line.substring(line.indexOf("SDO/")+4);
            if(!set.contains(imageFileName)) {
                writer.writeToFile(p + "\n");
                set.add(imageFileName);
            } else{
                System.out.println(input + "duplication");
            }
        }
        writer.finish();
    }


    public static void finalFormat(String inputFile, String outputfile) throws com.vividsolutions.jts.io.ParseException {
        EventReader reader = new EventReader(inputFile);
        Event event;

        FileWriter writer = new FileWriter(outputfile);
        writer.start();
        writer.writeToFile(HEADER);

        while((event = reader.next()) != null) {
            writer.writeToFile(Utilities.toFinalString(event) + "\n");

        }
        writer.finish();

    }

    public static void extractOneMonthImage(String inputFile, String outputFile) {
        EventReader reader = new EventReader(inputFile);
        Event event;

        Map<String, List<Event>> eventTypeByEventMap = new HashMap<>();

//        FileWriter writer = new FileWriter(outputFile);
//        writer.start();
//        writer.writeToFile(HEADER);

        while((event = reader.next()) != null) {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date start = simpleDateFormat.parse("2013-01-01");
                Date end = simpleDateFormat.parse("2013-01-30");
                //if(event.getMeasurement().equalsIgnoreCase("131") || event.getMeasurement().equalsIgnoreCase("171")) {
//                    if(event.getStartDate().after(start) && event.getStartDate().before(end)) {
                        WKTReader wktReader = new WKTReader();
                        Geometry g = wktReader.read(event.getCoordinateString());
//                        System.out.println(" 1: " +  g);

//                        System.out.println(" 2: " + g);
                        Coordinate A = g.getCoordinates()[0];
                        Coordinate B = g.getCoordinates()[1];
                        Coordinate C = g.getCoordinates()[2];
                        Coordinate D = g.getCoordinates()[3];
                        //BC
                        //AD
                        List<Integer> allXs = new ArrayList<>();
                        allXs.add((int)A.x);
                        allXs.add((int)B.x);
                        allXs.add((int)C.x);
                        allXs.add((int)D.x);


                        List<Integer> allYs = new ArrayList<>();
                        allYs.add((int)A.y);
                        allYs.add((int)B.y);
                        allYs.add((int)C.y);
                        allYs.add((int)D.y);

                        int w = Collections.max(allXs) - Collections.min(allXs);
                        int h = Collections.max(allYs) - Collections.min(allYs);
                        int x = Collections.min(allXs);
                        int y = Collections.min(allYs);
                        event.setCoordinateString((x + "-" + y + "-" + w + "-" + h));
                        event.setRegionHeight(h);
                        event.setRegionWidth(w);
                        //System.out.println(event);
//                        if(Math.abs(1 - (w/(double)h)) < 0.1)
//                            writer.writeToFile(event.toString() + "\n");
                        if(w == h ) {

                            String key = event.getEventType() + event.getMeasurement();
                            if(eventTypeByEventMap.containsKey(key)) {
                                eventTypeByEventMap.get(key).add(event);
                            } else {
                                List<Event> eventList = new ArrayList<>();
                                eventList.add(event);
                                eventTypeByEventMap.put(key, eventList);
                            }
                        }
//                    }
                //}
            } catch (ParseException e) {
                System.out.println(event.toString());
                e.printStackTrace();
            } catch (com.vividsolutions.jts.io.ParseException e) {
                e.printStackTrace();
            }

        }

        for (Map.Entry<String, List<Event>> entry: eventTypeByEventMap.entrySet()) {
            Collections.sort(entry.getValue(), (o1, o2) -> {
                if(o1.getRegionHeight() > o2.getRegionHeight()) {
                    return -1;
                } else if(o1.getRegionHeight() < o2.getRegionHeight()) {
                    return 1;
                }
                return 0;
            });
        }

        List<String> sortKeys = new ArrayList<>();
        sortKeys.addAll(eventTypeByEventMap.keySet());
        Collections.sort(sortKeys);

        for (String key: sortKeys) {
            int count = 0;
            System.out.println(key + " " + eventTypeByEventMap.get(key).size());
            for(Event e : eventTypeByEventMap.get(key)) {
                if(count >= 500) break;
//                writer.writeToFile(e.toString() + "\n");
                count++;
            }
        }


//        writer.finish();
    }

    public static void convertToPixelCoordinate(String inputFile, String outputFile) {
        EventReader reader = new EventReader(inputFile);
        Event event;

        FileWriter writer = new FileWriter(outputFile);
        writer.start();
        writer.writeToFile(HEADER);

        int invalidCount = 0;
        while((event = reader.next()) != null) {
            WKTReader wktReader = new WKTReader();
            try {
                Geometry polygon = wktReader.read(event.getCoordinateString());

                event.setCoordinateString(toPixelValues(polygon));
                polygon = wktReader.read(event.getCoordinateString());

                if(!polygon.isValid()) {
                    invalidCount++;
                    continue;
                }

                writer.writeToFile(event.toString() + "\n");

            } catch (com.vividsolutions.jts.io.ParseException e) {
                e.printStackTrace();
            }
        }
        writer.finish();
        System.out.println(invalidCount);
    }

    public static String toPixelValues(Geometry p) {
        StringBuilder builder = new StringBuilder();
        builder.append("POLYGON((");
        for(int i = 0; i < p.getCoordinates().length; i++) {
            Coordinate temp = CoordinateSystemConverter.convertHPCToPixXY(p.getCoordinates()[i]);
            builder.append((int)temp.x + " ");
            builder.append((int)temp.y);
            if(i != p.getCoordinates().length-1)
                builder.append(", ");

        }
        builder.append("))");
        return builder.toString();
    }


    public static void removeInvalidInstances(String filePath) throws com.vividsolutions.jts.io.ParseException {

        EventReader reader = new EventReader(filePath);

        Map<String, Event> eventId = new HashMap<>();

        Event event;
        int counter = 0;
        int counterDuplicates = 0;
        int invalidPolygons = 0;

        while((event = reader.next()) != null) {
            if(event.getStartDate().after(event.getEndDate())) {
                counter++;
                continue;
            }
            if((event.getEndDate().getTime() - event.getStartDate().getTime()) >= GAP) {
                counter++;
                continue;
            }
            if(eventId.containsKey(event.getId())) {
                counterDuplicates++;
                continue;
            }

            WKTReader polygonReader = new WKTReader();
            Geometry polygon = null;
            polygon = polygonReader.read(event.getCoordinateString());

            if(!polygon.isValid()) {
                invalidPolygons++;
                //System.out.println(polygon);
                continue;
            }
            eventId.put(event.getId(), event);

        }
        System.out.println(counter);
        System.out.println(counterDuplicates);
        System.out.println(invalidPolygons);
        List<Event> allEvents = new ArrayList<>(eventId.values());

        FileWriter writerP = new FileWriter(JSON_DIR + "/primary.txt");
        FileWriter writerS = new FileWriter(JSON_DIR + "/secondary.txt");

        writerP.start();
        writerS.start();


        writerP.writeToFile(HEADER);
        writerS.writeToFile(HEADER);



        for(Event e: allEvents) {

            String tempMeasurement = e.getMeasurement();
            if(!Utilities.hasRealMeasurementValue(tempMeasurement)) {
                e.setMeasurement(e.getEventType().getSecondaryMeasurement()+"");
                writerS.writeToFile(e.toString() + "\n");
            }
            e.setMeasurement(Utilities.mapToPrimaryMeasurement(tempMeasurement, e.getEventType())+"");
            writerP.writeToFile(e.toString() + "\n");
        }
        writerP.finish();
        writerS.finish();

    }


    public static void extractParameters(String directory) throws Exception {
        File [] files = getFileArrayFromDir(directory);

        FileWriter writer = new FileWriter(directory + "/all.txt");
        writer.start();
        writer.writeToFile(HEADER);

        for(File f: files) {
            System.out.println(f.getAbsoluteFile());
            JsonArray array = readJsonAsArray(f.getAbsolutePath());
            for (JsonElement j : array) {
                JsonObject obj = j.getAsJsonObject();
                String id = obj.get("kb_archivid").getAsString();
                String hpc = obj.get("hpc_bbox").getAsString();
                String eventStart = obj.get("event_starttime").getAsString();
                String eventEnd = obj.get("event_endtime").getAsString();
                String type = obj.get("event_type").getAsString();
                String channelid = obj.get("obs_channelid").getAsString();
                writer.writeToFile(id + "\t" + type + "\t" + eventStart + "\t" + eventEnd + "\t" + channelid + "\t" + hpc + "\n");

            }
        }
        writer.finish();

    }

    public static File[] getFileArrayFromDir(String directory) {
        File dir = new File(directory);

        File[] matches = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name)
            {
                return name.endsWith(".json");
            }
        });

        return matches;
    }

    public static JsonArray readJsonAsArray(String filePath) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        String json = "";
        String line;
        while((line = reader.readLine()) != null) {
            json += line;
        }

        reader.close();

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(json);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        return jsonObject.get("result").getAsJsonArray();
    }

    public static void removeDuplicates(String inputFile, String outputFile){
        EventReader reader = new EventReader(inputFile);
        Event event;

        Set<String> set = new HashSet<>();

        List<Event> listWithoutDups = new ArrayList<>();
        int counter = 0;
        while((event = reader.next()) != null) {
            String hash = event.getStartDateString() + event.getEndDateString() + event.getCoordinateString() + event.getMeasurement();
            if(set.contains(hash)) {
                counter++;
            } else {
                listWithoutDups.add(event);
            }
            set.add(hash);
        }


        //SORT event and write to file
        FileWriter writer = new FileWriter(outputFile);
        writer.start();
        writer.writeToFile(HEADER);
        Collections.sort(listWithoutDups, new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                if(o1.getEventType() == o2.getEventType()) {
                    return o1.getStartDate().compareTo(o2.getStartDate());
                }

                return o1.getEventType().toString().compareTo(o2.getEventType().toString());
            }
        });
        for(int i = 0; i < listWithoutDups.size(); i++) {
            writer.writeToFile(listWithoutDups.get(i).toString() + "\n");
        }
        writer.finish();


        System.out.println(counter);
    }

    public static String getName(String imageName) {
        String[] s = imageName.split("_");
        String result = s[11] + s[0] + s[1] + s[2] + "_" + s[4] + s[5] + s[6];
        return result;
    }

    public static void doStatistics(String inputFile) {

        EventReader reader = new EventReader(inputFile);
        Event event;

        Map<String, Integer> dateMap = new TreeMap<>();

        while((event = reader.next()) != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM");
            String dateYearMonth = formatter.format(event.getStartDate());
            if(!dateMap.containsKey(dateYearMonth)) {
                dateMap.put(dateYearMonth, 1);
            } else {
                dateMap.put(dateYearMonth, dateMap.get(dateYearMonth) + 1);
            }
        }
        for(Map.Entry<String, Integer> entry : dateMap.entrySet()) {
            System.out.println(entry.getKey() + "\t" + entry.getValue());
        }
    }

    /**
     * This method check gap between downloaded filename date and original event date
     */
    public static void testImageFileName(String inputFile, String outputFile) {
        EventReader reader = new EventReader(inputFile);

        app.utils.FileWriter writer = new app.utils.FileWriter(outputFile);
        writer.start();
        writer.writeToFile(HEADER);

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
            if(Math.abs((start.getTime() - e.getStartDate().getTime())) > GAP_BETWEEN_TIME_AND_IMAGE || Math.abs((mid.getTime() - e.getMiddleDate().getTime())) > GAP_BETWEEN_TIME_AND_IMAGE || Math.abs((end.getTime() - e.getEndDate().getTime())) > GAP_BETWEEN_TIME_AND_IMAGE) {
                count++;
            } else {
                writer.writeToFile(e.toString() + "\n");
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
        Event e;
        int counter171 = 0;
        int counter94 = 0;
        int counter193 = 0;
        int counter131 = 0;
        while((e = eventReader.next()) != null) {
            set.add(e.getsFileName() + ".jp2");
            set.add(e.getmFileName() + ".jp2");
            set.add(e.geteFileName() + ".jp2");
            if(e.getMeasurement().equalsIgnoreCase("171")) {
                counter171++;
            }
            if(e.getMeasurement().equalsIgnoreCase("94")) {
                counter94++;
            }
            if(e.getMeasurement().equalsIgnoreCase("193")) {
                counter193++;
            }
            if(e.getMeasurement().equalsIgnoreCase("131")) {
                counter131++;
            }
        }
        System.out.println("Measurement for 171: " + counter171);
        System.out.println("Measurement for 94: " + counter94);
        System.out.println("Measurement for 193: " + counter193);
        System.out.println("Measurement for 131: " + counter131);
        return set;
    }



}

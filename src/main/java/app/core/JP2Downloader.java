package app.core;

import app.models.Event;
import app.utils.*;
import app.utils.FileWriter;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by ahmetkucuk on 27/09/15.
 */
public class JP2Downloader {

    public static final String SEPARATOR = "\t";
    public static final int THREAD_COUNT = 10;
    private static ExecutorService executorService;
    private static Set<String> downloadedImageNames;

    private FileWriter errorFileWriter;
    private FileWriter eventRecords;
    private FileWriter downloadedImageNameFileWriter;


    public JP2Downloader() {
        executorService = Executors.newFixedThreadPool(THREAD_COUNT);
    }

    /**
     *
     * @param inputFile file should contain bbox value, kb_archvdate
     * @param fileLocation specify a directory for jp2 to be saved
     * @param limit how many of images should be downloaded.
     * @param waitBetween wait between two consecutive download in order not to be banned, in seconds
     */
    public void downloadFromInputFile(String inputFile, String fileLocation, int limit, int offset, int waitBetween) {

        EventReader eventReader = new EventReader(inputFile);
        downloadedImageNames = Utilities.getDownloadedFileNames(fileLocation + "downloaded.txt");
        initFileWriters(fileLocation);

        List<Event> events = new ArrayList<>();
        for(int i = 0; i <= limit + offset; i++) {

            Event e = eventReader.next();
            events.add(e);
            if(e == null) break;
            if(i >= offset) {
                executeFor(e, "S", fileLocation, e.getsFileName());
                executeFor(e, "M", fileLocation, e.getmFileName());
                executeFor(e, "E", fileLocation, e.geteFileName());
            }
        }
        System.out.println("Finished Creating Thread");
        try {
            executorService.shutdown();
            executorService.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS);
            for(Event e : events) {
                eventRecords.writeToFile(e.toString() + "\n");
            }

            closeFileWriters();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Finished All!");
    }

    private void executeFor(Event e, String s, String fileLocation, String fileName) {
//        if(!downloadedImageNames.contains(fileName)) {
            executorService.execute(() -> {
                        try {
//                            downloadForEvent(e, s, fileLocation);
                            downloadForEventSetFileName(e, s, fileLocation);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
            );
//        }
//        downloadedImageNames.add(fileName);
    }

    private void initFileWriters(String fileLocation) {

        errorFileWriter = new FileWriter(fileLocation + "errors.txt");
        eventRecords = new FileWriter(fileLocation + "new_event_records.txt");
        eventRecords.start();
        errorFileWriter.start();

        downloadedImageNameFileWriter = new FileWriter(fileLocation + "downloaded.txt");
        downloadedImageNameFileWriter.start();
    }

    private void closeFileWriters() {
        errorFileWriter.finish();
        downloadedImageNameFileWriter.finish();
        eventRecords.finish();
    }

    private void flushWriters() {
        errorFileWriter.flush();
        downloadedImageNameFileWriter.flush();
        eventRecords.flush();
    }

    private void wait(int seconds) {

        try {
            Thread.sleep(seconds * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadForEventSetFileName(Event event, String eventTimeType, String fileLocation) {

        //event.getStartDate will be changed according to eventTimeType
        String url = "";
        Date eventDate = null;
        String imageFileName = null;
        switch (eventTimeType) {
            case "S":
                eventDate = event.getStartDate();
                imageFileName = event.getsFileName();
                break;
            case "M":
                eventDate = event.getMiddleDate();
                imageFileName = event.getmFileName();
                break;
            case "E":
                eventDate = event.getEndDate();
                imageFileName = event.geteFileName();
                break;
        }

        String eventTime = Utilities.getStringFromDate(eventDate);
        url = String.format(Constants.IMAGE_DOWNLOAD_URL, eventTime, event.getMeasurement());
        String downloadedFileName = "";

        try {
            downloadedFileName = HttpDownloadUtility.downloadFile(url, fileLocation + Utilities.getImageSubPath(eventDate, event.getMeasurement()), true);
            if(downloadedFileName == null || downloadedFileName.length() < 5) {
                errorFileWriter.writeToFile(event.getId() + "\t" + eventTimeType + "\t" +  downloadedFileName + "\t" + imageFileName + "\n");
                errorFileWriter.flush();
            } else {
                switch (eventTimeType) {
                    case "S":
                        event.setsFileName(downloadedFileName.substring(0, downloadedFileName.length() - 4));
                        break;
                    case "M":
                        event.setmFileName(downloadedFileName.substring(0, downloadedFileName.length() - 4));
                        break;
                    case "E":
                        event.seteFileName(downloadedFileName.substring(0, downloadedFileName.length() - 4));
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorFileWriter.writeToFile(event.getId() + "\t" + eventTimeType + "\t" +  downloadedFileName + "\t" + imageFileName + "\n");
            errorFileWriter.flush();
        }
    }

    public void downloadForEvent(Event event, String eventTimeType, String fileLocation) {

        //event.getStartDate will be changed according to eventTimeType
        String url = "";
        Date eventDate = null;
        String imageFileName = null;
        switch (eventTimeType) {
            case "S":
                eventDate = event.getStartDate();
                imageFileName = event.getsFileName();
                break;
            case "M":
                eventDate = event.getMiddleDate();
                imageFileName = event.getmFileName();
                break;
            case "E":
                eventDate = event.getEndDate();
                imageFileName = event.geteFileName();
                break;
        }

        String eventTime = Utilities.getStringFromDate(eventDate);
        url = String.format(Constants.IMAGE_DOWNLOAD_URL, eventTime, event.getMeasurement());
        String downloadedFileName = "";

        try {
            downloadedFileName = HttpDownloadUtility.downloadFile(url, fileLocation + Utilities.getImageSubPath(eventDate, event.getMeasurement()), false);
            if(checkIfFailed(downloadedFileName, imageFileName)) {
                downloadedImageNameFileWriter.writeToFile(imageFileName + "\n");
                downloadedImageNameFileWriter.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorFileWriter.writeToFile(event.getId() + "\t" + eventTimeType + "\t" +  downloadedFileName + "\t" + imageFileName + "\n");
            errorFileWriter.flush();
        }
    }

    /**
     *
     * @param downloadedFileName
     * @param imageFileName
     * @return true if not failed
     * @throws Exception
     */
    private boolean checkIfFailed(String downloadedFileName, String imageFileName) throws Exception {
        if(downloadedFileName == null) {
            throw new Exception();
        } else {
            if(!downloadedFileName.equalsIgnoreCase((imageFileName))) {
                throw new Exception("downloaded and assumed file name are not same");
            }
        }
        return true;
    }


//
//    public void downloadImage(String sourceUrl, String targetDirectory, String targetFileName)
//            throws IOException
//    {
//        URL imageUrl = new URL(sourceUrl);
//        try (InputStream imageReader = new BufferedInputStream(
//                imageUrl.openStream());
//             OutputStream imageWriter = new BufferedOutputStream(
//                     new FileOutputStream(targetDirectory + File.separator
//                             + targetFileName));)
//        {
//
//            int readByte;
//
//            while ((readByte = imageReader.read()) != -1)
//            {
//                imageWriter.write(readByte);
//            }
//        }
//    }
}

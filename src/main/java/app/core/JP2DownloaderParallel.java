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
public class JP2DownloaderParallel {

    public static final String SEPARATOR = "\t";




    private static final String IMAGE_FILENAME_FILE = "jp2_files.txt";
    private static final String ERROR_FILE = "error.txt";
    private static final String NEW_EVENT_FILE = "new_event_records.txt";

    public static final int THREAD_COUNT = 10;
    private static ExecutorService executorService;
    private static Set<String> downloadedImageNames;

    private FileWriter errorFileWriter;
    private FileWriter eventRecords;
    private FileWriter downloadedImageNameFileWriter;

    private String inputFile;
    private String fileLocation;



    public JP2DownloaderParallel(String inputFile, String fileLocation) {
        executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        this.inputFile = inputFile;
        this.fileLocation = fileLocation;
    }

    /**
     *
     * @param limit how many of images should be downloaded.
     * @param waitBetween wait between two consecutive download in order not to be banned, in seconds
     */
    public void downloadFromInputFile(int limit, int offset, int waitBetween) {

        EventReader eventReader = new EventReader(inputFile);
        downloadedImageNames = Utilities.getDownloadedFileNames(fileLocation + IMAGE_FILENAME_FILE);
        initFileWriters(fileLocation);

        List<Event> events = new ArrayList<>();
        for(int i = 0; i <= limit + offset; i++) {

            Event e = eventReader.next();
            if(e == null) break;
            events.add(e);
            if(i >= offset) {
                executeFor(e, e.getStartDate(), e.getsFileName());
                executeFor(e, e.getMiddleDate(),e.getmFileName());
                executeFor(e, e.getEndDate(),e.geteFileName());
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

    private void executeFor(Event e, Date eventDate, String eventImageName) {
        executorService.execute(() -> {
                    try {
                        downloadForEvent(e, eventDate, eventImageName);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
        );
    }

    private void initFileWriters(String fileLocation) {

        errorFileWriter = new FileWriter(fileLocation + ERROR_FILE);
        eventRecords = new FileWriter(fileLocation + NEW_EVENT_FILE);
        eventRecords.start();
        errorFileWriter.start();

        downloadedImageNameFileWriter = new FileWriter(fileLocation + IMAGE_FILENAME_FILE);
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

    private void downloadForEvent(Event event, Date eventDate, String eventImageName) {

        //event.getStartDate will be changed according to eventTimeType

        String eventTime = Utilities.getStringFromDate(eventDate);
        String url = String.format(Constants.IMAGE_DOWNLOAD_URL, eventTime, event.getMeasurement());
        String downloadedFileName;

        try {
            downloadedFileName = HttpDownloadUtility.downloadFile(downloadedImageNames, url, fileLocation + Utilities.getImageSubPath(eventDate, event.getMeasurement()));
            checkIfFailed(downloadedFileName, eventImageName);
            downloadedImageNameFileWriter.writeToFile(downloadedFileName + "\n");
            downloadedImageNameFileWriter.flush();

        } catch (Exception e) {
            e.printStackTrace();
            errorFileWriter.writeToFile(event.toString() + "\n");
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
            throw new Exception("file name null");
        } else {
            if(!downloadedFileName.equalsIgnoreCase((imageFileName + ".jp2"))) {
                throw new Exception("downloaded and assumed file name are not same");
            }
        }
        return true;
    }

}

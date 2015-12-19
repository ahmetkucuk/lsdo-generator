package app.core;

import app.models.Event;
import app.utils.*;

import java.util.*;

/**
 * Created by ahmetkucuk on 10/12/15.
 */
public class JP2Downloader {


    private static final String IMAGE_FILENAME_FILE = "jp2_files.txt";
    private static final String ERROR_FILE = "error.txt";
    private static final String NEW_EVENT_FILE = "new_event_records.txt";

    private static Set<String> downloadedImageNames;


    private FileWriter errorFileWriter;
    private FileWriter newEventRecords;
    private FileWriter downloadedImageNameFileWriter;

    /**
     *
     * @param inputFile file should contain bbox value, kb_archvdate
     * @param fileLocation specify a directory for jp2 to be saved
     * @param limit how many of images should be downloaded.
     * @param waitBetween wait between two consecutive download in order not to be banned, in seconds
     */
    public void downloadFromInputFile(String inputFile, String fileLocation, int limit, int offset, int waitBetween) {

        EventReader eventReader = new EventReader(inputFile);
        downloadedImageNames = Utilities.getDownloadedFileNames(fileLocation + IMAGE_FILENAME_FILE);
        initFileWriters(fileLocation);

        for(int i = 0; i <= limit + offset; i++) {

            Event e = eventReader.next();
            if(e == null) break;
            if(i >= offset) {
                try {
                    downloadForEventSetFileName(e, "S", e.getStartDate(), fileLocation);
                    downloadForEventSetFileName(e, "M", e.getMiddleDate(), fileLocation);
                    downloadForEventSetFileName(e, "E", e.getEndDate(), fileLocation);
                } catch (Exception e1) {
                    errorFileWriter.writeToFile(e + "\n");
                    errorFileWriter.flush();
                    e1.printStackTrace();
                }
            }

        }

        closeFileWriters();
    }

    public void waitSeconds(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void downloadForEventSetFileName(Event event, String eventTimeType, Date eventDate, String fileLocation) throws Exception{

        //event.getStartDate will be changed according to eventTimeType

        String eventTime = Utilities.getStringFromDate(eventDate);
        String url = String.format(Constants.IMAGE_DOWNLOAD_URL, eventTime, event.getMeasurement());

        String eventFileName = "";
        switch (eventTimeType) {
            case "S":
                eventFileName = event.getsFileName();
                break;
            case "M":
                eventFileName = event.getmFileName();
                break;
            case "E":
                eventFileName = event.geteFileName();
                break;
        }

        if(downloadedImageNames.contains(eventFileName + ".jp2")) {
            return;
        }

        String downloadedFileName = HttpDownloadUtility.downloadFile(downloadedImageNames, url, fileLocation + Utilities.getImageSubPath(eventDate, event.getMeasurement()));

        if(downloadedFileName != null && downloadedFileName.length() > 5) {
            downloadedImageNameFileWriter.writeToFile(downloadedFileName + "\n");
            downloadedImageNameFileWriter.flush();
            downloadedImageNames.add(downloadedFileName);
            switch (eventTimeType) {
                case "S":
                    if(!(event.getsFileName() + ".jp2").equalsIgnoreCase(downloadedFileName)) {
                       throw new Exception("wrong file name");
                    }
                    break;
                case "M":
                    if(!(event.getmFileName() + ".jp2").equalsIgnoreCase(downloadedFileName)) {
                        throw new Exception("wrong file name");
                    }
                    break;
                case "E":
                    if(!(event.geteFileName() + ".jp2").equalsIgnoreCase(downloadedFileName)) {
                        throw new Exception("wrong file name");
                    }
                    break;
            }
        } else {
            throw new Exception("downloaded file name problem");
        }
    }


    private void initFileWriters(String fileLocation) {

        errorFileWriter = new FileWriter(fileLocation + ERROR_FILE);
        newEventRecords = new FileWriter(fileLocation + NEW_EVENT_FILE);
        downloadedImageNameFileWriter = new FileWriter(fileLocation + IMAGE_FILENAME_FILE);

        newEventRecords.start();
        errorFileWriter.start();
        downloadedImageNameFileWriter.start();
    }

    private void closeFileWriters() {
        errorFileWriter.finish();
        downloadedImageNameFileWriter.finish();
        newEventRecords.finish();
    }

    private void flushWriters() {
        errorFileWriter.flush();
        downloadedImageNameFileWriter.flush();
        newEventRecords.flush();
    }

}

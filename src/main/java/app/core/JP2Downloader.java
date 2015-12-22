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

    private String inputFile;
    private String fileLocation;

    public JP2Downloader(String inputFile, String fileLocation) {
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

        for(int i = 0; i <= limit + offset; i++) {

            Event e = eventReader.next();
            if(e == null) break;
            if(i >= offset) {
                try {
                    downloadForEventSetFileName(e, e.getStartDate(), fileLocation, e.getsFileName());
                    downloadForEventSetFileName(e, e.getMiddleDate(), fileLocation, e.getmFileName());
                    downloadForEventSetFileName(e, e.getEndDate(), fileLocation, e.geteFileName());
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

    public void downloadForEventSetFileName(Event event, Date eventDate, String fileLocation, String imageFileName) throws Exception{

        //event.getStartDate will be changed according to eventTimeType

        String eventTime = Utilities.getStringFromDate(eventDate);
        String url = String.format(Constants.IMAGE_DOWNLOAD_URL, eventTime, event.getMeasurement());

        String downloadedFileName = HttpDownloadUtility.downloadFile(downloadedImageNames, url, fileLocation + Utilities.getImageSubPath(eventDate, event.getMeasurement()));

        checkIfFailed(imageFileName, downloadedFileName);

        downloadedImageNameFileWriter.writeToFile(downloadedFileName + "\n");
        downloadedImageNameFileWriter.flush();
        downloadedImageNames.add(downloadedFileName);

        if(downloadedFileName != null && downloadedFileName.length() > 5) {
        } else {
            throw new Exception("downloaded file name problem");
        }
    }

    private void checkIfFailed(String actualImageFileName, String downloadedFileName) throws Exception {
        if(downloadedFileName == null || downloadedFileName.length() == 0 || !downloadedFileName.contains(".jp2")) {
            throw new Exception("downloaded file name problem");
        }
        checkIfImageFileNameCorrect(actualImageFileName, downloadedFileName);
    }

    private void checkIfImageFileNameCorrect(String actualImageFileName, String downloadedFileName) throws Exception {
        if(!(actualImageFileName + ".jp2").equalsIgnoreCase(downloadedFileName)) {
            throw new Exception("wrong file name");
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

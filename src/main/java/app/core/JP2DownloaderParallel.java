package app.core;

import app.models.Tuple2;
import app.utils.Constants;
import app.utils.FileWriter;
import app.utils.HttpDownloadUtility;
import app.utils.Utilities;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

    private List<Tuple2<Integer, String>> listOfDateWavelengthTuple;
    private String fileLocation;



    public JP2DownloaderParallel(List<Tuple2<Integer, String>> listToDownload, String fileLocation) {
        executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        listOfDateWavelengthTuple = listToDownload;
        this.fileLocation = fileLocation;
    }

    /**
     *
     * @param limit how many of images should be downloaded.
     * @param waitBetween wait between two consecutive download in order not to be banned, in seconds
     */
    public void downloadFromList(int limit, int offset, int waitBetween) {

        downloadedImageNames = Utilities.getDownloadedFileNames(fileLocation + IMAGE_FILENAME_FILE);
        initFileWriters(fileLocation);

        for(int i = 0; i <= limit + offset; i++) {

            if(i >= listOfDateWavelengthTuple.size()) break;

            if(i >= offset) {
                executeFor(listOfDateWavelengthTuple.get(i));
            }
        }
        System.out.println("Finished Creating Thread");
        try {
            executorService.shutdown();
            executorService.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS);

            closeFileWriters();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Finished All!");
    }

    private void executeFor(Tuple2<Integer, String> e) {
        executorService.execute(() -> {
                    try {
                        downloadForEvent(e);
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

    private void downloadForEvent(Tuple2<Integer, String> t) {

        //event.getStartDate will be changed according to eventTimeType

        String url = String.format(Constants.IMAGE_DOWNLOAD_URL, t.y, t.x);
        String downloadedFileName;

        try {
            downloadedFileName = HttpDownloadUtility.downloadFile(downloadedImageNames, url, fileLocation + Utilities.getImageSubPath(t.y, String.valueOf(t.x)));
            downloadedImageNameFileWriter.writeToFile(downloadedFileName + "\n");
            downloadedImageNameFileWriter.flush();

        } catch (Exception e) {
            e.printStackTrace();
            errorFileWriter.writeToFile(t.toString() + "\n");
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

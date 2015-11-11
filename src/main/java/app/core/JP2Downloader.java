package app.core;

import app.models.Event;
import app.utils.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created by ahmetkucuk on 27/09/15.
 */
public class JP2Downloader {

    public static final String SEPARATOR = "\t";


    /**
     *
     * @param inputFile file should contain bbox value, kb_archvdate
     * @param fileLocation specify a directory for jp2 to be saved
     * @param limit how many of images should be downloaded.
     * @param waitBetween wait between two consecutive download in order not to be banned, in seconds
     */
    public void downloadFromInputFile(String inputFile, String eventTimeType, String fileLocation, int limit, int offset, int waitBetween) {

        EventReader.init(inputFile);

        for(int i = 1; i <= limit + offset; i++) {

            Event e = EventReader.getInstance().next();
            if(i >= offset) {
                downloadForEvent(e, eventTimeType, fileLocation);
                wait(waitBetween);
            }
        }
    }

    private void wait(int seconds) {

        try {
            Thread.sleep(seconds * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadForEvent(Event event, String eventTimeType, String fileLocation) {

        //event.getStartDate will be changed according to eventTimeType
        String url = "";
        Date eventDate = null;
        switch (eventTimeType) {
            case "S":
                eventDate = event.getStartDate();
                break;
            case "M":
                eventDate = event.getMiddleDate();
                break;
            case "E":
                eventDate = event.getEndDate();
                break;
        }

        String eventTime = Utilities.getStringFromDate(eventDate);
        System.out.println(eventTime);
        url = String.format(Constants.IMAGE_DOWNLOAD_URL, eventTime, event.getMeasurement());

        try {
            HttpDownloadUtility.downloadFile(url, fileLocation);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        System.out.println("Finished for event: " + event.toString());
    }



    public void downloadImage(String sourceUrl, String targetDirectory, String targetFileName)
            throws IOException
    {
        URL imageUrl = new URL(sourceUrl);
        try (InputStream imageReader = new BufferedInputStream(
                imageUrl.openStream());
             OutputStream imageWriter = new BufferedOutputStream(
                     new FileOutputStream(targetDirectory + File.separator
                             + targetFileName));)
        {

            int readByte;

            while ((readByte = imageReader.read()) != -1)
            {
                imageWriter.write(readByte);
            }
        }
    }
}

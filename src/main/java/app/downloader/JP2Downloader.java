package app.downloader;

import app.models.Event;
import app.utils.Constants;
import app.utils.EventFileReader;
import app.utils.Utilities;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

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
    public void downloadFromInputFile(String inputFile, String fileLocation, int limit, int waitBetween) {

        EventFileReader.init(inputFile);

        for(int i = 1; i <= limit; i++) {

            try {

                Thread.sleep(waitBetween * 1000);

            } catch (Exception e) {
                e.printStackTrace();
            }

            Event e = EventFileReader.getInstance().next();


            String urlStart = String.format(Constants.IMAGE_DOWNLOAD_URL, Utilities.getStringFromDate(e.getStartDate()), e.getMeasurement());
            String urlMiddle = String.format(Constants.IMAGE_DOWNLOAD_URL, Utilities.getStringFromDate(e.getMiddleDate()), e.getMeasurement());
            String urlEnd = String.format(Constants.IMAGE_DOWNLOAD_URL, Utilities.getStringFromDate(e.getEndDate()), e.getMeasurement());

            String fileNameStart = e.getEventType().toString() + "_S_" + i + ".jp2";
            String fileNameMiddle = e.getEventType().toString() + "_M_" + i + ".jp2";
            String fileNameEnd = e.getEventType().toString() + "_E_" + i + ".jp2";

            System.out.println("Started for event: " +  e.toString());

//            System.out.println(e.getStartDate());
//            System.out.println(e.getEndDate());
//            System.out.println(e.getMiddleDate());
//            System.out.println(Utilities.getStringFromDate(e.getMiddleDate()));

            try {
                downloadImage(urlStart, fileLocation, fileNameStart);
                downloadImage(urlMiddle, fileLocation, fileNameMiddle);
                downloadImage(urlEnd, fileLocation, fileNameEnd);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            System.out.println("Finished for event: " +  e.toString());
        }
    }

    public int findIndexOfHeader(String record, String columnName) {

        String[] headers = record.split(SEPARATOR);
        if(headers == null) {
            return -1;
        }

        for(int i = 0; i < headers.length; i++) {
            if(headers[i].equalsIgnoreCase(columnName)) return i;
        }

        return -1;
    }



    public void downloadImage(String sourceUrl, String targetDirectory, String targetFileName)
            throws MalformedURLException, IOException, FileNotFoundException
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

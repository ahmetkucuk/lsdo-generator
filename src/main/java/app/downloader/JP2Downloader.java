package app.downloader;

import app.models.Event;
import app.utils.Constants;
import app.utils.EventFileReader;

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

//                try {
//
//                    Thread.sleep(waitBetween * 1000);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            Event e = EventFileReader.getInstance().next();

            String url = String.format(Constants.IMAGE_DOWNLOAD_URL, e.getDate() + "Z", e.getMeasurement());
            String fileName = e.getEventType().toString() + "_" + i + ".jp2";
            System.out.println("Download started for URL: " + url);

            try {
                downloadImage(url, fileLocation, fileName);
                System.out.println("Download finished for URL: " + url);
                System.out.println("Image saved to " + fileLocation);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
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

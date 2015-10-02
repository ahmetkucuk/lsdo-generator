package app.downloader;

import app.Event;
import app.utils.Constants;

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

        try(FileInputStream fStream1 = new FileInputStream(inputFile);
        DataInputStream in = new DataInputStream(fStream1);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));) {

            String line = reader.readLine();
            int eventTypeIndex = findIndexOfHeader(line, Constants.FieldNames.EVENT_TYPE);
            int dateIndex = findIndexOfHeader(line, Constants.FieldNames.ARCHIVE_DATE);
            int channelIdIndex = findIndexOfHeader(line, Constants.FieldNames.CHANNEL_ID);


            int counter = 1;
            while((line = reader.readLine()) != null) {

                if(counter >= limit)
                    break;

//                try {
//
//                    Thread.sleep(waitBetween * 1000);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }


                String[] columnValues = line.split(SEPARATOR);

                String eventType = columnValues[eventTypeIndex];
                String date = columnValues[dateIndex] + "Z";
                String measurement = columnValues[channelIdIndex];

                String url = String.format(Constants.IMAGE_DOWNLOAD_URL, date, measurement);
                String fileName = eventType + "_" + counter + "_date_" + date.replace('/', '-') + "_cid_" + measurement + ".jp2";
                System.out.println("Download started for URL: " + url);
                downloadImage(url, fileLocation, fileName);

                //Write name and bbox of this image into a file
                //writer.write();

                System.out.println("Download finished for URL: " + url);
                System.out.println("Image saved to " + fileLocation);
                counter++;
            }

        } catch (Exception e) {
            e.printStackTrace();
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

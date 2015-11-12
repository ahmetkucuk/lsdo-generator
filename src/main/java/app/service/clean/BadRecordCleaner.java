package app.service.clean;

import java.io.*;

/**
 * Created by ahmetkucuk on 21/10/15.
 */
public class BadRecordCleaner extends CleanerBase{


    public BadRecordCleaner(String inputFile, String[] selectedFeatures, String outputFile) {
        super(inputFile, selectedFeatures, outputFile);
    }

    public void run() {
        clearBadRecords();
    }

    private void clearBadRecords() {

        openWriteFile();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)))) {
            String header = null;
            System.out.println("here" + inputFile);
            int sampleCounter = 0;
            writeToFile(arrayToString(selectedFeatures));
            while((header = reader.readLine()) != null) {
                if(!header.contains("noposition")) {
                    System.out.println("something wrong " + header);
                }
                String value = reader.readLine();
                String[] features = getFeatures(header, value, selectedFeatures);
                if(features != null) {
                    String valueToWrite = arrayToString(features);
                    if(sampleCounter % 1000 == 0)
                        System.out.println(valueToWrite);
                    writeToFile(valueToWrite);
                } else {
                    System.out.println("There is problem with finding feature");
                }
                sampleCounter++;
            }

            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeFile();
        }

    }


    private String[] getFeatures(String header, String value, String[] selectedFeatures) {
        String[] features = header.split(SEPARATOR);
        String[] values = value.split(SEPARATOR);
        String[] result = new String[selectedFeatures.length];
        int i = 0;
        for(String s: selectedFeatures) {
            int index = findIndex(features, s);
            if(index == -1)
                return null;
            result[i] = values[index];
            i++;
        }
        return result;
    }
}

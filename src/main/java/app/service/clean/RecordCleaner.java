package app.service.clean;

import java.io.*;

/**
 * Created by ahmetkucuk on 21/10/15.
 */
public class RecordCleaner extends CleanerBase {

    public RecordCleaner(String inputFile, String[] selectedFeatures, String outputFile) {
        super(inputFile, selectedFeatures, outputFile);
    }

    public void run() {
        cleanData();
    }

    private void cleanData() {
        openWriteFile();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)))) {
            String[] features = reader.readLine().split(SEPARATOR);
            int sampleCounter = 0;
            writeToFile(arrayToString(selectedFeatures));
            String line = null;
            int[] indexes = new int[selectedFeatures.length];
            for(int i = 0; i < selectedFeatures.length; i++) {
                indexes[i] = findIndex(features, selectedFeatures[i]);
            }

            while((line = reader.readLine()) != null) {
                String[] values = line.split(SEPARATOR);
                String lineToWrite = "";

                for(int i = 0; i < indexes.length; i++) {
                    lineToWrite = lineToWrite + SEPARATOR + values[indexes[i]];
                }

                lineToWrite = lineToWrite.trim() + "\n";
                if(sampleCounter % 1000 == 0)
                    System.out.println("Sample#" + sampleCounter + " " + lineToWrite);
                writeToFile(lineToWrite);
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
}

package app.service.clean;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * This class is designed to clean Records
 *
 * Created by ahmetkucuk on 21/10/15.
 */
public class CleanerBase {

    protected BufferedWriter out;
    protected String inputFile;
    protected String[] selectedFeatures;
    protected String outputFile;

    protected static final String SEPARATOR = "\t";

    public CleanerBase(String inputFile, String[] selectedFeatures, String outputFile) {
        this.inputFile = inputFile;
        this.selectedFeatures = selectedFeatures;
        this.outputFile = outputFile;
    }


    protected String arrayToString(String[] array) {
        String line = "";
        for(String s : array) {
            line = line + SEPARATOR + s;
        }
        return line.trim() + "\n";
    }

    protected int findIndex(String[] features, String value) {
        for(int i = 0; i < features.length; i++) {
            if(features[i].equalsIgnoreCase(value))
                return i;
        }
        return -1;
    }

    protected void openWriteFile() {

        FileWriter fStream = null;
        try {
            fStream = new FileWriter(outputFile);
            out = new BufferedWriter(fStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void closeFile() {
        if(out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Write the parsed input file
     *
     * @param fn
     */
    protected void writeToFile(String fn) {
        try {
            out.write(fn);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package app.service;

import app.core.JP2Downloader;
import app.core.JP2DownloaderParallel;
import app.models.Event;
import app.utils.EventReader;
import app.utils.Utilities;

import java.text.ParseException;
import java.util.List;

/**
 * Created by ahmetkucuk on 13/10/15.
 */
public class JP2DownloaderService {

    /**
     * Main donwloader function
     *
     * @param inputFile
     * @param outputFileDir
     * @param numberOfItemToDownload
     * @param offset
     * @param parallel
     */
    public void downloadImageFromFile(String inputFile, String outputFileDir, int numberOfItemToDownload, int offset, boolean parallel) {

        if(parallel) {
            new JP2DownloaderParallel(inputFile, outputFileDir).downloadFromInputFile(numberOfItemToDownload, offset, 3);
        } else {
            new JP2Downloader(inputFile, outputFileDir).downloadFromInputFile(numberOfItemToDownload, offset, 3);
        }

    }

}

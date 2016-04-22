package app.service;

import app.core.JP2Downloader;
import app.core.JP2DownloaderParallel;
import app.models.Tuple2;

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
    public void downloadImageList(List<Tuple2<Integer, String>> inputList, String outputFileDir, int numberOfItemToDownload, int offset, boolean parallel) {

        if(parallel) {
            new JP2DownloaderParallel(inputList, outputFileDir).downloadFromList(numberOfItemToDownload, offset, 3);
        } else {
            new JP2Downloader(inputList, outputFileDir).downloadFromList(numberOfItemToDownload, offset, 3);
        }

    }

}

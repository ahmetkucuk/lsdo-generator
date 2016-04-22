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
     * Main downloader function
     *
     * @param inputList List of record to be downloaded
     * @param outputFileDir where do you want to save your images
     * @param numberOfItemToDownload how many of the items should be downloaded
     * @param offset skip that much item from beginning of the list
     * @param parallel calls parallel version
     */
    public void downloadImageList(List<Tuple2<Integer, String>> inputList, String outputFileDir, int numberOfItemToDownload, int offset, boolean parallel) {

        if(parallel) {
            new JP2DownloaderParallel(inputList, outputFileDir).downloadFromList(numberOfItemToDownload, offset, 3);
        } else {
            new JP2Downloader(inputList, outputFileDir).downloadFromList(numberOfItemToDownload, offset, 3);
        }

    }

}

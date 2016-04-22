# jp2-module


List of image to be downloaded. Example: 
List<Tuple2<Integer, String>> listToDownload = new ArrayList<>();

//Wavelength and date
listToDownload.add(new Tuple2<>(171, "2014-01-01T23:59:59Z"));

//Call downloader service. Definition of parameters are listed:

/**
 * Main downloader function
 *
 * @param inputList List of record to be downloaded
 * @param outputFileDir where do you want to save your images
 * @param numberOfItemToDownload how many of the items should be downloaded
 * @param offset skip that much item from beginning of the list
 * @param parallel calls parallel version
 */

new JP2DownloaderService().downloadImageList(listToDownload, FINAL_DATA_IMAGE_OUTPUT, 10, 0, false);

Parallel version is net tested on this branch.


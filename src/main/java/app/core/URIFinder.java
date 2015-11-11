package app.core;

import app.models.Event;
import app.utils.*;
import app.utils.FileWriter;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by ahmetkucuk on 07/11/15.
 */
public class URIFinder {

    ExecutorService executor;

    public URIFinder() {
        executor = Executors.newFixedThreadPool(15);

    }

    public void getJPIPUriNameFromFile(String file, String outputFile) {

        EventReader.init(file);
        Event event = null;
        int counter = 0;



        List<Event> eventList = new ArrayList<>();
        List<Future<HttpResponse<String>>> futureList = new ArrayList<>();

        while((event = EventReader.getInstance().next()) != null) {

            if(counter % 1000 == 0)
                System.out.println("Do not worry still going: " + counter);

            String sFileUrl = getUrlFor(event.getStartDate(), event.getMeasurement());
            String mFileUrl = getUrlFor(event.getMiddleDate(), event.getMeasurement());
            String eFileUrl = getUrlFor(event.getEndDate(), event.getMeasurement());
            eventList.add(event);

            event.setsFileName(cutFromLastSlash(getFileName(sFileUrl)));
            event.setmFileName(cutFromLastSlash(getFileName(mFileUrl)));
            event.seteFileName(cutFromLastSlash(getFileName(eFileUrl)));

//            executor.submit(generateRunnable(sFileUrl, "S", event));
//            executor.submit(generateRunnable(mFileUrl, "M", event));
//            executor.submit(generateRunnable(eFileUrl, "E", event));
            futureList.add(findAndSetFileName(sFileUrl, "S", event));
            futureList.add(findAndSetFileName(mFileUrl, "M", event));
            futureList.add(findAndSetFileName(eFileUrl, "E", event));

            if(futureList.size() >= 15) {
                waitUntilAllDone(futureList);
//                waitUntilOneDone(futureList);
            }
            counter++;
        }
        System.out.println("finished creating threads");
//
//        try {
//            executor.shutdown();
//            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        FileWriter writer = new FileWriter(outputFile);
        writer.start();
        writer.writeToFile("id\tevent_type\tstart_time\tend_time\tchannel\tbbox\tsfilename\tmfilename\tefilename\n");
        for(Event e : eventList) {
            writer.writeToFile(e.toString() + "\t" + e.getsFileName() + "\t" + e.getmFileName() + "\t" + e.geteFileName() + "\n");
        }
        writer.finish();
    }

    private void waitUntilAllDone(List<Future<HttpResponse<String>>> futureList) {

        while(true) {

            Iterator<Future<HttpResponse<String>>> iterator = futureList.iterator();

            boolean isAllDone = true;
            while (iterator.hasNext()) {
                if(!iterator.next().isDone()) {
                    isAllDone = false;
                } else {
                    iterator.remove();
                }
            }

            if(isAllDone)
                break;
        }
    }

    private static String cutFromLastSlash(String uri) {
        return uri.substring(uri.lastIndexOf("/") + 1, uri.lastIndexOf(".jp2"));
    }

    private String getUrlFor(Date date, String measurement) {

        String eventTime = Utilities.getStringFromDate(date);
        return String.format(Constants.IMAGE_JPIP_URI_DOWNLOAD_URL, eventTime, measurement);
    }

    public String getFileName(String sourceUrl) {

        URL imageUrl = null;
        try {
            imageUrl = new URL(sourceUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(imageUrl.openStream()));)
        {
            String line = null;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                builder.append(line);
            }
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Future<HttpResponse<String>> findAndSetFileName(String url, String eventTimeType, Event event) {

        Future<HttpResponse<String>> asyncRequest = Unirest.get(url).asStringAsync(new Callback<String>() {
            @Override
            public void completed(HttpResponse<String> httpResponse) {
                if(eventTimeType.equalsIgnoreCase("S"))
                    event.setsFileName(cutFromLastSlash(httpResponse.getBody()));
                if(eventTimeType.equalsIgnoreCase("M"))
                    event.setmFileName(cutFromLastSlash(httpResponse.getBody()));
                if(eventTimeType.equalsIgnoreCase("E"))
                    event.seteFileName(cutFromLastSlash(httpResponse.getBody()));
            }

            @Override
            public void failed(UnirestException e) {

                e.printStackTrace();
                System.out.println("Failed for " + url + " " + event.toString());
            }

            @Override
            public void cancelled() {
                System.out.println("Canceled for " + event.toString());

            }
        });
        return asyncRequest;
    }

    public Runnable generateRunnable(String url, String eventTimeType, Event event) {

        return () -> {

            if(eventTimeType.equalsIgnoreCase("M"))
                event.setsFileName(cutFromLastSlash(getFileName(url)));
            if(eventTimeType.equalsIgnoreCase("M"))
                event.setmFileName(cutFromLastSlash(getFileName(url)));
            if(eventTimeType.equalsIgnoreCase("E"))
                event.seteFileName(cutFromLastSlash(getFileName(url)));
        };
    }
}

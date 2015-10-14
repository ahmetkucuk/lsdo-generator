package app.core;

import app.models.Coordinate;
import app.models.Event;
import app.utils.EventFileReader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by ahmetkucuk on 27/09/15.
 */
public class DrawPolygonOnImage {


    public void draw(String inputFile, String eventTimeType, String imageFileDirectory, int limit, String targetFileExtension) {
        EventFileReader.init(inputFile);
        for(int i = 1; i <= limit; i++) {
            Event e = EventFileReader.getInstance().next();
            drawPolygon(e, imageFileDirectory, targetFileExtension);
        }
    }


    /**
     *
     *
     */
    public void drawPolygon(Event event, String outputFileDirectory, String targetFileExtension) {
        BufferedImage img = null;
        try {
            String fileName = outputFileDirectory + event.getImageFileName("S");
            System.out.println(fileName);
            img = ImageIO.read(new FileInputStream(new File(fileName)));
            Polygon p = createPolygon(event.getCoordinates());
            Graphics2D g2d = img.createGraphics();
            g2d.setPaint(Color.white);
            g2d.setStroke(new BasicStroke(10f));
            g2d.drawPolygon(p);
            g2d.dispose();
            ImageIO.write(img, targetFileExtension, new File(fileName));

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error: " + e);
        }
    }

    public Polygon createPolygon(Coordinate[] coordinates) {
        int[] xCoordinates = new int[coordinates.length];
        int[] yCoordinates = new int[coordinates.length];

        for(int i = 0; i < coordinates.length; i++) {
            xCoordinates[i] = (int)coordinates[i].getX();
            yCoordinates[i] = (int)coordinates[i].getY();
        }

        return new Polygon(xCoordinates, yCoordinates, xCoordinates.length);
    }

}

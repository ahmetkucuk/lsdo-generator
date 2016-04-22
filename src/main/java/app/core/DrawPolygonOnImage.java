package app.core;

import app.models.Event;
import app.utils.EventReader;
import com.vividsolutions.jts.geom.Coordinate;

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


    public void draw(String inputFile, String eventTimeType, String imageFileDirectory, int limit) {
        EventReader eventReader = new EventReader(inputFile);
        for(int i = 0; i < limit; i++) {
            Event e = eventReader.next();
            drawPolygonOfEvent(e, eventTimeType, imageFileDirectory);
        }
    }


    public void drawPolygonOfEvent(Event event, String eventTimeType, String outputFileDirectory) {

        String fileName = outputFileDirectory + eventTimeType + "_" + event.getImageFileName() + ".jpg";
        System.out.println(fileName);
        Polygon p = DrawPolygonOnImage.createPolygon(event.getCoordinates());
        drawPolygon(fileName, p);
    }

    public void drawPolygonByImageName(Event event, String imageFilePath) {

        Polygon p = DrawPolygonOnImage.createPolygon(event.getCoordinates());
        drawPolygon(imageFilePath, p);
    }

    protected void drawPolygon(String fileString, Polygon polygon) {

        BufferedImage img;

        try {
            img = ImageIO.read(new FileInputStream(new File(fileString)));
            System.out.println(img.getMinX() + " " + img.getColorModel());
            Graphics2D g2d = img.createGraphics();
            g2d.setPaint(Color.red);
            g2d.setStroke(new BasicStroke(10f));
            g2d.drawPolygon(polygon);
            g2d.dispose();
            ImageIO.write(img, "jpg", new File(fileString));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Polygon createPolygon(Coordinate[] coordinates) {
        int[] xCoordinates = new int[coordinates.length];
        int[] yCoordinates = new int[coordinates.length];

        for(int i = 0; i < coordinates.length; i++) {
            xCoordinates[i] = (int)coordinates[i].x;
            yCoordinates[i] = (int)coordinates[i].y;
        }

        return new Polygon(xCoordinates, yCoordinates, xCoordinates.length);
    }

}

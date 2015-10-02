package app.polygon;

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


    /**
     *
     *
     */
    public void drawPolygon(app.Event event) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new FileInputStream(new File(event.getImageFileString())));
            Polygon p = createPolygon(event.getCoordinates());
            Graphics2D g2d = img.createGraphics();
            g2d.setPaint(Color.BLUE);
            g2d.setStroke(new BasicStroke(10f));
            g2d.drawPolygon(p);
            ImageIO.write(img, "png", new File(event.getImageFilePNGString()));

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error: " + e);
        }
    }

    public Polygon createPolygon(Coordinate[] coordinates) {
        int[] xCoordinates = new int[coordinates.length];
        int[] yCoordinates = new int[coordinates.length];

        for(int i = 0; i < coordinates.length; i++) {
            xCoordinates[i] = coordinates[i].getPixelX();
            yCoordinates[i] = coordinates[i].getPixelY();
            System.out.println(xCoordinates[i] + " " + yCoordinates[i]);
        }

        return new Polygon(xCoordinates, yCoordinates, xCoordinates.length);
    }

}

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by ahmetkucuk on 27/09/15.
 */
public class DrawPolygon {


    /**
     *
     *
     * @param file
     * @param polygonValues
     */
    public void drawPolygon(String file, String polygonValues) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new FileInputStream(new File(file)));
            String justNumbers = polygonValues.substring(9, polygonValues.length()-2);
            System.out.println(justNumbers);

            Polygon p = getPolygonFromString(justNumbers);
            Graphics2D g2d = img.createGraphics();
            g2d.setColor(Color.black);
            g2d.fillPolygon(p);
            ImageIO.write(img, "jpg", new File(file));

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error: " + e);
        }
    }

    public Polygon getPolygonFromString(String coordinates) {
        String[] numbers = coordinates.split(",");
        int[] xCoordinates = new int[numbers.length];
        int[] yCoordinates = new int[numbers.length];

        for(int i = 0; i < numbers.length; i++) {
            String[] coordinate = numbers[i].split(" ");
            xCoordinates[i] = (int)Math.round(Double.parseDouble(coordinate[0]));
            yCoordinates[i] = (int)Math.round(Double.parseDouble(coordinate[1]));
        }

        return new Polygon(xCoordinates, yCoordinates, xCoordinates.length);
    }
}

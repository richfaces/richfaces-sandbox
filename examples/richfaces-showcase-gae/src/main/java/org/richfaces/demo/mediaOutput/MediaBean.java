package org.richfaces.demo.mediaOutput;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.imageio.ImageIO;

@ManagedBean(name = "mediaBean")
@SessionScoped
public class MediaBean implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -4483889108870388747L;

    public void paint(OutputStream out, Object data) throws IOException {
        if (data instanceof MediaData) {
            MediaData paintData = (MediaData) data;
            BufferedImage img = new BufferedImage(paintData.width, paintData.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = img.createGraphics();

            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, 300, 120);

            int testLenght = paintData.text.length();
            int fontSize = testLenght < 8 ? 40 : 40 - (testLenght - 8);

            if (fontSize < 12) {
                fontSize = 12;
            }

            Font font = new Font("Serif", Font.HANGING_BASELINE, fontSize);

            g2d.setFont(font);

            int x = 10;
            int y = fontSize * 5 / 2;

            g2d.translate(x, y);

            Color color = new Color(paintData.color);

            g2d.setPaint(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));

            AffineTransform origTransform = g2d.getTransform();

            g2d.shear(-0.5 * paintData.scale, 0);
            g2d.scale(1, paintData.scale);
            g2d.drawString(paintData.text, 0, 0);
            g2d.setTransform(origTransform);
            g2d.setPaint(color);
            g2d.drawString(paintData.text, 0, 0);
            ImageIO.write(img, "jpeg", out);
        }
    }

    private void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[2048];
        int read;

        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public void paintFlash(OutputStream out, Object data) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        if (loader == null) {
            loader = getClass().getClassLoader();
        }

        InputStream stream = loader.getResourceAsStream("org/richfaces/demo/mediaOutput/text.swf");

        if (stream != null) {
            try {
                copy(stream, out);
            } finally {
                stream.close();
            }
        }
    }
}

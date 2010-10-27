package org.richfaces.renderkit.html.images;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.richfaces.resource.DynamicResource;
import org.richfaces.resource.ImageType;
import org.richfaces.resource.Java2DUserResource;
import org.richfaces.resource.PostConstructResource;
import org.richfaces.resource.StateHolderResource;
import org.richfaces.skin.Skin;
import org.richfaces.skin.SkinFactory;

/**
 * @author amarkhel
 *
 */
@DynamicResource
public class CalendarIcon implements Java2DUserResource, StateHolderResource{

    private static final Dimension DIMENSION = new Dimension(20, 20);
    
    private Integer headerTextColor;
    
    private Integer headerBackgroundColor;
    
    
    @PostConstructResource
    public final void initialize() {
        FacesContext context = FacesContext.getCurrentInstance();
        Skin skin = SkinFactory.getInstance(context).getSkin(context);
        
        this.headerTextColor = skin.getColorParameter(context, Skin.HEAD_TEXT_COLOR);
        this.headerBackgroundColor = skin.getColorParameter(context, Skin.HEADER_BACKGROUND_COLOR);
    }

    
    public boolean isTransient() {
        return false;
    }

    public void writeState(FacesContext context, DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.headerTextColor);
        dataOutput.writeInt(this.headerBackgroundColor);
    }

    public void readState(FacesContext context, DataInput dataInput) throws IOException {
        this.headerTextColor = dataInput.readInt();
        this.headerBackgroundColor = dataInput.readInt();
    }

    public Map<String, String> getResponseHeaders() {
        return null;
    }

    public Date getLastModified() {
        return null;
    }

    public ImageType getImageType() {
        return ImageType.PNG;
    }

    public Dimension getDimension() {
        return DIMENSION;
    }
    
    protected BufferedImage createImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public void paint(Graphics2D graphics2d, Dimension dimension) {
        BufferedImage image = paintImage();
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics2d.drawImage(image, 0, 0, dimension.width, dimension.height, null);
    }
    
    public BufferedImage paintImage() {
        
        BufferedImage image = createImage(16, 16);

        Graphics2D g2d = image.createGraphics();

        Color borderColor = new Color(this.headerTextColor);
        Color activeColor = new Color(this.headerBackgroundColor);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

        g2d.setStroke(new BasicStroke(1));

        int w = 16;
        int h = 16;

        //Draw Border
        g2d.setColor(borderColor);
        Rectangle2D border = new Rectangle2D.Double(1, 1, w - 3, h - 3);
        RoundRectangle2D round = new RoundRectangle2D.Double(1, 1, w- 3, h - 3, 2, 2);
        g2d.draw(round);

        Color lightBlue = new Color(216, 226, 240);
        Paint gradient1 = new GradientPaint(w-4, h-4, lightBlue, 2, 2, Color.white);
        g2d.setPaint(gradient1);
        border = new Rectangle2D.Double(2, 2, w - 4, h - 4);
        g2d.fill(border);

        border = new Rectangle2D.Double(3, 3, w - 6, h - 6);
        gradient1 = new GradientPaint(3, 3, lightBlue , w - 6, h - 6, borderColor);
        g2d.setPaint(gradient1);
        g2d.fill(border);

        g2d.setColor(Color.white);
        g2d.drawLine(3, 6, 3, 11);
        g2d.drawLine(5, 6, 5, 11);
        g2d.drawLine(7, 6, 7, 11);
        g2d.drawLine(9, 6, 9, 11);
        g2d.drawLine(11, 6, 11, 11);

        //Draw orange rectangle
        border = new Rectangle2D.Double(3, 3, 10, 3);
        g2d.setColor(Color.white);
        g2d.fill(border);

        Color c = new Color(activeColor.getRed(), activeColor.getGreen(), activeColor.getBlue(), 100);
        Color c2 = new Color(activeColor.getRed(), activeColor.getGreen(), activeColor.getBlue(), 200);

        gradient1 = new GradientPaint(12, 4, activeColor, 4, 7, c2);
        g2d.setPaint(gradient1);
        g2d.fill(border);
        //g2d.setColor(activeColor);

        c = new Color(activeColor.getRed(), activeColor.getGreen(), activeColor.getBlue(), 150);
        c2 = new Color(activeColor.getRed(), activeColor.getGreen(), activeColor.getBlue(), 200);
        border = new Rectangle2D.Double(4, 4, 8, 1);

        g2d.setColor(Color.white);
        g2d.fill(border);

        gradient1 = new GradientPaint(4, 4, c, 10, 4, c2);
        //g2d.setPaint(gradient1);
        g2d.setColor(c);
        g2d.fill(border);

        g2d.dispose();
        
        return image;
    }
}

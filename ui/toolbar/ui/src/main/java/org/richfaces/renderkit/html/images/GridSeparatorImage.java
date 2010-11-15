package org.richfaces.renderkit.html.images;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

import org.richfaces.resource.DynamicUserResource;

@DynamicUserResource
public class GridSeparatorImage extends ToolBarSeparatorImage {

    public Dimension getDimension() {
        return calculateDimension();
    }
    
    public void paint(Graphics2D g2d) {
        Dimension dimensions = calculateDimension();
        
        BufferedImage texture = new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB);
        Graphics2D txG2d = texture.createGraphics();
        txG2d.setColor(new Color(this.getHeaderBackgroundColor()));
        txG2d.fillRect(0, 0, 2, 2);
        txG2d.setColor(new Color(255, 255, 255, 150));
        txG2d.fillRect(0, 0, 1, 1);
        txG2d.dispose();
        g2d.setPaint(new TexturePaint(texture, new Rectangle(1, 1, 3, 3)));
        g2d.fillRect(0, 0, dimensions.width, dimensions.height);
    }
    
    private Dimension calculateDimension() {
        int h = (int)(this.getSeparatorHeight() * 0.8);
        h = h - h % 3;
        int w = 9;
        return new Dimension(w, h);
    }
}
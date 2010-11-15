package org.richfaces.renderkit.html.images;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import org.richfaces.resource.DynamicUserResource;

@DynamicUserResource
public class LineSeparatorImage extends ToolBarSeparatorImage {

    public Dimension getDimension() {
        return calculateDimension();
    }

    public void paint(Graphics2D g2d) {
        Dimension dimensions = calculateDimension();
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(new Color(this.getHeaderBackgroundColor()));
        g2d.fillRect(-1, -1, dimensions.width + 2, dimensions.height + 2);
        g2d.setColor(new Color(255, 255, 255, 150));
        g2d.drawLine(1, -1, 1, dimensions.height + 2);
    }
    
    private Dimension calculateDimension() {
        int h = this.getSeparatorHeight();     
        int w = 2;
        return new Dimension(w, h);
    }
}

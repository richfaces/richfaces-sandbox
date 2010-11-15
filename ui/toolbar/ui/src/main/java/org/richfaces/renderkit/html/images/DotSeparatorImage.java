/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package org.richfaces.renderkit.html.images;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

import org.richfaces.resource.DynamicUserResource;

/**
 * @author Maksim Kaszynski
 * 
 */
@DynamicUserResource
public class DotSeparatorImage extends ToolBarSeparatorImage {

    private static final Dimension DIMENSIONS = new Dimension(9, 9);

    public Dimension getDimension() {
        return DIMENSIONS;
    }

    public void paint(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color gradientColorStart = Color.WHITE;
        Color gradientColorEnd = new Color(this.getHeaderBackgroundColor());
        Ellipse2D inSquare = new Ellipse2D.Double(2, 2, DIMENSIONS.getWidth() - 4, DIMENSIONS.getHeight() - 4);
        GradientPaint paint = new GradientPaint((float) 3, (float) 3, gradientColorStart,
                (float) DIMENSIONS.getWidth() - 2, (float) DIMENSIONS.getHeight() - 2, gradientColorEnd);
        g2d.setPaint(paint);
        g2d.fill(inSquare);

        Ellipse2D outSquare = new Ellipse2D.Double(0, 0, DIMENSIONS.getWidth(), DIMENSIONS.getHeight());
        Ellipse2D midSquare = new Ellipse2D.Double(1, 1, DIMENSIONS.getWidth() - 2, DIMENSIONS.getHeight() - 2);

        g2d.setColor(new Color(this.getHeaderBackgroundColor()));
        Area area2 = new Area(outSquare);
        area2.subtract(new Area(midSquare));
        g2d.fill(area2);

        g2d.setColor(new Color(this.getHeaderGradientColor()));
        Area area = new Area(midSquare);
        area.subtract(new Area(inSquare));
        g2d.fill(area);
    }
}

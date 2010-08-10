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
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.faces.context.FacesContext;

import org.ajax4jsf.resource.ResourceContext;

public class SpinnerButtonDown extends SpinnerButtonImage {
	private static final Dimension dimensions = new Dimension(14, 7);
	
	protected Dimension getDimensions(ResourceContext resourceContext) {
		return dimensions;
	}
	public Dimension getDimensions(FacesContext facesContext, Object data) {
		return dimensions;
	}

	protected void paint(ResourceContext context, Graphics2D g2d) {
		Color[] color = (Color []) restoreData(context);
		Color triangleColor = color[0];
		Color borderColor  = color[1];
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	
		g2d.setColor(triangleColor);
		g2d.drawLine(5,2,9,2);
		g2d.drawLine(6,3,8,3);
		g2d.drawLine(7,4,7,4);
		
		g2d.setColor(borderColor);
		g2d.drawLine(5, 1, 9, 1);
		g2d.drawLine(10, 2, 7, 5);
		g2d.drawLine(7, 5, 4, 2);
		
		
	}
	

}

/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
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
import java.util.Date;

import javax.faces.context.FacesContext;

import org.ajax4jsf.resource.GifRenderer;
import org.ajax4jsf.resource.InternetResourceBuilder;
import org.ajax4jsf.resource.Java2Dresource;
import org.ajax4jsf.resource.ResourceContext;
import org.ajax4jsf.util.HtmlColor;
import org.ajax4jsf.util.Zipper2;

/**
 * implementation of the default OK icon renderer 
 * @author Anton Belevich
 * @since 3.2.0
 *
 */
public class SaveControlIcon extends Java2Dresource {
	
	protected static final String ICON_COLOR = "#5BC248"; 
	protected static final String ICON_SHADOW = "#006406";
	protected static final String ICON_BORDER = "#FFFFFF";
	
	private static final Dimension dimensions = new Dimension(11, 11);

	public Dimension getDimensions(FacesContext facesContext, Object data) {
		return dimensions;
	}
	
	
	protected Dimension getDimensions(ResourceContext resourceContext) {
		return dimensions;
	}
	public SaveControlIcon() {
		setRenderer(new GifRenderer());
		setLastModified(new Date(InternetResourceBuilder.getInstance().getStartTime()));
	}
	
	protected Object deserializeData(byte[] objectArray) {
		if (objectArray == null) {
			return null;
		}
		Zipper2 zipper = new Zipper2(objectArray);
		return new Color[] {zipper.nextColor(), zipper.nextColor(), zipper.nextColor()};
	}	
	
	protected Object getDataToStore(FacesContext context, Object data){
		
		byte [] ret = new byte[9];
			
		Color color = null;
		Zipper2 zipper = new Zipper2(ret);
				
		color = HtmlColor.decode(ICON_COLOR);
		zipper.addColor(color);
			
		color = HtmlColor.decode(ICON_SHADOW);
		zipper.addColor(color);
		
		color = HtmlColor.decode(ICON_BORDER);
		zipper.addColor(color);
			
		return ret;
	}
	
	protected void paint(ResourceContext context, Graphics2D g2d) {
		Color [] data = (Color[]) restoreData(context);
		
		Color iconColor = data[0];
		Color iconShadow = data[1];
		Color borderColor = data[2];
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
		g2d.setColor(iconColor);
		g2d.drawLine(1, 6, 4, 9);
		g2d.drawLine(4, 9, 4, 8);
		g2d.drawLine(4, 8, 2, 6);
		g2d.drawLine(2, 5, 5, 8);
		g2d.drawLine(5, 8, 5, 6);
		g2d.drawLine(5, 6, 9, 2);
		g2d.drawLine(9, 2, 9, 3);
		g2d.drawLine(9, 3, 5, 7);

		//draw shadow
		g2d.setColor(iconShadow);
		g2d.drawLine(3, 5, 4, 6);
		g2d.drawLine(5, 9, 6, 8);
		g2d.drawLine(6, 8, 6, 7);
		g2d.drawLine(6, 7, 9, 4);
		
		// draw border
		g2d.setColor(borderColor);
		g2d.drawLine(0, 6, 4, 10);
		g2d.drawLine(4, 10, 5,10);
		g2d.drawLine(5,10,7,8);
		g2d.drawLine(7,8,7,7);
		g2d.drawLine(7,7,10,4);
		g2d.drawLine(10,4,10,1);
		g2d.drawLine(10,1,9,1);
		g2d.drawLine(9,1,5,5);
		g2d.drawLine(5,5,4,5);
		g2d.drawLine(4,5,3,4);
		g2d.drawLine(3,4,2,4);
		g2d.drawLine(2,4,0,6);
		
		
	
	}
}

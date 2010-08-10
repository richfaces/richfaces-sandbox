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
import java.util.Date;

import javax.faces.context.FacesContext;

import org.ajax4jsf.resource.GifRenderer;
import org.ajax4jsf.resource.InternetResourceBuilder;
import org.ajax4jsf.resource.Java2Dresource;
import org.ajax4jsf.resource.ResourceContext;
import org.ajax4jsf.util.HtmlColor;
import org.ajax4jsf.util.Zipper2;

/** 
 * implementation of the EDIT icon renderer
 * @author Anton Belevich
 * @since 3.2.0
 *
 */
public class EditIcon extends Java2Dresource {
	
	protected static final String ICON_COLOR = "#FF0000";
	
	private static final Dimension dimensions = new Dimension(4, 4);

	public EditIcon() {
		setRenderer(new GifRenderer());
		setLastModified(new Date(InternetResourceBuilder.getInstance().getStartTime()));
	}
	
	public Dimension getDimensions(FacesContext facesContext, Object data) {
		return dimensions;
	}
	
	protected Dimension getDimensions(ResourceContext resourceContext) {
		return dimensions;
	}
	
	protected Object deserializeData(byte[] objectArray) {
		if (objectArray == null) {
			return null;
		}
		Zipper2 zipper = new Zipper2(objectArray);
		return new Color[] {zipper.nextColor()};
	}	
	
	protected Object getDataToStore(FacesContext context, Object data){
		
		byte [] ret = new byte[3];
			
		Color color = null;
		Zipper2 zipper = new Zipper2(ret);
				
		color = HtmlColor.decode(ICON_COLOR);
		zipper.addColor(color);
			
		return ret;
	}
	
	protected void paint(ResourceContext context, Graphics2D g2d) {
		Color [] data = (Color[]) restoreData(context);
		Color iconColor = data[0];
		
		g2d.setColor(iconColor);
		g2d.drawLine(0, 0, 0, 4);
		g2d.drawLine(0, 0, 4, 0);
		g2d.drawLine(1, 0, 1, 2);
		g2d.drawLine(2, 0, 2, 1);
	}
}

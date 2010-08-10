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
import java.util.Date;

import javax.faces.context.FacesContext;

import org.ajax4jsf.resource.GifRenderer;
import org.ajax4jsf.resource.InternetResourceBuilder;
import org.ajax4jsf.resource.Java2Dresource;
import org.ajax4jsf.resource.ResourceContext;

public abstract class SliderArrowBase  extends Java2Dresource{
	private static final Dimension dimensions = new Dimension(7, 8);

	public SliderArrowBase() {
		setRenderer(new GifRenderer());
		setLastModified(new Date(InternetResourceBuilder.getInstance().getStartTime()));
	}
	
	protected Dimension getDimensions(ResourceContext resourceContext) {
		return dimensions;
	}
	public Dimension getDimensions(FacesContext facesContext, Object data) {
		return dimensions;
	}

	protected void paint(ResourceContext context, Graphics2D g2d) {
		Integer color = (Integer) restoreData(context);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(new Color(color.intValue()));
		g2d.drawLine(0,2,6,2);
		g2d.drawLine(1,3,5,3);
		g2d.drawLine(2,4,4,4);
		g2d.drawLine(3,5,3,5);
	}
	
	public boolean isCacheable() {
		return true;
	}

}

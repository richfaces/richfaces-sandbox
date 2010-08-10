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
import org.richfaces.skin.Skin;
import org.richfaces.skin.SkinFactory;

/**
 * @author Anton Belevich
 * @since 3.2.0
 * ComboBox down image renderer
 */
public class ComboBoxArrowImage extends Java2Dresource{
	
	
	protected static final String ICON_COLOR = "generalTextColor";
	protected static final String BACKGROUND_COLOR = "tabBackgroundColor";
	protected static final String BORDER_COLOR = "generalBackgroundColor";
	
	protected static final String DISABLED_ICON_COLOR = "tabDisabledTextColor";
	protected static final String DISABLED_BACKGROUND_COLOR = "tabBackgroundColor";
	protected static final String DISABLED_BORDER_COLOR = "generalBackgroundColor";
	
	private static final Dimension dimensions = new Dimension(15, 15);
	
	public ComboBoxArrowImage() {
		setRenderer(new GifRenderer());
		setLastModified(new Date(InternetResourceBuilder.getInstance().getStartTime()));
	}
		
	protected Dimension getDimensions(ResourceContext resourceContext) {
		return dimensions;
	}
	public Dimension getDimensions(FacesContext facesContext, Object data) {
		return dimensions;
	}
	
	protected Object deserializeData(byte[] objectArray) {
		if (objectArray == null) {
			return null;
		}
		Zipper2 zipper = new Zipper2(objectArray);
		return new Color[] {zipper.nextColor(), zipper.nextColor(), zipper.nextColor()};
	}	
	
	protected Object getDataToStore(FacesContext context, Object data) {
		return storeData(context, ICON_COLOR, BACKGROUND_COLOR, BORDER_COLOR);
	}
	
	protected Object storeData(FacesContext context, String colorSkinParam, String backgroundSkinParam, String borderSkinParam) {
		
		Skin skin = SkinFactory.getInstance().getSkin(context);
		Skin defaultSkin = SkinFactory.getInstance().getDefaultSkin(context);
			
		byte [] ret = new byte[9];
			
		Color color = null;
		Zipper2 zipper = new Zipper2(ret);
			
		String color1 = (String) skin.getParameter(context, colorSkinParam);
		
		if (null == color1 || "".equals(color1)) {
			color1 = (String) defaultSkin.getParameter(context, colorSkinParam);
		}	
			
		if (color1 == null) {
			color1 = "#FFFFFF";
		}
			
		color = HtmlColor.decode(color1);
			
		zipper.addColor(color);
			
		String color2 = (String) skin.getParameter(context, backgroundSkinParam);
		if (null == color2 || "".equals(color2))
			color2 = (String) defaultSkin.getParameter(context, backgroundSkinParam);
			
		if (color2 == null) {
			color2 = "#000000";
		}
			
		color = HtmlColor.decode(color2);
		zipper.addColor(color);
			
		String color3 = (String) skin.getParameter(context, borderSkinParam);
		if (null == color3 || "".equals(color3))
			color3 = (String) defaultSkin.getParameter(context, backgroundSkinParam);
			
		if (color3 == null) {
			color3 = "#000000";
		}
			
		color = HtmlColor.decode(color3);
		zipper.addColor(color);
			
		return ret;
	}
		
	protected void paint(ResourceContext context, Graphics2D g2d) {
		Color [] data = (Color[]) restoreData(context);
		Color textColor = data[0];
//		Color backgroundColor = data[1];
		Color borderColor = data[2];
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		if (textColor != null && borderColor != null && g2d != null) {
	
			g2d.setColor(textColor);
			g2d.translate(7, 5);
			paintBaseTriangle(g2d);
			g2d.translate(-7, -5);
	
			g2d.setColor(borderColor);
			g2d.drawLine(4, 5, 10, 5);
			g2d.drawLine(11, 6, 7, 10);
			g2d.drawLine(7, 10, 3, 6);
		}	
	}
	
	protected void paintBaseTriangle(Graphics2D g2d) {
		for (int i = 0; i < 7; i++) {
			g2d.drawLine(-3 + i, 1, -3 + i, 1 + (i < 4 ? i : 6 - i));
		}
	}
}

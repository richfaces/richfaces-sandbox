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
import java.util.Date;

import javax.faces.context.FacesContext;

import org.ajax4jsf.resource.GifRenderer;
import org.ajax4jsf.resource.InternetResourceBuilder;
import org.ajax4jsf.resource.Java2Dresource;
import org.ajax4jsf.util.HtmlColor;
import org.ajax4jsf.util.Zipper2;
import org.richfaces.skin.Skin;
import org.richfaces.skin.SkinFactory;

public abstract class SpinnerButtonImage extends Java2Dresource {

	public SpinnerButtonImage() {
		setRenderer(new GifRenderer());
		setLastModified(new Date(InternetResourceBuilder.getInstance().getStartTime()));
	}
	
	protected Object deserializeData(byte[] objectArray) {
		if (objectArray == null) {
			return null;
		}
		Zipper2 zipper = new Zipper2(objectArray);
		return new Color[] {zipper.nextColor(), zipper.nextColor()};
	}
	
	protected Object getDataToStore(FacesContext context, Object data) {
		Skin skin = SkinFactory.getInstance().getSkin(context);
		byte[] ret = new byte[6];
		Zipper2 zipper = new Zipper2(ret);
		
		String skinParameterTextColor = "generalTextColor";
		String generalTextColor = (String) skin.getParameter(context, skinParameterTextColor);
		if (null == generalTextColor || "".equals(generalTextColor)) {
			Skin defaultSkin = SkinFactory.getInstance().getDefaultSkin(context);
			generalTextColor = (String) defaultSkin.getParameter(context, skinParameterTextColor);
		}
			
		Integer color = HtmlColor.decode( generalTextColor == null ? "#000000":generalTextColor ).getRGB();
			
		zipper.addColor(color);
		
		String skinParameterBackgroundColor = "controlBackgroundColor";
		String generalBackgroundColor = (String) skin.getParameter(context, skinParameterBackgroundColor);
		
		if (null == generalBackgroundColor || "".equals(generalBackgroundColor)) {
			Skin defaultSkin = SkinFactory.getInstance().getDefaultSkin(context);
			generalBackgroundColor = (String) defaultSkin.getParameter(context, skinParameterBackgroundColor);
		}
		
		color = HtmlColor.decode( generalTextColor == null ? "#FFFFFF":generalBackgroundColor ).getRGB();
		zipper.addColor(color);

		return ret;
	}
	
}

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

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.faces.context.FacesContext;

import org.ajax4jsf.util.HtmlColor;
import org.richfaces.skin.Skin;
import org.richfaces.skin.SkinFactory;

public class SliderArrowImage extends SliderArrowBase {
	protected Object getDataToStore(FacesContext context, Object data) {
		Skin skin = SkinFactory.getInstance().getSkin(context);
		
		String skinParameter = "generalTextColor";
		String tmp = (String) skin.getParameter(context, skinParameter);
		if (null == tmp || "".equals(tmp)) {
			Skin defaultSkin = SkinFactory.getInstance().getDefaultSkin(context);
			tmp = (String) defaultSkin.getParameter(context, skinParameter);
		}
		int intValue = HtmlColor.decode(tmp ==null? "#000000":tmp).getRGB();
		// Serialize data as byte[] 
		ByteBuffer buff = ByteBuffer.allocate(1*4);
		IntBuffer intBuffer = buff.asIntBuffer();
		intBuffer.put(intValue);
		return buff.array();
	}

	protected Object deserializeData(byte[] objectArray) {
		// restore color value from a byte[] array.
		int i = ByteBuffer.wrap(objectArray).asIntBuffer().get();
		return new Integer(i);
	}
}
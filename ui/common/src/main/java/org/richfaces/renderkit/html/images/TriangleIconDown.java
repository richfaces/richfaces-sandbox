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
import java.awt.Graphics2D;

import org.ajax4jsf.resource.ResourceContext;

/**
 * @author Siarhej Chalipau
 *
 */
public class TriangleIconDown extends TriangleIconBase {

	protected void paintImage(ResourceContext context, Graphics2D g2d,
			Color textColor, Color borderColor) {
		
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

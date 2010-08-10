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

package org.ajax4jsf.renderkit.html;

import java.io.IOException;
import java.util.ListIterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.ajax4jsf.component.UIRepeat;
import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.renderkit.RendererBase;

/**
 * @author shura
 * 
 */
public class RepeatRenderer extends RendererBase {

	public void encodeChildren(FacesContext context, UIComponent component)
			throws IOException {
		final UIRepeat repeater = (UIRepeat) component;
		repeater.captureOrigValue(context);
		try {
			DataVisitor visitor = new DataVisitor() {

				public void process(FacesContext context, Object rowKey, Object argument) throws IOException {
					repeater.setRowKey(rowKey);
					ListIterator childIterator = repeater.getChildren()
							.listIterator();
					while (childIterator.hasNext()) {
						UIComponent child = (UIComponent) childIterator.next();
						renderChild(context, child);
					}

				}

			};
			repeater.walk(context, visitor, null);

		} finally {
			repeater.restoreOrigValue(context);
			repeater.setRowKey(null);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ajax4jsf.renderkit.RendererBase#getComponentClass()
	 */
	protected Class getComponentClass() {
		// TODO Auto-generated method stub
		return UIRepeat.class;
	}

	public boolean getRendersChildren() {
		return true;
	}

}

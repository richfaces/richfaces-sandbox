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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.renderkit.AjaxContainerRenderer;


/**
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/02/01 15:31:28 $
 *
 */
public class AjaxRegionRenderer extends AjaxContainerRenderer {
	
    public static final String RENDERER_TYPE = "org.ajax4jsf.components.AjaxRegionRenderer";

	/* (non-Javadoc)
	 * @see org.ajax4jsf.renderkit.RendererBase#doEncodeBegin(javax.faces.context.ResponseWriter, javax.faces.context.FacesContext, javax.faces.component.UIComponent)
	 */
	protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
//		AjaxRendererUtils.encodeClientScript(context,component);
	}

	/* (non-Javadoc)
	 * @see org.ajax4jsf.renderkit.RendererBase#doEncodeEnd(javax.faces.context.ResponseWriter, javax.faces.context.FacesContext, javax.faces.component.UIComponent)
	 */
	protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
//        if (((AjaxContainer) component).isAjaxRequest()) {
//			AjaxRendererUtils.encodeAreas(context,component);
//		}
	}

}

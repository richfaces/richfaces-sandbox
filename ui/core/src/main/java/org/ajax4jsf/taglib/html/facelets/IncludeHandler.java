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

package org.ajax4jsf.taglib.html.facelets;

import java.io.IOException;

import javax.el.ELException;
import javax.el.VariableMapper;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;

import org.ajax4jsf.component.UIInclude;

/**
 * @author shura
 * 
 */
public class IncludeHandler extends ComponentHandler {

	private TagAttribute viewId;

	public IncludeHandler(ComponentConfig config) {
		super(config);
		this.viewId = getRequiredAttribute("viewId");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.facelets.tag.jsf.ComponentHandler#onComponentCreated(com.sun.facelets.FaceletContext,
	 *      javax.faces.component.UIComponent,
	 *      javax.faces.component.UIComponent)
	 */
	public void onComponentCreated(FaceletContext ctx, UIComponent c,
			UIComponent parent) {
		// TODO Auto-generated method stub
		super.onComponentCreated(ctx, c, parent);
	}

	public void applyNextHandler(FaceletContext ctx, UIComponent component)
			throws IOException, FacesException, ELException {
		String path;
			if (component instanceof UIInclude) {
				UIInclude include = (UIInclude) component;
				path = include.getViewId();
				 if(include.isWasNavigation()){
					 component.getChildren().clear();
				 }
			} else {
				path = (String) component.getAttributes().get("viewId");
			}
			VariableMapper orig = ctx.getVariableMapper();
			ctx.setVariableMapper(new VariableMapperWrapper(orig));
			try {
				this.nextHandler.apply(ctx, component);
				ctx.includeFacelet(component, path);
			} catch (Exception e) {
				throw new FacesException("UIInclude component "
						+ component.getClientId(ctx.getFacesContext())
						+ " could't include page with path " + path, e);
			} finally {
				ctx.setVariableMapper(orig);
			}
	}

}

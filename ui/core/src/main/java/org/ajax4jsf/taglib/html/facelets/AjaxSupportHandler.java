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
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.FaceletException;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagHandler;

import org.ajax4jsf.taglib.html.jsp.AjaxSupportTag;
import org.ajax4jsf.webapp.taglib.AjaxComponentHandler;

/**
 * "proxy" class for creating UIAjaxSupport component as facet for it's parent.
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/02/01 15:31:22 $
 *
 */
public class AjaxSupportHandler extends TagHandler {
    /**
     * A UIComponent for capturing a child UIComponent, representative of the
     * desired Facet
     * 
     * @author Jacob Hookom
     * 
     */
    private final static class UIFacet extends UIComponentBase {
        public String getFamily() {
            return null;
        }
    }

	/**
	 * Real tag handler for create component.
	 */
	private TagHandler _ajaxSupportHandler;
	
	private TagAttribute _event;
	/**
	 * @param config
	 */
	public AjaxSupportHandler(ComponentConfig config) {
		super(config);
		_event = getRequiredAttribute("event");
		_ajaxSupportHandler = new AjaxComponentHandler(config);
	}

	/* (non-Javadoc)
	 * @see com.sun.facelets.FaceletHandler#apply(com.sun.facelets.FaceletContext, javax.faces.component.UIComponent)
	 */
	public void apply(FaceletContext ctx, UIComponent parent)
			throws IOException, FacesException, FaceletException, ELException {
        UIComponent c;
        // our id
//        String id = ctx.generateUniqueId(this.tagId);
        UIFacet facet = new UIFacet();
        // Find facet for support component
        String facetName = AjaxSupportTag.AJAX_SUPPORT_FACET+_event.getValue();
		c = parent.getFacet(facetName);
        if (null != c) {
			parent.getFacets().remove(facetName);
			facet.getChildren().add(c);
		}
        this._ajaxSupportHandler.apply(ctx, facet);
        c = (UIComponent) facet.getChildren().get(0);
        parent.getFacets().put(facetName, c);
        // Fix for possible incompabilites in different frameworks.
//        c.setParentProperties(parent);
	}

}

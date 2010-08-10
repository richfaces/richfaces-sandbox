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
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.FaceletException;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagException;
import javax.faces.view.facelets.TagHandler;

import org.ajax4jsf.Messages;

/**
 * Register a named facet on the UIComponent associated with the closest parent
 * UIComponent custom action. <p/> See <a target="_new"
 * href="http://java.sun.com/j2ee/javaserverfaces/1.1_01/docs/tlddocs/f/facet.html">tag
 * documentation</a>.
 * 
 * @author Jacob Hookom
 * @version $Id: FacetHandler.java,v 1.1.2.1 2007/02/01 15:31:21 alexsmirnov Exp $
 */
public final class FacetHandler extends TagHandler {

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

    protected final TagAttribute name;

    public FacetHandler(TagConfig config) {
        super(config);
        this.name = this.getRequiredAttribute("name");
    }

    /* (non-Javadoc)
     * @see com.sun.facelets.FaceletHandler#apply(com.sun.facelets.FaceletContext, javax.faces.component.UIComponent)
     */
    public void apply(FaceletContext ctx, UIComponent parent)
            throws IOException, FacesException, FaceletException, ELException {
        UIFacet facet = new UIFacet();
        UIComponent c;
        // Check for existing facet
        String facetName = this.name.getValue(ctx);
		c = parent.getFacet(facetName);
        if (null != c) {
			parent.getFacets().remove(facetName);
			facet.getChildren().add(c);
		}
        this.nextHandler.apply(ctx, facet);
        int childCount = facet.getChildCount();
        if (childCount == 1) {
            c = (UIComponent) facet.getChildren().get(0);
            parent.getFacets().put(facetName, c);
        } else {
            throw new TagException(this.tag, Messages.getMessage(Messages.FACET_TAG_MANY_CHILDREN_ERROR));
        }
    }
}

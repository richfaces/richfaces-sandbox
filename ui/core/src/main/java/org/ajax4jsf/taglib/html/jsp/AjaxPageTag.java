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

package org.ajax4jsf.taglib.html.jsp;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;

import org.ajax4jsf.renderkit.html.AjaxPageRenderer;


/**
 * @author shura
 *
 * Other variant of AJAX View - render full html page with root <html>,
 * <head> ( filled from "head" facet ) and <body> areas.
 * 
 */
public class AjaxPageTag extends AjaxRegionTag {
    
    private ValueExpression onload;
    private ValueExpression onunload;

    /* (non-Javadoc)
     * @see javax.faces.webapp.UIComponentTag#getRendererType()
     */
    public String getRendererType() {
        return AjaxPageRenderer.RENDERER_TYPE;
    }
    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.Tag#release()
     */
    public void release() {
        this.onload = null;
        this.onunload = null;
        super.release();
    }
    /* (non-Javadoc)
     * @see javax.faces.webapp.UIComponentTag#setProperties(javax.faces.component.UIComponent)
     */
    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        setStringProperty(component, "onload", this.onload);
        setStringProperty(component, "onunload", this.onunload);
    }
    /**
     * @return Returns the onload.
     */
    public ValueExpression getOnload() {
        return onload;
    }
    /**
     * @return Returns the onunload.
     */
    public ValueExpression getOnunload() {
        return onunload;
    }
    /**
     * @param onload The onload to set.
     */
    public void setOnload(ValueExpression onload) {
        this.onload = onload;
    }
    /**
     * @param onunload The onunload to set.
     */
    public void setOnunload(ValueExpression onunload) {
        this.onunload = onunload;
    }
}

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

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;

import org.ajax4jsf.Messages;
import org.ajax4jsf.component.AjaxContainer;
import org.ajax4jsf.component.UIAjaxRegion;
import org.ajax4jsf.renderkit.html.AjaxRegionRenderer;
import org.ajax4jsf.webapp.taglib.UIComponentTagBase;


/**
 * @author shura
 * Tag for create root Ajax component. must be immediate in produced html/body.
 * Example use in jsp:
 *  
 * For produce correct html code in any ( ajax and non-ajax requests ),
 * better result with &lt;ajax:page&gt; tag.
 */
public class AjaxRegionTag extends UIComponentTagBase {

    private MethodExpression ajaxListener = null;

    private ValueExpression reRender = null;

    private ValueExpression immediate = null;
    
    private ValueExpression selfRendered = null;
    
    private ValueExpression javascriptLocation = null;

    /**
     *  
     */
    public AjaxRegionTag() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.webapp.UIComponentTag#getComponentType()
     */
    public String getComponentType() {
        return UIAjaxRegion.COMPONENT_TYPE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.webapp.UIComponentTag#getRendererType()
     */
    public String getRendererType() {
        return AjaxRegionRenderer.RENDERER_TYPE;
    }

    // set component properties

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.Tag#release()
     */
    public void release() {
        // release reources properties
        super.release();
        this.reRender = null;
        this.ajaxListener = null;
        this.immediate = null;
        this.selfRendered = null;
        this.javascriptLocation = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.webapp.UIComponentTag#setProperties(javax.faces.component.UIComponent)
     */
    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        setBooleanProperty(component, "immediate", this.immediate);
        setStringProperty(component, "ajaxAreas", this.reRender);
        setBooleanProperty(component,"selfRendered",this.selfRendered);
        setStringProperty(component, "javascriptLocation", this.javascriptLocation);
        if (ajaxListener != null) {
            if (!(component instanceof AjaxContainer)) {
                throw new IllegalArgumentException(Messages.getMessage(Messages.NOT_AJAX_CONTAINER_ERROR, component.getClientId(getFacesContext())));
            }
            if (ajaxListener.isLiteralText()) {
                getFacesContext().getExternalContext().log(Messages.getMessage(Messages.INVALID_EXPRESSION, ajaxListener));
            }
            ((AjaxContainer) component).setAjaxListener(ajaxListener);

        }
    }

    /**
     * @param ajaxAreas
     *            The ajaxAreas to set.
     */
    public void setReRender(ValueExpression ajaxAreas) {
        this.reRender = ajaxAreas;
    }

    /**
     * @param ajaxListener
     *            The ajaxListener to set.
     */
    public void setAjaxListener(MethodExpression ajaxListener) {
        this.ajaxListener = ajaxListener;
    }

    /**
     * @param immediate
     *            The immediate to set.
     */
    public void setImmediate(ValueExpression immediate) {
        this.immediate = immediate;
    }

	/**
	 * @param selfRendered The selfRendered to set.
	 */
	public void setSelfRendered(ValueExpression selfRendered) {
		this.selfRendered = selfRendered;
	}

//	public String getJavascriptLocation() {
//		return javascriptLocation;
//	}
//
	public void setJavascriptLocation(ValueExpression javascriptLocation) {
		this.javascriptLocation = javascriptLocation;
	}
}

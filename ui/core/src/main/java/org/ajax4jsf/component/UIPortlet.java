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
package org.ajax4jsf.component;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author asmirnov
 *
 */
public class UIPortlet extends UIComponentBase implements NamingContainer {

	
    private static final Log _log = LogFactory.getLog(UIPortlet.class);
    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "org.ajax4jsf.Portlet";
    
    
    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "org.ajax4jsf.Portlet";

    /* (non-Javadoc)
     * @see javax.faces.component.UIComponent#getFamily()
     */
    public String getFamily() {
	// TODO Auto-generated method stub
	return COMPONENT_FAMILY;
    }
    
    private String portletId = null;
    
    @Override
    public void setId(String id) {
    	portletId = null;
    	super.setId(id);
    }
    
    // ----------------------------------------------------- UIComponent Methods
    public String getClientId(FacesContext context) {
        if (portletId == null) {
                portletId = context.getExternalContext().encodeNamespace(super.getClientId(context));
        }
        return portletId;
    }

}

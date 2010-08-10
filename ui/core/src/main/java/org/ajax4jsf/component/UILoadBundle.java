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

import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.ajax4jsf.Messages;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author shura
 * 
 */
public abstract class UILoadBundle extends UIComponentBase implements AjaxLoadBundleComponent {

	public static final String COMPONENT_FAMILY = "org.ajax4jsf.Bundle";
	
	public static final String COMPONENT_TYPE = "org.ajax4jsf.Bundle";
	
	private static final Log _log = LogFactory.getLog(UILoadBundle.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.component.UIComponent#getFamily()
	 */
	public String getFamily() {
		// TODO Auto-generated method stub
		return COMPONENT_FAMILY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.component.UIComponentBase#encodeBegin(javax.faces.context.FacesContext)
	 */
	public void encodeBegin(FacesContext context) throws IOException {
		loadBundle(context);

	}

	public void decode(FacesContext context) {
		loadBundle(context);
	}
	/**
	 * @param context
	 */
	public void loadBundle(FacesContext context) {
	    UIViewRoot viewRoot = context.getViewRoot();
	    Locale locale = viewRoot.getLocale();
	    if (locale == null) {
	    	locale = context.getApplication().getDefaultLocale();
	    }

	    ResourceBundle bundle;
	    try {
	    	bundle = ResourceBundle.getBundle(getBasename(), locale, Thread
	    			.currentThread().getContextClassLoader());
	    } catch (MissingResourceException e) {
	    	_log.error(Messages.getMessage(Messages.COULD_NOT_LOAD_RESOURCE_BUNDLE, getBasename()));
	    	return;
	    }

	    context.getExternalContext().getRequestMap().put(getVar(),
	    		new ResourceBundleMap(bundle));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.component.UIComponentBase#getRendererType()
	 */
	public String getRendererType() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return Returns the basename.
	 */
	public abstract String getBasename();

	/**
	 * @param basename
	 *            The basename to set.
	 */
	public abstract void setBasename(String basename);

	/**
	 * @return Returns the var.
	 */
	public abstract String getVar();

	/**
	 * @param var
	 *            The var to set.
	 */
	public abstract void setVar(String var);

}

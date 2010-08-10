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
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Set;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import org.ajax4jsf.component.AjaxDataEncoder;
import org.ajax4jsf.renderkit.AjaxChildrenRenderer;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * Base class for component, performed AJAX encoding on selected values in
 * iterable components ( UIData, trees etc )
 * 
 * @author shura
 * 
 */
public abstract class UISelector extends UIComponentBase implements
		AjaxDataEncoder {

	private static final AjaxChildrenRenderer childrenRenderer = new AjaxChildrenRenderer() {

		protected Class<? extends UIComponent> getComponentClass() {
			return UISelector.class;
		}

	};

	/**
	 * Name of serRow ( or simple ) method for setup current value in iterable
	 * component
	 * 
	 * @parameter
	 * @return the acceptClass
	 */
	public abstract String getIterationProperty();

	/**
	 * @param newSelectMethod
	 *            the value to set
	 */
	public abstract void setIterationProperty(String newSelectMethod);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ajax4jsf.framework.ajax.AjaxChildrenEncoder#encodeAjaxChild(javax.faces.context.FacesContext,
	 *      java.lang.String, java.util.Set, java.util.Set)
	 */
	public void encodeAjaxChild(FacesContext context, String path, 
	        Set<String> ids, Set<String> renderedAreas) throws IOException {
		if (getChildCount() != 1) {
			throw new FacesException("Selector component must have one, and only one, child");
		}
		UIComponent child = (UIComponent) getChildren().get(0);
		Set<Object> ajaxKeys = getAjaxKeys();
		if (null != ajaxKeys) {
			String iterationProperty = getIterationProperty();
			try {
				Object savedKey = PropertyUtils.getProperty(child,
						iterationProperty);
				for (Iterator<Object> iter = ajaxKeys.iterator(); iter.hasNext();) {
					Object key = (Object) iter.next();
					PropertyUtils.setProperty(child, iterationProperty, key);
					if (true) {
						childrenRenderer.encodeAjaxChildren(context, this, path,
								ids, renderedAreas);
					}

				}
				PropertyUtils.setProperty(child, iterationProperty, savedKey);

			} catch (IllegalAccessException e) {
				throw new FacesException(
				"Illegal access to iteration selection property "+iterationProperty+" on component "+child.getClientId(context),e);
			} catch (InvocationTargetException e) {
				throw new FacesException(
						"Error in iteration selection property "+iterationProperty+" on component "+child.getClientId(context),e.getCause());
			} catch (NoSuchMethodException e) {
				throw new FacesException(
						"No iteration selection property "+iterationProperty+" on component "+child.getClientId(context),e);
			}
		}

	}

}

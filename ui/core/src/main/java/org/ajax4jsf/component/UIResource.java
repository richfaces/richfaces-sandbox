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

import javax.faces.component.UIComponentBase;

/**
 * JSF component class
 *
 */
public abstract class UIResource extends UIComponentBase {
	
	public static final String COMPONENT_TYPE = "org.ajax4jsf.Resource";
	
	public static final String COMPONENT_FAMILY = "org.ajax4jsf.Resource";
	
	/**
	 * Get name of script to load
	 * @parameter
	 * @return the acceptClass
	 */
	public abstract Object getSrc();

	/**
	 * @param newSrc the value  to set
	 */
	public abstract void setSrc(Object newSrc);
	
	
}

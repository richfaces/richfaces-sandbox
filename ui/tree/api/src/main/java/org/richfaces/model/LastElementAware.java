/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
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

package org.richfaces.model;

/**
 * {@link org.ajax4jsf.ajax.repeat.UIDataAdaptor.ComponentVisitor} instances can implement this interface in order to
 * be notified of last element occurence at the current tree level. {@link #setLastElement()}
 * method is invoked before visiting last element and {@link #resetLastElement()} is called after
 * visiting last element. 
 * 
 * @author Konstantin Mishin
 */
public interface LastElementAware {
	
	/**
	 * The method is invoked to notify that last element occured
	 */
	public void setLastElement();
	/**
	 * The method is invoked to notify that we're done with last element
	 */
	public void resetLastElement();

}

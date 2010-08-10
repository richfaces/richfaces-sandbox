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

package org.richfaces.event;

import javax.faces.el.MethodBinding;


/**
 * @author Nick Belaevski - nbelaevski@exadel.com
 * created 29.11.2006
 * 
 * Base tree events interface
 */
public interface TreeListenerEventsProducer {
	public abstract void setChangeExpandListener(MethodBinding  binding);
	public abstract MethodBinding getChangeExpandListener();

	public abstract void setNodeSelectListener(MethodBinding  binding);
	public abstract MethodBinding getNodeSelectListener();

	public abstract void setDropListener(MethodBinding  binding);
	public abstract MethodBinding getDropListener();

	public abstract void setDragListener(MethodBinding  binding);
	public abstract MethodBinding getDragListener();
	
	public void addNodeSelectListener(NodeSelectedListener listener);
	public void addChangeExpandListener(NodeExpandedListener listener);
	
	//TODO - rename this
	public boolean hasAjaxSubmitSelection();
}

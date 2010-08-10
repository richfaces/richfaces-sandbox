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

import javax.faces.component.UIComponent;

import org.ajax4jsf.event.AjaxEvent;

/**
 * Event send by tree component in case of AJAX of action (node expansion/collapsion or selection). 
 * Contains data to distinguish between different action types in order to support different re-rendering
 * behaviors for particular actions.
 * 
 * @author Nick Belaevski
 * @since 3.3.0
 */

public class TreeAjaxEvent extends AjaxEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2410803626536767706L;

	private TreeAjaxEventType eventType;
	
	public TreeAjaxEvent(UIComponent component, TreeAjaxEventType eventType) {
		super(component);
		
		this.eventType = eventType;
	}

	public TreeAjaxEventType getEventType() {
		return eventType;
	}
}

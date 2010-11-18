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
import javax.faces.event.FacesEvent;

/**
 * 
 * <br /><br />
 * 
 * Created 12.11.2007
 * @author Nick Belaevski
 * @since 3.2
 */

public abstract class DnDEvent extends FacesEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2455016405742082110L;

	public DnDEvent(UIComponent component) {
		super(component);
	}

}

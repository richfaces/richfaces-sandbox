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
package org.richfaces.event;

import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class CurrentDateChangeEvent extends FacesEvent {

	
	private static final long serialVersionUID = -8169207286087810907L;
	private Date currentDate = null;
	private String currentDateString = null;

	@Deprecated
	public CurrentDateChangeEvent(UIComponent component, Date curentDate) {
		super(component);
		this.currentDate = curentDate;

	}
	
	public CurrentDateChangeEvent(UIComponent component, String curentDateString) {
		super(component);
		this.currentDateString = curentDateString;		 
	}

	public boolean isAppropriateListener(FacesListener listener) {
		return false;
	}

	public void processListener(FacesListener listener) {
		// TODO Auto-generated method stub		
		throw new UnsupportedOperationException();
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}
	
	public String getCurrentDateString() {
		return currentDateString;
	}

}

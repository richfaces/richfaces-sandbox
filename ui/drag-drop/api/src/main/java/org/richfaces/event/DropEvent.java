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

import javax.faces.component.UIComponent;
import javax.faces.event.FacesListener;

import org.richfaces.component.Draggable;
import org.richfaces.component.Dropzone;

/**
 * @author shura
 *
 */
public class DropEvent extends DnDEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1660545054556302746L;
	
	private Draggable draggableSource;
	private String dragType;
	private Object dragValue;
	
	public DropEvent(UIComponent component) {
		super(component);
	}

	/* (non-Javadoc)
	 * @see javax.faces.event.FacesEvent#isAppropriateListener(javax.faces.event.FacesListener)
	 */
	public boolean isAppropriateListener(FacesListener listener) {
		return listener instanceof DropListener;
	}

	/* (non-Javadoc)
	 * @see javax.faces.event.FacesEvent#processListener(javax.faces.event.FacesListener)
	 */
	public void processListener(FacesListener listener) {
		((DropListener) listener).processDrop(this);
	}

	/**
	 * @return the dropValue
	 */
	public Object getDropValue() {
		return ((Dropzone) this.getSource()).getDropValue();
	}

	public Draggable getDraggableSource() {
		return draggableSource;
	}
	
	/**
	 * @see java.util.EventObject#toString()
	 */
/*	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
			append("component", getComponent()).
			append("source", getSource()).
			append("phaseId", getPhaseId()).
			append("dropValue", getDropValue()).
			append("draggableSource", getDraggableSource()).
			append("dragValue", getDragValue()).
			toString();
	}
*/	
	public void setDraggableSource(Draggable draggableSource) {
		this.draggableSource = draggableSource;
	}

	public String getDragType() {
		return dragType;
	}

	public void setDragType(String dragType) {
		this.dragType = dragType;
	}

	public Object getDragValue() {
		return dragValue;
	}

	public void setDragValue(Object dragValue) {
		this.dragValue = dragValue;
	}
}

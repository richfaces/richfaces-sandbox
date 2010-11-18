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
 * @author Nick Belaevski - nbelaevski@exadel.com
 * created 27.12.2006
 * 
 */
public class DragEvent extends DnDEvent {

	private Dropzone dropTarget;
	private Object acceptedTypes;
	private Object dropValue;
	/**
	 * 
	 */
	private static final long serialVersionUID = 6179268394391836905L;

	public DragEvent(UIComponent uiComponent) {
		super(uiComponent);
	}

	/* (non-Javadoc)
	 * @see javax.faces.event.FacesEvent#isAppropriateListener(javax.faces.event.FacesListener)
	 */
	public boolean isAppropriateListener(FacesListener faceslistener) {
		return faceslistener instanceof DragListener;
	}

	/* (non-Javadoc)
	 * @see javax.faces.event.FacesEvent#processListener(javax.faces.event.FacesListener)
	 */
	public void processListener(FacesListener faceslistener) {
		((DragListener) faceslistener).processDrag(this);
	}

	public Object getDragValue() {
		return ((Draggable) this.getSource()).getDragValue();
	}

	public Dropzone getDropTarget() {
		return dropTarget;
	}
	
	/**
	 * @see java.util.EventObject#toString()
	 */
/*	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
		append("component", getComponent()).append("source", getSource()).append("phaseId", getPhaseId()).
		append("dragValue", getDragValue()).
		append("dropTarget", getDropTarget()).
		append("dropValue", getDropValue()).
		toString();
	}
*/
	public void setDropTarget(Dropzone dropTarget) {
		this.dropTarget = dropTarget;
	}

	public Object getAcceptedTypes() {
		return acceptedTypes;
	}

	public void setAcceptedTypes(Object acceptedTypes) {
		this.acceptedTypes = acceptedTypes;
	}

	public Object getDropValue() {
		return dropValue;
	}

	public void setDropValue(Object dropValue) {
		this.dropValue = dropValue;
	}
}

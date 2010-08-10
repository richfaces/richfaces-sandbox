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

package org.richfaces.component;

import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;

import org.richfaces.event.DragListener;


/**
 * Interface for all draggable components. Describe bean properties, implemented by component.
 * @author shura
 *
 */
public interface Draggable  {
	
	/**
	 * Getter for value parameter, used as key for draggable component.
	 * @return
	 */
	public Object getDragValue();
	
	public void setDragValue(Object value); 
	
	/**
	 * Getter for id of component ( instance of {@link UIDragIndicator} ) used for create drag cursor.
	 * @return
	 */
	public String getDragIndicator();
	
	/**
	 * @param dragIndicator - id of cursor component
	 */
	public void setDragIndicator(String dragIndicator);
	
	/**
	 * Draggable implementation may wish to resolve drag indicator id to clientId itself
	 * @param facesContext {@link FacesContext} instance
	 * @return resolved indicator client id or null
	 * @since 3.1
	 */
	public String getResolvedDragIndicator(FacesContext facesContext);
	
	/**
	 * Getter for type of this draggable ( file, mail etc ).
	 * @return
	 */
	public String getDragType();
	
	/**
	 * @param dragType
	 */
	public void setDragType(String dragType);
	
	/**
	 * Getter for JavaScript event handler, called before start drag operation.
	 * If this handler return false, drag operation is cancelled.
	 * @return javaScript code of event handler.
	 */
	public String getOndragstart();
	
	/**
	 * @param dragType
	 */
	public void setOndragstart(String ondrag);

	/**
	 * Getter for JavaScript event handler; Called when drag operation end.
	 * @return javaScript code of event handler.
	 */	
	public String getOndragend();
	public void setOndragend(String ondrag);
	
	/**
	 * Getter for JavaScript event handler; Called when dragged element over the dropzone.
	 * @return javaScript code of event handler.
	 */
	public String getOndropover();
	public void setOndropover(String ondropover);

	/**
	 * Getter for JavaScript event handler; Called when dragged element out the dropzone.
	 * @return javaScript code of event handler.
	 */
	public String getOndropout();
	public void setOndropout(String ondropout);
	
	/**
	 * Append drag listener to component listeners collection
	 * @param listener
	 */
	public void addDragListener(DragListener listener);
	
	/**
	 * Get array of all Drop Listeners
	 * @return
	 */
	public DragListener[] getDragListeners();
	
	/**
	 * Remove drop listener from component listeners array.
	 * @param listener
	 */
	public void removeDragListener(DragListener listener);
	
	public void setDragListener(MethodBinding binding);
	public MethodBinding getDragListener();

	public String getGrabCursors();
	public void setGrabCursors(String grabCursors);

	public String getGrabbingCursors();
	public void setGrabbingCursors(String grabbingCursors);

}

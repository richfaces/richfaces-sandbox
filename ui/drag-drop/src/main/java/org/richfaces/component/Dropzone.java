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

import org.richfaces.event.DropSource;

/**
 * Base interface for all components, accesible as drop zone.
 * @author shura
 *
 */
public interface Dropzone extends DropSource {
	
	/**
	 * Getter for dropType's , accepted by this zone.
	 * @return
	 */
	public Object getAcceptedTypes();
	
	public void setAcceptedTypes(Object types);
	
	/**
	 * Getter for mapping between drop types and indicator states.
	 * @return
	 */
	public Object getTypeMapping();
	
	public void setTypeMapping(Object types);
	
	
	/**
	 * Getter for mapping between drop types and acceptable cursors
	 * @return
	 */
	public Object getCursorTypeMapping();
	public void setCursorTypeMapping(Object types);
	
	/**
	 * Getter for JavaScript event handler, called then drag curcor enter in component area.
	 * If this handler return false, or "declined", drop operation on this component not allowed.
	 * If handler return true or "allowed" , or any other supported indicator states, drop operation
	 * is allowed and corresponding indicator state will be displayed.
	 * @return javaScript code of event handler.
	 */
	public String getOndragenter();
	
	/**
	 * @param dragType
	 */
	public void setOndragenter(String ondrag);

	/**
	 * Getter for JavaScript event handler, called before  drag cursor leave component area.
	 * @return javaScript code of event handler.
	 */
	public String getOndragexit();
	
	/**
	 * @param dragType
	 */
	public void setOndragexit(String ondrag);

	public Object getDropValue();
	
	public void setDropValue(Object o);
	
	/**
	 * Javascript code called before drop event.
	 * @parameter
	 * @return the acceptClass
	 */
	public abstract String getOndrop();

	/**
	 * @param newOndrop the value  to set
	 */
	public abstract void setOndrop(String newOndrop);
	
	/**
	 *  Javascript handler for event fired on drop even the drop for given type is not available
	 * @parameter
	 * @return the acceptClass
	 */
	public abstract String getOndropend();

	/**
	 * @param newname the value  to set
	 */
	public abstract void setOndropend(String ondropend);
	
	/**
	 *  Getter for the list of comma separated cursors that indicates when acceptable draggable over dropzone
	 */
	public String getAcceptCursors();
	public void setAcceptCursors(String acceptCursors);

	/**
	 *  Getter for the list of comma separated cursors that indicates when rejectable draggable over dropzone   
	 */
	public String getRejectCursors();
	public void setRejectCursors(String rejectCursors);

}

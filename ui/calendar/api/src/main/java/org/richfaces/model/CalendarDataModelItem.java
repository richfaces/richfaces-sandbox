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
 * @author Alexej Kushunin
 * created 19.06.2007
 *
 */

public interface CalendarDataModelItem {
	
	/**
	 *@return true if date is �selectable� on calendar, default implementation return true 
	 **/
	public boolean isEnabled();
	
	/**
	 * @return String that will be appended to style class for that date span.
	 * For example it may be �relevant holyday� � that mean class will be like �rich-cal-day relevant holyday�.
	 * Default implementation return empty string.
	 * */
	public String getStyleClass();
	
	/**
	 * @return any additional payload that must be JSON-serialazable object.
	 * May be used in custom date representation on calendar (inside custom facet).*/
	public Object getData();
	
	/**
	 * @return true if given date has an associated with it tooltip data.
	 * Default implementation return false.*/
	public boolean hasToolTip();
	
	/**
	 *@return tool tip data that will be used in �batch� tooltip loading mode.
	 **/
	public Object getToolTip();
	
	/**
	 *@return day of the month on which data must be shown.
	 **/
	public int getDay();
	
}




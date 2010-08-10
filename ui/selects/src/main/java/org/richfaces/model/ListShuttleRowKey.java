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
package org.richfaces.model;

import java.io.Serializable;

/**
 * Special type of row key containing information on item origin and new placement
 * 
 * @author Nick Belaevski
 *
 */
public class ListShuttleRowKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3308741255288495879L;
	
	private boolean source;
	
	private boolean facadeSource;
	
	private Object rowKey;

	public boolean isSource() {
		return source;
	}
	
	public boolean isFacadeSource() {
		return facadeSource;
	}
	
	public Object getRowKey() {
		return rowKey;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rowKey == null) ? 0 : rowKey.hashCode());
		result = prime * result + (source ? 1231 : 1237);
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ListShuttleRowKey other = (ListShuttleRowKey) obj;
		if (rowKey == null) {
			if (other.rowKey != null)
				return false;
		} else if (!rowKey.equals(other.rowKey))
			return false;
		if (source != other.source)
			return false;
		return true;
	}
	
	public String toString() {
		return (source ? "" : "t") + rowKey.toString();
	}

	public ListShuttleRowKey(Object rowKey, boolean source) {
		super();
		this.rowKey = rowKey;
		this.source = source;
		this.facadeSource = source;
	}

	public ListShuttleRowKey(Object rowKey, boolean source, boolean facadeSource) {
		super();
		this.rowKey = rowKey;
		this.source = source;
		this.facadeSource = facadeSource;
	}
}

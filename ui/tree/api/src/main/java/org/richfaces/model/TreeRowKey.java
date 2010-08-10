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

import java.io.Serializable;
import java.util.Iterator;

/**
 * Base abstract clas for all tree row keys
 * @author Nick Belaevski - nbelaevski@exadel.com
 * created 06.12.2006
 */
public abstract class TreeRowKey<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7318192501938658798L;
	
	public static final char SEPARATOR_ESCAPE_CHAR  = '_';
	
	/**
	 * getter for key depth
	 * @return key depth
	 */
	public abstract int depth();

	/**
	 * getter for subpath iterator
	 * @param fromIndex
	 * @return subpath segments iterator
	 */
	public abstract Iterator<T> getSubPathIterator(int fromIndex);
	
	/**
	 * getter for path string representation
	 * @return path string
	 */
	public abstract String getPath();

	public String toString() {
		return getPath();
	}

	/**
	 * tests if specified rowKey is sub-key of this row key
	 * @param rowKey
	 * @return
	 */
	public boolean isSubKey(TreeRowKey<T> rowKey) {
		if (rowKey == null) {
			return false;
		}

		String otherPath = rowKey.getPath();
		String path = getPath();
		
		if (otherPath.startsWith(path)) {
			return otherPath.length() == path.length() || 
					otherPath.charAt(path.length()) == AbstractTreeDataModel.SEPARATOR;
		} else {
			return false;
		}
	}
	
	/**
	 * getter for path iterator
	 * @return path segments iterator
	 */
	public abstract Iterator<T> iterator();

	/**
	 * returns this row key and otherRowKey argument row key common path segments count 
	 * @param otherRowKey {@link TreeRowKey} to count common path segments for
	 * @return common path segmments count
	 */
	public abstract int getCommonPathLength(TreeRowKey<T> otherRowKey);

	public abstract TreeRowKey<T> getParentKey();

	public TreeRowKey<T> getSubKey(int fromIndex) {
	    	throw new UnsupportedOperationException();
	}
}

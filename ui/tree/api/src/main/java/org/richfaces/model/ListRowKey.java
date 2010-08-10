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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.NamingContainer;

/**
 * Default {@link TreeRowKey} implementation based on {@link ArrayList}
 * @author Nick Belaevski - nbelaevski@exadel.com
 * created 17.11.2006
 */
public class ListRowKey<T> extends TreeRowKey<T> {

	private ArrayList<T> path;

	/**
	 * 
	 */
	private static final long serialVersionUID = 7718335783201397177L;

	public String toString() {
		return getPath();
	}

	/**
	 * Default constructor
	 */
	public ListRowKey() {
		super();
		this.path = new ArrayList<T>();
	}

	/**
	 * Copy constructor
	 * @param parentRowKey row key to clone
	 */
	
	@SuppressWarnings("unchecked")
	public ListRowKey(ListRowKey<T> parentRowKey) {
		super();
		if (parentRowKey != null) {
			this.path = (ArrayList<T>) parentRowKey.path.clone();
		} else {
			this.path = new ArrayList<T>();
		}
	}

	/**
	 * Appending constructor
	 * 
	 * @param parentRowKey base row key
	 * @param pathElement path segment to append to base row key
	 */
	public ListRowKey(ListRowKey<T> parentRowKey, T pathElement) {
		this(parentRowKey);
		this.path.add(pathElement);
	}

	/**
	 * Appending constructor
	 * @param parentRowKey base row key
	 * @param pathElement path segment to append to base row key
	 */
	public ListRowKey(ListRowKey<T> parentRowKey, ListRowKey<T> childRowKey) {
		this(parentRowKey);
		this.path.addAll(childRowKey.path);
	}

	protected ListRowKey(ArrayList<T> list) {
		super();
		
		this.path = list;
	}
	
	/**
	 * Path object constructor
	 * @param path first path segment
	 * @deprecated
	 */
	public ListRowKey(T path) {
		super();
		this.path = new ArrayList<T>(1);
		this.path.add(path);
	}
	
	/**
	 * List constructor
	 * @param list List of strings to create corresponding row key from
	 */
	public ListRowKey(List<T> list) {
		super();

		this.path = new ArrayList<T>(list);
	}

	public int depth() {
		return path.size();
	}

	public Iterator<T> iterator() {
		return path.iterator();
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ListRowKey<?> other = (ListRowKey<?>) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}

	public Iterator<T> getSubPathIterator(int fromIndex) {
		return path.listIterator(fromIndex);
	}

	@Override
	public TreeRowKey<T> getSubKey(int fromIndex) {
		return new ListRowKey<T>(path.subList(fromIndex, path.size()));
	}
	
	
	@SuppressWarnings("unchecked")
	public boolean isSubKey(TreeRowKey<T> rowKey) {
		if (rowKey instanceof ListRowKey) {
			ListRowKey<T> listRowKey = (ListRowKey<T>) rowKey;

			return depth() == getCommonPathLength(listRowKey);
		} else {
			return super.isSubKey(rowKey);
		}
	}

	private void appendSegment(StringBuilder builder, String segment) {
	    	StringBuilder escapedSubPath = new StringBuilder();
		for (int i = 0; i < segment.length(); i++) {
			char ch = segment.charAt(i);

			//escape
			if (AbstractTreeDataModel.SEPARATOR == ch || ListRowKey.SEPARATOR_ESCAPE_CHAR == ch) {
				escapedSubPath.append(ListRowKey.SEPARATOR_ESCAPE_CHAR);
			}

			escapedSubPath.append(ch);
		}
		
		builder.append(escapedSubPath.toString());
	}
	
	public String getPath() {
		StringBuilder result = new StringBuilder();
		Iterator<T> iterator = path.iterator();
		boolean hasNext = iterator.hasNext();
		
		while (hasNext) {
		    T segment = iterator.next();
			
		    if (segment instanceof ComplexTreeRowKey) {
				ComplexTreeRowKey complexKey = (ComplexTreeRowKey) segment;
				
				int segmentsCount = complexKey.getKeySegmentsCount();
				for (int i = 0; i < segmentsCount; i++) {
				    appendSegment(result, complexKey.getKeySegment(i).toString());
				    if (i < segmentsCount - 1) {
				    	result.append(AbstractTreeDataModel.SEPARATOR);
				    }
				}
				
			} else {
			    appendSegment(result, segment.toString());
			}

			hasNext = iterator.hasNext();

			if (hasNext) {
				result.append(AbstractTreeDataModel.SEPARATOR);
			}
		}

		return result.toString();
	}

	public int getCommonPathLength(TreeRowKey<T> otherRowKey) {
		if (otherRowKey == null)
			return 0;
		Iterator<T> iterator = this.iterator();
		Iterator<T> otherIterator = otherRowKey.iterator();
		int length = 0;
		while (iterator.hasNext() && otherIterator.hasNext()
				&& iterator.next().equals(otherIterator.next()))
			length++;
		return length;
	}
	
	public T get(int i) {
		return path.get(i);
	}
	
	private static final String SEPARATOR = "(?<!" + ListRowKey.SEPARATOR_ESCAPE_CHAR + ")\\" 
		+ NamingContainer.SEPARATOR_CHAR;

	public static String[] fromString(String keyString) {
	    String[] split = keyString.split(SEPARATOR);
	    for (int i = 0; i < split.length; i++) {
		//TODO exception if not escaped properly
		split[i] = split[i].replaceAll("_(:|_)", "$1");
	    }
	
	    return split;
	}
	
	public static void main(String[] args) {
	    System.out.println(Arrays.toString(fromString("test_:abc:123:a__b")));
	}

	@Override
	public TreeRowKey<T> getParentKey() {
		int toIdx = path.size() - 1;
		TreeRowKey<T> result = null;
		
		if (toIdx >= 0) {
			result = new ListRowKey<T>(new ArrayList<T>(path.subList(0, toIdx)));
		} 
		
		return result;
	}
}

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

package org.ajax4jsf.component;

import java.util.Collection;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import org.ajax4jsf.Messages;

/**
 * Very limited Map decorator to ResourceBundle.
 * @author shura
 *
 */
public class ResourceBundleMap implements Map<String, Object> {
	
	private ResourceBundle _bundle;

	/**
	 * @param bundle
	 */
	public ResourceBundleMap(ResourceBundle bundle) {
		super();
		_bundle = bundle;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#size()
	 */
	public int size() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty() {
		return false;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key) {
		
		try {
			_bundle.getObject(key.toString());
			return true;
		} catch (MissingResourceException e) {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public Object get(Object key) {
		try {
			return _bundle.getObject(key.toString());
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public Object put(String arg0, Object arg1) {
		throw new UnsupportedOperationException(Messages.getMessage(Messages.BUNDLE_MAP_NO_PUT_VALUE));	
	}

	/* (non-Javadoc)
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public Object remove(Object key) {
		throw new UnsupportedOperationException(Messages.getMessage(Messages.BUNDLE_MAP_NO_REMOVE_VALUE));	
	}


	/* (non-Javadoc)
	 * @see java.util.Map#clear()
	 */
	public void clear() {
		throw new UnsupportedOperationException(Messages.getMessage(Messages.BUNDLE_MAP_NO_REMOVE_VALUE));	
	}

	/* (non-Javadoc)
	 * @see java.util.Map#keySet()
	 */
	public Set<String> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#values()
	 */
	public Collection<Object> values() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void putAll(Map<? extends String, ? extends Object> t) {
		throw new UnsupportedOperationException(Messages.getMessage(Messages.BUNDLE_MAP_NO_PUT_VALUE));	
	}

}

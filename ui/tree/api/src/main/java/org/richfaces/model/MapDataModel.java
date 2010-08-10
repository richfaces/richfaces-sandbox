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

import java.io.IOException;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.Range;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * That is intended for internal use
 * 
 * @author Nick Belaevski mailto:nbelaevski@exadel.com created 30.07.2007
 */
public class MapDataModel extends ExtendedDataModel implements ConvertableKeyModel {

	private Map<Object, Object> map;
	private Object rowKey;
	private Object wrappedData;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ajax4jsf.model.ExtendedDataModel#getRowKey()
	 */
	public Object getRowKey() {
		return rowKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ajax4jsf.model.ExtendedDataModel#setRowKey(java.lang.Object)
	 */

	public void setRowKey(Object key) {
		this.rowKey = key;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ajax4jsf.model.ExtendedDataModel#walk(javax.faces.context.FacesContext,
	 *      org.ajax4jsf.model.DataVisitor, org.ajax4jsf.model.Range,
	 *      java.lang.Object)
	 */

	public void walk(FacesContext context, DataVisitor visitor, Range range,
			Object argument) throws IOException {
		if (map != null) {
			for (Iterator<Object> iterator = map.keySet().iterator(); iterator.hasNext();) {
				Object key = (Object) iterator.next();
				
				visitor.process(context, key, argument);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.model.DataModel#getRowCount()
	 */

	public int getRowCount() {
		return map != null ? map.size() : 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.model.DataModel#getRowData()
	 */

	public Object getRowData() {
		return map != null ? map.get(rowKey) : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.model.DataModel#getRowIndex()
	 */

	public int getRowIndex() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.model.DataModel#getWrappedData()
	 */
	public Object getWrappedData() {
		return wrappedData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.model.DataModel#isRowAvailable()
	 */
	public boolean isRowAvailable() {
		return map != null && map.containsKey(rowKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.model.DataModel#setRowIndex(int)
	 */
	public void setRowIndex(int rowIndex) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.model.DataModel#setWrappedData(java.lang.Object)
	 */
	public void setWrappedData(Object data) {
		this.wrappedData = data;
		if (data instanceof NamedNodeMap) {
			this.map = new AbstractMap<Object, Object>() {

				private Set<Entry<Object, Object>> entrySet = new AbstractSet<Entry<Object, Object>>() {

					public Iterator<Entry<Object, Object>> iterator() {
						return new Iterator<Entry<Object, Object>>() {
							private int index = 0;

							public boolean hasNext() {
								return index < ((NamedNodeMap) getWrappedData()).getLength();
							}

							public Entry<Object, Object> next() {
								final Node node = ((NamedNodeMap) getWrappedData()).item(index++);
								if (node == null) {
									throw new NoSuchElementException();
								} else {
									return new Map.Entry<Object, Object>() {

										public Object getKey() {
											return node.getNodeName();
										}

										public Object getValue() {
											return node;
										}

										public Object setValue(Object arg0) {
											throw new UnsupportedOperationException();
										}
										
									};
								}
							}

							public void remove() {
								throw new UnsupportedOperationException();
							}
						};
					}

					public int size() {
						return ((NamedNodeMap) getWrappedData()).getLength();
					}
					
				};
				
				public boolean containsKey(Object key) {
					return ((NamedNodeMap) getWrappedData()).getNamedItem((String) key) != null;
				}
				
				public Object get(Object key) {
					return ((NamedNodeMap) getWrappedData()).getNamedItem((String) key);
				}
				
				public Set<Entry<Object, Object>> entrySet() {
					return entrySet;
				}
				
			};
		} else if (data != null) {
			this.map = (Map<Object, Object>) data;
		} else {
			this.map = null;
		}
	}

	public Object getKeyAsObject(FacesContext context, String key, UIComponent component, Converter converter) {
	    if (this.map != null) {
		if (this.map.containsKey(key)) {
		    return key;
		} else {
		    Set<Entry<Object,Object>> set = this.map.entrySet();
		    for (Entry<Object, Object> entry : set) {
			Object keyObject = entry.getKey();
			if (key.equals(keyObject.toString())) {
			    return keyObject;
			}
		    }
		}
	    }
	    
	    return null;
	}

}

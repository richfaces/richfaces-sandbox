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
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.Range;
import org.w3c.dom.NodeList;

/**
 * That is intended for internal use
 * 
 * @author Nick Belaevski
 *         mailto:nbelaevski@exadel.com
 *         created 30.07.2007
 *
 */
public class SequenceDataModel extends ExtendedDataModel implements ConvertableKeyModel {

	private Object wrappedData;
	private List list;
	private Integer key;
	
	/* (non-Javadoc)
	 * @see org.ajax4jsf.model.ExtendedDataModel#getRowKey()
	 */
	public Object getRowKey() {
		return key;
	}

	/* (non-Javadoc)
	 * @see org.ajax4jsf.model.ExtendedDataModel#setRowKey(java.lang.Object)
	 */
	public void setRowKey(Object key) {
		this.key = (Integer) key;
	}

	/* (non-Javadoc)
	 * @see org.ajax4jsf.model.ExtendedDataModel#walk(javax.faces.context.FacesContext, org.ajax4jsf.model.DataVisitor, org.ajax4jsf.model.Range, java.lang.Object)
	 */
	public void walk(FacesContext context, DataVisitor visitor, Range range,
			Object argument) throws IOException {

		if (list != null) {
			int i = 0;
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				iterator.next();
				
				visitor.process(context, new Integer(i), argument);
				
				i++;
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.faces.model.DataModel#getRowCount()
	 */
	public int getRowCount() {
		return list != null ? list.size() : 0;
	}

	/* (non-Javadoc)
	 * @see javax.faces.model.DataModel#getRowData()
	 */
	public Object getRowData() {
		return (list != null && key != null) ? list.get(key.intValue()) : null;
	}

	/* (non-Javadoc)
	 * @see javax.faces.model.DataModel#getRowIndex()
	 */
	public int getRowIndex() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.faces.model.DataModel#getWrappedData()
	 */
	public Object getWrappedData() {
		return wrappedData;
	}

	/* (non-Javadoc)
	 * @see javax.faces.model.DataModel#isRowAvailable()
	 */
	public boolean isRowAvailable() {
		return list != null && key != null && list.size() > key.intValue();
	}

	/* (non-Javadoc)
	 * @see javax.faces.model.DataModel#setRowIndex(int)
	 */
	public void setRowIndex(int rowIndex) {
	}

	/* (non-Javadoc)
	 * @see javax.faces.model.DataModel#setWrappedData(java.lang.Object)
	 */
	public void setWrappedData(Object data) {
		this.wrappedData = data;
		if (data instanceof List) {
			this.list = (List) data;
		} else if (data instanceof Collection) {
			this.list = new ArrayList((Collection) data);
			//copying all collection content into new collection here should prevent us
			//from possible issues caused by iteration tricks. e.g.: returning custom iterator
			//caching current element and comparing indices inside get()
			//
			//possible concurrent collection modifications and multithreading will break the magic
		} else if (data instanceof NodeList) {
			this.list = new AbstractList() {

				public Object get(int index) {
					return ((NodeList) getWrappedData()).item(index);
				}

				public int size() {
					return ((NodeList) getWrappedData()).getLength();
				}
				
			};
		} else if (data instanceof Object[]) {
			this.list = Arrays.asList((Object[]) data); 
		} else if (data != null) {
			this.list = Collections.singletonList(data); 
		} else {
			this.list = null;
		}
	}

	public Object getKeyAsObject(FacesContext context, String keyString, UIComponent component, Converter converter) {
	    int key = Integer.parseInt(keyString);
	    if (key >= 0 && this.list != null && key < this.list.size()) {
		return key;
	    } else {
		return null;
	    }
	}
}

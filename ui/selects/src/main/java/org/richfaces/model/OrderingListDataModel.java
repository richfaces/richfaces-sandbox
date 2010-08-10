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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.faces.context.FacesContext;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.Range;

/**
 * Map-based extended data model for model-translating components like ordering list
 * 
 * @author Nick Belaevski
 *         mailto:nbelaevski@exadel.com
 *         created 07.11.2007
 *
 */
public class OrderingListDataModel extends ExtendedDataModel {

	private Map<Object, Object> data;
	
	private Object rowKey;
	
	public Object getRowKey() {
		return rowKey;
	}

	public void setRowKey(Object rowKey) {
		this.rowKey = rowKey;
	}

	public void walk(FacesContext context, DataVisitor visitor, Range range,
			Object argument) throws IOException {

		Set<Entry<Object,Object>> entrySet = data.entrySet();
		Iterator<Entry<Object, Object>> iterator = entrySet.iterator();
		
		while (iterator.hasNext()) {
			Entry<Object, Object> entry = iterator.next();
			
			visitor.process(context, entry.getKey(), argument);
		}
	}

	public int getRowCount() {
		return data.size();
	}

	public Object getRowData() {
		return data.get(rowKey);
	}

	public int getRowIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Object getWrappedData() {
		return data;
	}

	public boolean isRowAvailable() {
		return data.containsKey(rowKey);
	}

	public void setRowIndex(int rowIndex) {
		// TODO Auto-generated method stub
		
	}

	public void setWrappedData(Object data) {
		this.rowKey = null;
		this.data = (Map<Object, Object>) data;
	}

}

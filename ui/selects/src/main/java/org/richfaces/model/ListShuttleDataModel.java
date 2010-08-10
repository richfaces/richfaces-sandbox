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
import java.util.Map.Entry;

import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceDataModel;

/**
 * Dual map-based extended data model for model-translating components like list shuttle
 * @author Nick Belaevski
 *
 */
public class ListShuttleDataModel extends ExtendedDataModel {

	private ListShuttleRowKey rowKey;

	private SequenceDataModel sourceModel;
	private SequenceDataModel targetModel;

	private Map<Object, Object> data;

	private Object wrappedData;
	
	/* (non-Javadoc)
	 * @see org.ajax4jsf.model.ExtendedDataModel#getRowKey()
	 */
	public Object getRowKey() {
		return rowKey;
	}

	/* (non-Javadoc)
	 * @see org.ajax4jsf.model.ExtendedDataModel#setRowKey(java.lang.Object)
	 */
	public void setRowKey(Object key) {
		this.rowKey = (ListShuttleRowKey) key;
	}

	public void walk(final FacesContext context, final DataVisitor visitor, final Range range,
			final Object argument) throws IOException {
		if (data != null) {
			Iterator<Entry<Object, Object>> iterator = data.entrySet().iterator();
			
			while (iterator.hasNext()) {
				Entry<Object, Object> entry = iterator.next();
				
				visitor.process(context, entry.getKey(), argument);
			}
			
		} else {
			sourceModel.walk(context, new DataVisitor() {

				public void process(FacesContext context, Object rowKey,
						Object argument) throws IOException {

					ListShuttleRowKey key = new ListShuttleRowKey(rowKey, true);
					visitor.process(context, key, argument);
				}
			}, range, argument);

			targetModel.walk(context, new DataVisitor() {

				public void process(FacesContext context, Object rowKey,
						Object argument) throws IOException {

					ListShuttleRowKey key = new ListShuttleRowKey(rowKey, false);
					visitor.process(context, key, argument);
				}
				
			}, range, argument);
		}
	}

	public int getRowCount() {
		if (data != null) {
			return data.size();
		} else {
			return sourceModel.getRowCount() + targetModel.getRowCount();
		}
	}

	public Object getRowData() {
		if (data != null) {
			return data.get(rowKey);
		} else {
			if (this.rowKey != null) {
				if (this.rowKey.isSource()) {
					return sourceModel.getRowData();
				} else {
					return targetModel.getRowData();
				}
			} else {
				return null;
			}
		}
	}

	public int getRowIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Object getWrappedData() {
		return wrappedData;
	}

	public boolean isRowAvailable() {
		if (data != null) {
			return data.containsKey(rowKey);
		} else {
			if (rowKey != null) {
				if (rowKey.isSource()) {
					return sourceModel.isRowAvailable();
				} else {
					return targetModel.isRowAvailable();
				}
			} else {
				return false;
			}
		}
	}

	public void setRowIndex(int rowIndex) {
		// TODO Auto-generated method stub
		
	}

	public void setWrappedData(Object data) {
		this.rowKey = null;
		this.wrappedData = data;
		
		if (data instanceof Map) {
			this.data = (Map<Object, Object>) data;
		} else {
			DataModel[] models = (DataModel[]) data;
			this.sourceModel = new SequenceDataModel(models[0]);
			this.targetModel = new SequenceDataModel(models[1]);
		}
	}

}

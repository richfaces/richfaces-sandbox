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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.Range;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.iterators.FilterIterator;
import org.w3c.dom.NamedNodeMap;

/**
 * That is intended for internal use
 * 
 * @author Nick Belaevski mailto:nbelaevski@exadel.com created 25.07.2007
 * 
 */
public class StackingTreeModel extends AbstractTreeDataModel {

	//ctor arguments
	private String id;
	private String var;
	private StackingTreeModelDataProvider dataProvider;

	//structural elements
	private StackingTreeModel parent;
	private Map<String, StackingTreeModel> models = new LinkedHashMap<String, StackingTreeModel>();

	private Object rowKey;

	private class StackEntry {
		private Object modelKey;
		private Object varObject;
		private StackingTreeModel model;
		private Object rowData;
		
		public StackEntry(Object varObject, Object modelKey, Object rowData, StackingTreeModel model) {
			super();
			this.varObject = varObject;
			this.modelKey = modelKey;
			this.rowData = rowData;
			this.model = model;
		}
	}

//	private StackingTreeModel stackingTreeModel;
	private LinkedList<StackEntry> stackEntries = new LinkedList<StackEntry>();

	public ExtendedDataModel getDataModel() {
		Object data = dataProvider.getData();
		ExtendedDataModel dataModel;
		if (data instanceof Map || data instanceof NamedNodeMap) {
			dataModel = new MapDataModel();
		} else {
			dataModel = new SequenceDataModel();
		}

		dataModel.setWrappedData(data);
		return dataModel;
	}

	protected StackingTreeModel getCurrentModel() {
		if (this.rowKey == null) {
			return this;
		}
		
		if (isRowAvailable()) {
			return ((StackEntry) stackEntries.getLast()).model;
		}

		throw new IllegalStateException(
				"No tree element available or row key not set!");
	}
	
	public boolean isEmpty() {
		//TODO optimize that
		return getDataModel().getRowCount() == 0;
	}

	private void leaveModel(Iterator<StackEntry> iterator, StackEntry currentEntry, FacesContext context) {
		if (iterator == null) {
			return ;
		}

		LinkedList<StackEntry> stack = new LinkedList<StackEntry>();

		StackingTreeModel lastModel = null;
		if (currentEntry != null) {
			iterator.remove();
			stack.addFirst(currentEntry);
			lastModel = currentEntry.model;
		}
		
		while (iterator.hasNext()) {
			StackEntry entry = (StackEntry) iterator.next();
			if (entry.model != lastModel) {
				//always true for non-recursive models
				lastModel = entry.model;
				stack.addFirst(entry);
			}
			
			iterator.remove();
		}

		for (Iterator<StackEntry> iterator2 = stack.iterator(); iterator2.hasNext();) {
			StackEntry stackEntry = (StackEntry) iterator2.next();
			stackEntry.model.setupVariable(stackEntry.varObject, context);
		}
	}

	protected StackingTreeModel doSetupKey(Iterator<StackingTreeModelKey> keyIterator, Iterator<StackEntry> entriesIterator, FacesContext context, Object modelKey) {
		if (modelKey != null) {
			if (!setupModel(modelKey, context)) {
				//no key is available
				leaveModel(getRoot().stackEntries.iterator(), null, context);
				return null;
			}
			
			//TODO what's here?
		}
		
		if (keyIterator != null && keyIterator.hasNext()) {
			StackingTreeModelKey key = keyIterator.next();
			StackingTreeModel stackingTreeModel = this.getInternalModelById(key.modelId);
			Iterator<StackEntry> nextEntriesIterator = null;
			Object nextModelKey = key.modelKey;
			
			if (entriesIterator != null && entriesIterator.hasNext()) {
				StackEntry entry = entriesIterator.next();
				if (!entry.model.equals(stackingTreeModel) || !entry.modelKey.equals(nextModelKey)) {
					leaveModel(entriesIterator, entry, context);
				} else {
					//continue iterating entries, they still lead us by key path
					nextEntriesIterator = entriesIterator;
					nextModelKey = null;
				}
			}

			//should not be called when nextEntriesIterator & nextModelKey are both valid
			return stackingTreeModel.doSetupKey(keyIterator, nextEntriesIterator, context, nextModelKey);
		
		} else {
			leaveModel(entriesIterator, null, context);
			return this;
		}
	}
	
	protected StackingTreeModel setupKey(Object key, FacesContext context) {
		if (key == this.rowKey) {
			if (stackEntries.isEmpty()) {
				return this;
			} else {
				return (stackEntries.getLast()).model;
			}
		} else {
			Iterator<StackingTreeModelKey> keyIterator = null;
			if (key != null) {
				keyIterator = ((ListRowKey<StackingTreeModelKey>) key).iterator();
			}
			
			StackingTreeModel model = doSetupKey(keyIterator, stackEntries.iterator(), context, null);
			this.rowKey = key;

			return model;
		}
	}

	public StackingTreeModel(String id, String var, StackingTreeModelDataProvider dataProvider) {
		super();
		this.id = id;
		this.var = var;
		this.dataProvider = dataProvider;
	}

	public StackingTreeModel() {
		this(null, null, null);
	}

	private Object setupVariable(Object variable, FacesContext context) {
		if (var != null) {
			Map<String, Object> map = context.getExternalContext().getRequestMap();
			return map.put(var, variable);
		}

		return null;
	}

	public boolean setupModel(Object key, FacesContext facesContext) {
		ExtendedDataModel dataModel = getDataModel();
		dataModel.setRowKey(key);

		if (dataModel.isRowAvailable()) {
			Object rowData = dataModel.getRowData();
			//System.out.println("StackingTreeModel.setupModel() " + rowData);
			Object varObject = setupVariable(rowData, facesContext);

			getRoot().stackEntries.add(new StackEntry(varObject, key, rowData, this));

			return true;
		}
		
		return false;
	}

	public void setParent(StackingTreeModel parent) {
		this.parent = parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.richfaces.model.AbstractTreeDataModel#getTreeNode()
	 */
	public TreeNode getTreeNode() {
		if (isRowAvailable()) {
			return null;
		}

		throw new IllegalStateException(
				"No tree element available or row key not set!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.richfaces.model.AbstractTreeDataModel#isLeaf()
	 */
	public boolean isLeaf() {
		if (isRowAvailable()) {
			StackEntry lastEntry = (StackEntry) stackEntries.getLast();
			for (Iterator<StackingTreeModel> iterator = lastEntry.model.getInternalModelsIterator(); iterator.hasNext();) {
				StackingTreeModel stackingTreeModel = iterator.next();

				if (!stackingTreeModel.isEmpty()) {
					return false;
				}
			}

			return true;
		}
		
		throw new IllegalStateException(
				"No tree element available or row key not set!");
	}

	protected StackingTreeModel getRoot() {
		if (parent != null) {
			return parent.getRoot();
		}

		return this;
	}

	protected void doWalk(FacesContext context, DataVisitor dataVisitor,
			Range range, ListRowKey argumentKey, Object argument,
			boolean last) throws IOException {

		TreeRange treeRange = (TreeRange) range;
		
		if (treeRange == null || treeRange.processNode(argumentKey)) {
			StackingTreeModel rootModel = getRoot();
			
			if (argumentKey != null) {
				processElement(context, dataVisitor, argument, argumentKey, last);
			}
			
			final ShiftingDataVisitor shiftingDataVisitor = new ShiftingDataVisitor(
					new Visitor1(dataVisitor));
			
			if (treeRange == null || treeRange.processChildren(argumentKey)) {

				Object savedRowKey = rootModel.getRowKey();
				//setup key in order for nested components to initialize data models
				rootModel.setRowKey(context, argumentKey);
				Iterator<StackingTreeModel> iterator = this.getInternalModelsIterator();
				rootModel.setRowKey(context, savedRowKey);

				while (iterator.hasNext()) {
					final StackingTreeModel model = iterator.next();

					savedRowKey = rootModel.getRowKey(); 
					rootModel.setRowKey(context, argumentKey);
					final ExtendedDataModel scalarModel = model.getDataModel();
					rootModel.setRowKey(context, savedRowKey);
					
					Argument argument2 = new Argument();
					argument2.listRowKey = argumentKey;
					argument2.argument = argument;
					// setup current model
					argument2.model = model;
					argument2.range = range;
					
					scalarModel.walk(context, new DataVisitor() {

						public void process(FacesContext context,
								Object rowKey, Object argument)
								throws IOException {

							Object key = scalarModel.getRowKey();
							scalarModel.setRowKey(rowKey);
							Object data = scalarModel.getRowData();
							
							Object variable = model.setupVariable(data, context);
							boolean activeData = model.isActiveData();
							model.setupVariable(variable, context);
							scalarModel.setRowKey(key);

							if (activeData) {
								shiftingDataVisitor.process(context, rowKey, argument);
							}
						}
						
					}, null, argument2);
					
				}
			}
			
			shiftingDataVisitor.end(context);
		}
	}

	private StackingTreeModel getInternalModelById(String id) {
		StackingTreeModel model = getModelById(id);
		if (model.isActive()) {
			return model;
		}
		
		throw new IllegalStateException();
	}
	
	public StackingTreeModel getModelById(String id) {
		return (StackingTreeModel) models.get(id);
	}
	
	private Iterator<StackingTreeModel> getInternalModelsIterator() {
		return new FilterIterator(getModelsIterator(), ACTIVE_MODEL_PREDICATE);
	}
	
	public Iterator<StackingTreeModel> getModelsIterator() {
		return models.values().iterator();
	}
	
	public void walk(FacesContext context, DataVisitor dataVisitor,
			Range range, Object rowKey, Object argument,
			boolean last) throws IOException {
		StackingTreeModel rootModel = getRoot();
		
		if (rowKey != null) {
			ListRowKey listRowKey = (ListRowKey) rowKey;

			StackingTreeModel treeModel = rootModel.setupKey(listRowKey, context);

			treeModel.doWalk(context, dataVisitor, range, listRowKey, argument,
					last);

		} else {
			doWalk(context, dataVisitor, range, 
					(ListRowKey) rowKey, argument, last);
		}
	}

	private class Argument {
		private ListRowKey listRowKey;
		private StackingTreeModel model;
		private Range range;
		private Object argument;
	}

	private class Visitor1 implements DataVisitor, LastElementAware {
		private DataVisitor dataVisitor;
		private boolean theLast;

		public Visitor1(DataVisitor dataVisitor) {
			super();
			this.dataVisitor = dataVisitor;
		}

		public void process(FacesContext context, Object rowKey, Object argument)
		throws IOException {

			Argument a = (Argument) argument;
			ListRowKey listRowKey = new ListRowKey(a.listRowKey, new StackingTreeModelKey(
					a.model.id, rowKey));
			//System.out.println(".walk() " + (theLast ? " * " : "") + listRowKey);

			a.model.doWalk(context, dataVisitor, a.range, listRowKey, a.argument,
					theLast);
		}

		public void resetLastElement() {
			theLast = false;
		}

		public void setLastElement() {
			theLast = true;
		}

	}

	private static class ShiftingDataVisitor implements DataVisitor {

		private DataVisitor dataVisitor;

		public ShiftingDataVisitor(DataVisitor dataVisitor) {
			super();
			this.dataVisitor = dataVisitor;
		}

		private Object rowKey;
		private Object argument;
		private boolean shifted = false;

		public void process(FacesContext context, Object rowKey, Object argument)
		throws IOException {

			if (!shifted) {
				this.rowKey = rowKey;
				this.argument = argument;
				this.shifted = true;
			} else {
				dataVisitor.process(context, this.rowKey, this.argument);
				this.rowKey = rowKey;
				this.argument = argument;
			}
		}

		public void end(FacesContext context) throws IOException {
			if (shifted) {
				try {
					((LastElementAware) dataVisitor).setLastElement();
					dataVisitor.process(context, this.rowKey, argument);
				} finally {
					((LastElementAware) dataVisitor).resetLastElement();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.richfaces.model.AbstractTreeDataModel#walkModel(javax.faces.context.FacesContext,
	 *      org.ajax4jsf.model.DataVisitor, org.ajax4jsf.model.Range,
	 *      java.lang.Object, java.lang.Object, boolean)
	 */
	public void walkModel(FacesContext facesContext, DataVisitor visitor,
			Range range, Object key, Object argument, boolean last)
	throws IOException {

		walk(facesContext, visitor, range, key, argument, last);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ajax4jsf.model.ExtendedDataModel#getRowKey()
	 */
	public Object getRowKey() {
		return rowKey;
	}

	public void setRowKey(Object key) {
		setRowKey(FacesContext.getCurrentInstance(), key);
	}

	public void setRowKey(FacesContext context, Object key) {
		setupKey(key, context);
	}

	public void addStackingModel(StackingTreeModel model) {
		this.models.put(model.id, model);
		model.setParent(this);
	}

	public void removeStackingModel(StackingTreeModel model) {
		this.models.remove(model.id);
		model.setParent(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.model.DataModel#getRowData()
	 */
	public Object getRowData() {
		if (isRowAvailable()) {
			StackEntry lastEntry = (StackEntry) stackEntries.getLast();
			return lastEntry.rowData;
		}
		
		throw new IllegalStateException(
				"No tree element available or row key not set!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.model.DataModel#isRowAvailable()
	 */
	public boolean isRowAvailable() {
		return !stackEntries.isEmpty();
	}

	public StackingTreeModel getParent() {
		return parent;
	}

	protected boolean isActiveData() {
		return true;
	}
	
	protected boolean isActive() {
		return true;
	}

	private final static Predicate ACTIVE_MODEL_PREDICATE = new Predicate() {

		public boolean evaluate(Object object) {
			StackingTreeModel model = (StackingTreeModel) object;
			if (model == null) {
				return false;
			}

			return model.isActive();
		}
		
	};

	@Override
	public Object getWrappedData() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setWrappedData(Object data) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object convertToKey(FacesContext context, String keyString, 
		UIComponent component, Converter converter) {

	    //force model leave
	    setRowKey(context, null);
	    
	    String[] strings = ListRowKey.fromString(keyString);
	    int l = strings.length / 2;
	    List<Object> list = new ArrayList<Object>(l);
	    StackingTreeModel model = getRoot();
	    
	    for (int i = 0; i < l; i++) {
		int idx = i*2;

		String modelId = strings[idx];
		model = model.getModelById(modelId);
		if (model.isActive()) {
			Object key = model.convert(context, strings[idx + 1], component, converter);
			if (key == null) {
			    return null;
			}
			
			list.add(new StackingTreeModelKey(modelId, key));
			
			if (!model.setupModel(key, context) || !model.isActiveData()) {
			    return null;
			}
		} else {
		    return null;
		}
	    }
	    
	    return new ListRowKey<Object>(list);
	}

	protected Object convert(FacesContext context, String string, 
		UIComponent component, Converter converter) {
	    
	    ConvertableKeyModel convertable = (ConvertableKeyModel) getDataModel();
	    return convertable.getKeyAsObject(context, string, component, converter);
	}
	
	public String getId() {
		return id;
	}
}

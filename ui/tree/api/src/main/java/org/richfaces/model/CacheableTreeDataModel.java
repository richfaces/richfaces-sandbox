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

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Extension of {@link TreeDataModel} supporting lazy data fetching for caching
 * 
 * created 08.01.2007
 * 
 * @author Nick - mailto:nbelaevski@exadel.com 
 */
public abstract class CacheableTreeDataModel<T> extends TreeDataModel<T> {
	
	private Log log = LogFactory.getLog(CacheableTreeDataModel.class);
	
	private final class CacheFillingVisitor implements DataVisitor {

		private CacheFillingVisitor() {
		}

		public void process(FacesContext context, Object rowKey, Object argument)
				throws IOException {
			TreeRowKey treeRowKey = (TreeRowKey) rowKey;
			treeDataModel.setRowKey(treeRowKey);
			setDefaultNodeData(locateTreeNode(treeRowKey, true), treeDataModel.getRowData());
		}

	}

	private final static DataVisitor NULL_VISITOR = new DataVisitor() {

	    public void process(FacesContext context, Object rowKey, Object argument) throws IOException {
		//do nothing
	    }
	    
	};
	
	private TreeDataModel<T> treeDataModel;

	public boolean isLeaf() {
		TreeRowKey rowKey = (TreeRowKey) getRowKey();
		T treeNode = locateTreeNode(rowKey);
		if (treeNode != null && !nodeAdaptor.isLeaf(treeNode)) {
			return false;
		}
			
		treeNode = treeDataModel.locateTreeNode(rowKey);
		if (treeNode != null) {
			return nodeAdaptor.isLeaf(treeNode);
		}

		return false;
	}

	public CacheableTreeDataModel(TreeDataModel<T> model, MissingNodeHandler<T> missingNodeHandler) {
		super(model.getClazz(), model.getNodeAdaptor(), missingNodeHandler);
		setWrappedData(missingNodeHandler.handleMissingNode(null, null));
		setTreeDataModel(model);
	}

	public void walkModel(FacesContext context, DataVisitor visitor,
			Range range, Object key, Object argument, boolean last)
			throws IOException {
		treeDataModel.walkModel(context, visitor, range, key,
				argument, last);
	}

	public void setTreeDataModel(TreeDataModel<T> treeDataModel) {
		this.treeDataModel = treeDataModel;
	}
	
	public TreeDataModel<T> getTreeDataModel() {
		return treeDataModel;
	}

	public void walk(FacesContext context, final DataVisitor dataVisitor,
			Range range, Object rowKey, Object argument, boolean last)
			throws IOException {
		
		T cachedTreeNode = locateTreeNode((TreeRowKey) rowKey);
		T treeNode = treeDataModel.locateTreeNode((TreeRowKey) rowKey);
		
		if (treeNode != null) {
			if (cachedTreeNode == null || (nodeAdaptor.isLeaf(cachedTreeNode) && !nodeAdaptor.isLeaf(treeNode))) {
				//fill cache
				treeDataModel.walk(context, new CacheFillingVisitor(), range,
						rowKey, argument, last);
			}

			super.walk(context, dataVisitor, range, rowKey, argument, last);
		}
	}

	public void setTransient(boolean newTransientValue) {
		if (!newTransientValue) {
			throw new IllegalArgumentException(
					"ReplaceableTreeDataModel shouldn't be transient!");
		}
	}
	
	protected abstract void setDefaultNodeData(T node, Object data);

	@Override
	public Object convertToKey(FacesContext context, String keyString, UIComponent component, Converter converter) {
	Object convertedKey = treeDataModel.convertToKey(context, keyString, component, converter);

	if (convertedKey != null) {
	    final TreeRowKey treeRowKey = (TreeRowKey) convertedKey;
	    try {
		walk(context, NULL_VISITOR, new TreeRange() {

		public boolean processChildren(TreeRowKey rowKey) {
		    return rowKey == null || rowKey.isSubKey(treeRowKey);
		}

		public boolean processNode(TreeRowKey rowKey) {
		    return this.processChildren(rowKey) || rowKey.equals(treeRowKey);
		}
		
		}, null);
	    } catch (IOException e) {
		context.getExternalContext().log(e.getLocalizedMessage(), e);
		
		return null;
	    }
	}
	
	return convertedKey;
	}
	
	@Override
	public T locateTreeNode(TreeRowKey rowKey) {
		return locateTreeNode(rowKey, true);
	}
	
	@Override
	public void addNode(Object parentRowKey, TreeNode newNode, Object id) {
	    	super.addNode(parentRowKey, newNode, id);
	    	
    		if (treeDataModel != null) {
    			Object savedRowKey = treeDataModel.getRowKey();

    			try {
    				treeDataModel.setRowKey(getRowKey());
    				treeDataModel.addNode(parentRowKey, newNode, id);
    			} finally {
    				try {
    					treeDataModel.setRowKey(savedRowKey);
    				} catch (Exception e) {
    					log.error(e.getMessage(), e);
    				}
    			}
    		}
        }
	
	@Override
	public void removeNode(Object rowKey) {
	    	super.removeNode(rowKey);
	    	
	    	if (treeDataModel != null) {
	    		Object savedRowKey = treeDataModel.getRowKey();

    			try {
    				treeDataModel.setRowKey(getRowKey());
    				treeDataModel.removeNode(rowKey);
    			} finally {
    				try {
    					treeDataModel.setRowKey(savedRowKey);
    				} catch (Exception e) {
    					log.error(e.getMessage(), e);
    				}
    			}
		}
	}
	
	@Override
	public TreeNode getModelTreeNode() {
		TreeNode node = null;
		if (treeDataModel != null) {
	    		Object savedRowKey = treeDataModel.getRowKey();

    			try {
    				treeDataModel.setRowKey(getRowKey());
    				node = treeDataModel.getModelTreeNode();
    			} finally {
    				try {
    					treeDataModel.setRowKey(savedRowKey);
    				} catch (Exception e) {
    					log.error(e.getMessage(), e);
    				}
    			}
		}
		
		return node;
	}
}
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

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.Range;

/**
 * Base class for all tree data models
 * @author Nick Belaevski - nbelaevski@exadel.com created 07.12.2006
 */
public abstract class AbstractTreeDataModel extends ExtendedDataModel {
	public final static char SEPARATOR = NamingContainer.SEPARATOR_CHAR;

	public final int getRowCount() {
		return -1;
	}

	public final int getRowIndex() {
		return -1;
	}

	public final void setRowIndex(int rowIndex) {
		if(rowIndex!=-1) {
			throw new IllegalArgumentException("In AbstractTreeDataModel rowIndex must be -1.");
		}
	}

	public final void walk(FacesContext context, DataVisitor dataVisitor,
			Range range, Object argument) throws IOException {
		walk(context, dataVisitor, range, null, argument, false);
	}

	/**
	 * walk sub-model having row key argument as its root
	 * @param context faces context
	 * @param dataVisitor {@link org.ajax4jsf.ajax.repeat.UIDataAdaptor.ComponentVisitor}
	 * @param range {@link TreeRange} to constraint the walk 
	 * @param rowKey row key to treat as root of sub-model
	 * @param argument implementation specific argument
	 * @param last boolean flag indicating whether we started our walk from last element
	 * @throws IOException
	 * 
	 * @see {@link ExtendedDataModel#walk(FacesContext, DataVisitor, Range, Object)}
	 */
	public abstract void walk(FacesContext context, DataVisitor dataVisitor,
			Range range, Object rowKey, Object argument, boolean last) throws IOException;

	/**
	 * returns whether this node is leaf
	 * @return
	 */
	public abstract boolean isLeaf();

	/**
	 * Walk backing sub-model having row key argument as its root. If there is no backing model
	 * configured, calling this method is equivalent to calling {@link #walk(FacesContext, DataVisitor, Range, Object, Object, boolean)}
	 * @param facesContext faces context
	 * @param visitor {@link org.ajax4jsf.ajax.repeat.UIDataAdaptor.ComponentVisitor} instance
	 * @param range {@link Range} to constraint the walk
	 * @param key row key to treat as root of sub-model
	 * @param argument implementation-specific argument
	 * @throws IOException
	 * 
	 * @see {@link #walk(FacesContext, DataVisitor, Range, TreeRowKey, Object, boolean)}
	 */
	public abstract void walkModel(FacesContext facesContext, DataVisitor visitor, Range range, Object key, Object argument, boolean last) throws IOException;

	/**
	 * Processes concrete tree node. Knows about {@link LastElementAware} interface and handles it
	 * properly. Checks if argument is instance of {@link SubTreeChildrenAppender} and if it is so
	 * does appending current element
	 * @param context
	 * @param dataVisitor
	 * @param argument
	 * @param treeRowKey
	 * @param last
	 * @throws IOException
	 */
	protected void processElement(FacesContext context, DataVisitor dataVisitor, Object argument, TreeRowKey treeRowKey, boolean last) throws IOException {
		if (last && dataVisitor instanceof LastElementAware) {
			try {
				((LastElementAware) dataVisitor).setLastElement();
				dataVisitor.process(context, treeRowKey, argument);
			} finally {
				((LastElementAware) dataVisitor).resetLastElement();
			}
		} else {
			dataVisitor.process(context, treeRowKey, argument);
		}
	}
	
	/**
	 * Get current tree node.
	 * Note: valid only for classical TreeNode based tree data model implementation
	 * 
	 * @return current tree node
	 */
	public abstract TreeNode getTreeNode();
	
	/**
	 * Get model node recursively with all sub nodes.
	 * Note: valid only for classical TreeNode based tree data model implementation
	 * 
	 * @return current tree node
	 */
	public TreeNode getModelTreeNode() {
	    return getTreeNode();
	}

	public abstract Object convertToKey(FacesContext context, String keyString, 
		UIComponent component, Converter converter);
	
	/**
	 * Get row key for certain tree node object
	 * 
	 * @param node to get key for
	 * @return node row key
	 */
	public Object getTreeNodeRowKey(Object node) {
		throw new UnsupportedOperationException();		
	}
	
	/**
	 * Get node local id in it's parent childs collection
	 * 
	 * @param childNode node to get identifier for
	 * @return node local identifier
	 */
	public Object getChildNodeId(Object childNode) {
		throw new UnsupportedOperationException();
	}
	
	public Object getParentRowKey(Object key) {
		return ((TreeRowKey) key).getParentKey();
	}

	public void addNode(Object parentRowKey, Object newNode, Object id) {
	    throw new UnsupportedOperationException();
	}

	public void addNode(Object parentRowKey, TreeNode newNode, Object id) {
	    throw new UnsupportedOperationException();
	}
	
	public void removeNode(Object rowKey) {
	    throw new UnsupportedOperationException();
	}
}

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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;

/**
 * That is intended for internal use
 * 
 * @author Nick Belaevski - nbelaevski@exadel.com created 16.11.2006
 * 
 */
public abstract class TreeDataModel<T> extends AbstractTreeDataModel {
	
	private Object wrappedData;
	private Class<T> clazz;
	
	private TreeRowKey currentRowKey;

	private TreeRowKey oldRowKey;

	private Boolean rowAvailable = Boolean.FALSE;
	private T rowTreeData;

	protected final TreeDataModelNodeAdaptor<T> nodeAdaptor;

	private MissingNodeHandler<T> missingNodeHandler;
	
	/**
	 * Interface aimed to handle missing nodes for externally-generated keys. 
	 * Typical usage for the interface is filling model cache
	 * @param <T> generic tree node type
	 * 
	 * @author Nick Belaevski
	 * @since 3.2
	 */
	public static interface MissingNodeHandler<T> {
		T handleMissingNode(T parentNode, Object pathSegment);
	};
	
	public TreeDataModel(Class<T> clazz, TreeDataModelNodeAdaptor<T> nodeAdaptor, 
			MissingNodeHandler<T> missingNodeHandler) {
		
		this.clazz = clazz;
		this.nodeAdaptor = nodeAdaptor;
		this.missingNodeHandler = missingNodeHandler;
	}

	public final Class<T> getClazz() {
		return clazz;
	}
	
	public final TreeDataModelNodeAdaptor<T> getNodeAdaptor() {
		return nodeAdaptor;
	}
	
	public Object getRowKey() {
		return this.currentRowKey;
	}

	public void setRowKey(Object rowKey) {
		if (rowKey != null) {
			ListRowKey newRowKey = (ListRowKey) rowKey;
			this.currentRowKey = newRowKey;
			this.rowAvailable = null;
		} else {
			this.currentRowKey = null;
			this.oldRowKey = null;
			this.rowTreeData = null;
			this.rowAvailable = Boolean.FALSE;
		}
	}

	protected void doWalk(FacesContext context, DataVisitor dataVisitor,
			Range range, Object rowKey, Object argument, boolean last) throws IOException {
		ListRowKey listRowKey = (ListRowKey) rowKey;

		T node = locateTreeNode(listRowKey);

		if (node != null) {
			TreeRange treeRange = (TreeRange) range;

			if (treeRange == null || treeRange.processNode(listRowKey)) {

				//root node is not processed, it is considered fake for this class of models
				if (listRowKey != null && listRowKey.depth() > 0) {
					processElement(context, dataVisitor, argument, listRowKey, last);
				}

				if (treeRange == null || treeRange.processChildren(listRowKey)) {
					if (!nodeAdaptor.isLeaf(node)) {
						Iterator<Map.Entry<Object, T>> children = nodeAdaptor.getChildren(node);

						if (children != null) {
							Map.Entry<Object, T> childEntry = children.hasNext() ? children.next() : null;
							T childNode;
							Object identifier;

							if (childEntry != null) {
								childNode = childEntry.getValue();
								identifier = childEntry.getKey();
							} else {
								childNode = null;
								identifier = null;
							}

							do {
								Map.Entry<Object, T> nextChildEntry = children.hasNext() ? children.next() : null;
								T nextChildNode;
								Object nextIdentifier;

								if (nextChildEntry != null) {
									//TODO consider lazy initialization of value
								    	nextChildNode = nextChildEntry.getValue();
									nextIdentifier = nextChildEntry.getKey();
								} else {
									nextChildNode = null;
									nextIdentifier = null;
								}

								if (childNode != null) {

									boolean isLast = nextChildNode == null;

									ListRowKey newRowKey = new ListRowKey(listRowKey, identifier);
									this.doWalk(context, dataVisitor, range, newRowKey, argument, isLast);
								}

								identifier = nextIdentifier;
								childNode = nextChildNode;
							} while (childNode != null);
						}
					}
				}
			}
		}
	}
	
	public void walk(FacesContext context, DataVisitor dataVisitor,
			Range range, Object rowKey, Object argument, boolean last) throws IOException {

		if (rowKey != null) {
			setRowKey(rowKey);
			if (!isRowAvailable()) {
				throw new IllegalStateException(
						"No tree element available or row key not set!");
			}
		}
		
		doWalk(context, dataVisitor, range, rowKey, argument, last);
	}

	public T locateTreeNode(TreeRowKey rowKey) {
		return locateTreeNode(rowKey, false);
	}

	public T locateTreeNode(TreeRowKey rowKey, boolean allowCreate) {
		T tmpRowTreeData = this.rowTreeData;
		TreeRowKey tmpOldRowKey = this.oldRowKey;
		
		boolean useCached = (rowTreeData != null && rowKey != null && rowKey.equals(tmpOldRowKey));		
		if (!useCached) {
			T rootNode = getData();

			if (rootNode != null) {
				if (rowKey != null) {
					int commonPathLength = rowKey.getCommonPathLength(tmpOldRowKey);
					if (tmpOldRowKey == null) {
						tmpRowTreeData = rootNode;
					} else {
						int rootOpsCount = rowKey.depth();
						int currentUpOpsCount = tmpOldRowKey.depth() - commonPathLength;
						int currentOpsCount = currentUpOpsCount + rootOpsCount - commonPathLength;

						if (rootOpsCount > currentOpsCount) {
							for (int i = 0; i < tmpOldRowKey.depth() 
							- commonPathLength; i++) {

								tmpRowTreeData = nodeAdaptor.getParent(tmpRowTreeData);
							}
						} else {
							commonPathLength = 0;
							tmpRowTreeData = rootNode;
							tmpOldRowKey = null;
						}
					}
					tmpOldRowKey = rowKey;
					Iterator<?> iterator = rowKey.getSubPathIterator(commonPathLength);
					while (iterator.hasNext()) {
						//TODO nick - check rowTreeData for null
						
						Object pathSegment = iterator.next();
						T childRowTreeData = nodeAdaptor.getChild(tmpRowTreeData, pathSegment);

						if (childRowTreeData == null) {
							if (!allowCreate) {
								//TODO nick - reset rowTreeData
								return null;
							} else {
								if (missingNodeHandler != null) {
									childRowTreeData = missingNodeHandler.
										handleMissingNode(tmpRowTreeData, pathSegment);

									if (childRowTreeData == null) {
										return null;
									}
								} else {
									return null;
								}
							}
						}

						tmpRowTreeData = childRowTreeData;
					}
				} else {
					return rootNode;
				}
			} else {
				return null;
			}
		}
		
		// check whether we were found something and store it
		if (tmpRowTreeData != null) {
			rowTreeData = tmpRowTreeData;
			oldRowKey = tmpOldRowKey;
		}
		return rowTreeData;
	}

	public boolean isRowAvailable() {
		if (Boolean.FALSE.equals(rowAvailable)) {
			return false;
		}
		
		T data = locateTreeNode(this.currentRowKey);

		if (data != null) {
			return true;
		}

		return false;
	}

	public Object getRowData() {
		if (isRowAvailable()) {
			T treeNode = locateTreeNode(this.currentRowKey);
			if (treeNode != null) {
				return nodeAdaptor.getRowData(treeNode);
			}

			return null;
		}
		

		throw new IllegalStateException(
				"No tree element available or row key not set!");
	}

	public boolean isLeaf() {
		if (isRowAvailable()) {
			T treeNode = locateTreeNode(this.currentRowKey);
			if (treeNode != null) {
				return nodeAdaptor.isLeaf(treeNode);
			}
		}

		throw new IllegalStateException(
				"No tree element available or row key not set!");
	}

	public void walkModel(FacesContext context, DataVisitor visitor, Range range, Object key, Object argument, boolean last) throws IOException {
		walk(context, visitor, range, key, argument, last);
	}

	@Override
	public Object getWrappedData() {
		return wrappedData;
	}

	@Override
	public void setWrappedData(Object data) {
		this.wrappedData = data;
	}
	
	protected T getData() {
		return clazz.cast(wrappedData);
	}
	
	public TreeNode<T> getTreeNode() {
		return null;
	}

	/**
	 * Get row key for certain tree node object
	 * 
	 * @param node to get key for
	 * @return node row key
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Object getTreeNodeRowKey(Object node) {
		if (node == null) {
			throw new UnsupportedOperationException();
		}

		T childNode = (T) node; 
		List<Object> path = new LinkedList<Object>();
		Object id = nodeAdaptor.getChildNodeId(childNode);
		if (id != null) {
			path.add(id);
		}

		T parentNode = childNode;
		while ((parentNode = nodeAdaptor.getParent(parentNode)) != null) {
			id = nodeAdaptor.getChildNodeId(parentNode);
			if (id != null) {
				path.add(0, id);
			} else if (nodeAdaptor.getParent(parentNode) != null) {
				throw new UnsupportedOperationException();
			}
		}
		return new ListRowKey(path);
	}
	
	/**
	 * Get node local id in it's parent childs collection
	 * 
	 * @param childNode node to get identifier for
	 * @return node local identifier
	 */
	@Override
	public Object getChildNodeId(Object childNode) {
		return getNodeAdaptor().getChildNodeId((T) childNode);
	}

	@Override
	public void addNode(Object parentRowKey, TreeNode newNode, Object id) {
		addNode(parentRowKey, (Object) newNode, id);
	}
	
	public void addNode(Object parentRowKey, Object newNode, Object id) {
		Object initialRowKey = getRowKey();
		try {
			T parentTreeNode = locateTreeNode((TreeRowKey) parentRowKey);
			
			if (parentTreeNode == null) {
	    		throw new IllegalArgumentException("Parent node not found!");
			}
			
			getNodeAdaptor().appendChild(parentTreeNode, id, clazz.cast(newNode));
		} finally {
			setRowKey(initialRowKey);
		}
    }
	
	@Override
	public void removeNode(Object rowKey) {
		Object initialRowKey = getRowKey();
		try {
			T treeNode = locateTreeNode((TreeRowKey) rowKey);
			
			if (treeNode == null) {
	    		throw new IllegalArgumentException("Node not fond!");
			}

			getNodeAdaptor().removeFromParent(treeNode);
		} finally {
			setRowKey(initialRowKey);
		}
	}

}

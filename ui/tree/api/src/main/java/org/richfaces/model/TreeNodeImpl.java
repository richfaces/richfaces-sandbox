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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Default {@link TreeNode} implementation based on {@link LinkedHashMap} to preserve 
 * elements ordering
 * 
 * @author Nick Belaevski - nbelaevski@exadel.com
 * created 16.11.2006
 */
public class TreeNodeImpl<T> implements TreeNode<T> {
	
	private static final long serialVersionUID = -5498990493803705085L;
	private T data;
	private TreeNode<T> parent;
	
	private Map<Object, TreeNode<T>> childrenMap = 
		new LinkedHashMap<Object, TreeNode<T>>();
	
	public T getData() {
		return data;
	}

	public TreeNode<T> getChild(Object identifier) {
		return (TreeNode<T>) childrenMap.get(identifier);
	}

	public void addChild(Object identifier, TreeNode<T> child) {
		child.setParent(this);
		childrenMap.put(identifier, child);
	}

	public void removeChild(Object identifier) {
		TreeNode<T> treeNode = childrenMap.remove(identifier);
		if (treeNode != null) {
			treeNode.setParent(null);
		}
	}

	public void setData(T data) {
		this.data = data;
	}

	public TreeNode<T> getParent() {
		return parent;
	}

	public void setParent(TreeNode<T> parent) {
		this.parent = parent;
	}

	public Iterator<Map.Entry<Object, TreeNode<T>>> getChildren() {
		return childrenMap.entrySet().iterator();
	}

	public boolean isLeaf() {
		return childrenMap.isEmpty();
	}

}

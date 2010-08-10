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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.swing.tree.TreeNode;

/**
 * {@link TreeDataModel} implementation for classic {@link TreeNode} instances
 * 
 * Created 01.11.2007
 * 
 * @author Nick Belaevski
 * @since 3.2
 */

public class SwingTreeDataModel extends TreeDataModel<TreeNode> {

	private TreeNode treeNode;
	
	public SwingTreeDataModel() {
		super(TreeNode.class, TreeDataModelNodeAdaptor.swingTreeNodeAdaptor, null);
	}

	@Override
	protected TreeNode getData() {
		return treeNode;
	}
	
	@Override
	public void setWrappedData(Object data) {
		if (data != null) {
			SwingTreeNodeImpl treeNodeImpl = new SwingTreeNodeImpl();
			
			if (data instanceof Collection<?>) {
				Collection<?> collection = (Collection<?>) data;
				
				for (Iterator<?> iterator = collection.iterator(); iterator
						.hasNext();) {
					treeNodeImpl.addChild((TreeNode) iterator.next());
				}
			} else if (data.getClass().isArray()) {
				Object[] nodes = (Object[]) data;
				for (int i = 0; i < nodes.length; i++) {
					treeNodeImpl.addChild((TreeNode) nodes[i]);
				}
			} else {
				treeNodeImpl.addChild((TreeNode) data);
			}
			
			this.treeNode = treeNodeImpl;
		} else {
			this.treeNode = null;
		}

		super.setWrappedData(data);
	}


	@Override
	public Object convertToKey(FacesContext context, String keyString, UIComponent component, Converter converter) {
	    String[] strings = ListRowKey.fromString(keyString);
	    List<Integer> list = new ArrayList<Integer>(strings.length);
	    TreeNode node = this.treeNode;
	    
	    if (node != null) {
		TreeDataModelNodeAdaptor<TreeNode> adaptor = getNodeAdaptor();

		for (int i = 0; i < strings.length; i++) {
		    int key = Integer.parseInt(strings[i]);

		    node = adaptor.getChild(node, key);
		    if (node != null) {
			list.add(key);
		    } else {
			return null;
		    }
		}
	    }

	    return new ListRowKey<Integer>(list);
	}

}

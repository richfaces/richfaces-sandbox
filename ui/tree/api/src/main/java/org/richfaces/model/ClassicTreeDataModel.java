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
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * {@link TreeDataModel} implementation for classic {@link TreeNode} instances
 * 
 * Created 01.11.2007
 * 
 * @author Nick Belaevski
 * @since 3.2
 */

public class ClassicTreeDataModel extends TreeDataModel<TreeNode> {

	public ClassicTreeDataModel() {
		super(TreeNode.class, TreeDataModelNodeAdaptor.classicTreeNodeAdaptor, null);
	}

	@Override
	public TreeNode getTreeNode() {
		if (isRowAvailable()) {
			return locateTreeNode((TreeRowKey) getRowKey());
		}

		throw new IllegalStateException(
				"No tree element available or row key not set!");
	}
	
	@Override
	public Object convertToKey(FacesContext context, String keyString, UIComponent component, Converter converter) {
	    //TODO optimize search for empty string
		String[] strings = ListRowKey.fromString(keyString);
	    List<Object> list = new ArrayList<Object>(strings.length);
	    TreeNode node = (TreeNode) getWrappedData();
	    
	    for (int i = 0; i < strings.length; i++) {
		String key = strings[i];
		TreeNode<?> child = node.getChild(key);
		if (child != null) {
		    node = child;
		    list.add(key);
		} else {
		    boolean found = false;
		    Iterator<Entry<Object, TreeNode>> children = node.getChildren();

		    while (children.hasNext() && !found) {
			Entry<Object, TreeNode> entry = children.next();
			Object keyObject = entry.getKey();
			if (key.equals(keyObject.toString())) {
			    node = entry.getValue();
			    list.add(keyObject);
			    found = true;
			}
		    }
		    
		    if (!found) {
			return null;
		    }
		}
	    }
	    
	    return new ListRowKey<Object>(list);
	}

}

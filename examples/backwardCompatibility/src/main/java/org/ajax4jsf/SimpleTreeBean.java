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

package org.ajax4jsf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.richfaces.component.UITree;
import org.richfaces.event.NodeSelectedEvent;
import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;

public class SimpleTreeBean {
	
	private TreeNode rootNode = null;
	
	private String nodeTitle;
	
	private static final String DATA_PATH = "/WEB-INF/simple-tree-data.properties";
	
	private void addNodes(String path, TreeNode node, Properties properties) {
		boolean end = false;
		int counter = 1;
		
		while (!end) {
			String key = path != null ? path + '.' + counter : String.valueOf(counter);

			String value = properties.getProperty(key);
			if (value != null) {
				TreeNodeImpl nodeImpl = new TreeNodeImpl();
				nodeImpl.setData(value);
				node.addChild(new Integer(counter), nodeImpl);
				
				addNodes(key, nodeImpl, properties);
				
				counter++;
			} else {
				end = true;
			}
		}
	}
	
	private void loadTree() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		InputStream dataStream = externalContext.getResourceAsStream(DATA_PATH);
		try {
			Properties properties = new Properties();
			properties.load(dataStream);
			
			rootNode = new TreeNodeImpl();
			addNodes(null, rootNode, properties);
			
		} catch (IOException e) {
			throw new FacesException(e.getMessage(), e);
		} finally {
			if (dataStream != null) {
				try {
					dataStream.close();
				} catch (IOException e) {
					externalContext.log(e.getMessage(), e);
				}
			}
		}
	}
	
	public TreeNode getTreeNode() {
		if (rootNode == null) {
			loadTree();
		}
		
		return rootNode;
	}

	public void processSelection(NodeSelectedEvent event) {
		UITree tree = (UITree) event.getComponent();
		nodeTitle = (String) tree.getRowData();
	}
	
	public String getNodeTitle() {
		return nodeTitle;
	}

	public void setNodeTitle(String nodeTitle) {
		this.nodeTitle = nodeTitle;
	}
	
}

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

import javax.swing.tree.TreeNode;

/**
 * {@link CacheableTreeDataModel} implementation for Swing {@link TreeNode} instances
 * 
 * Created 01.11.2007
 * 
 * @author Nick Belaevski
 * @since 3.2
 */

public class SwingCacheableTreeDataModel extends CacheableTreeDataModel<TreeNode> {

	private static final MissingNodeHandler<TreeNode> missingNodeHandler = new MissingNodeHandler<TreeNode>() {

		public TreeNode handleMissingNode(TreeNode parent,
				Object pathSegment) {
			
			SwingTreeNodeImpl childNode = new CacheableSwingTreeNodeImpl();

			if (parent != null) {
				SwingTreeNodeImpl parentNode = (SwingTreeNodeImpl) parent;

				parentNode.addChild(pathSegment, childNode);
				childNode.setParent(parentNode);
			}

			return childNode;
		}
		
	};

	public SwingCacheableTreeDataModel(TreeDataModel<TreeNode> model) {
		super(model, missingNodeHandler);
	}

	@Override
	public Object getRowData() {
		SwingTreeNodeImpl swingTreeNodeImpl = ((SwingTreeNodeImpl) super.getRowData());
		if (swingTreeNodeImpl != null) {
			return swingTreeNodeImpl.getData();
		} else {
			return null;
		}
	}
	
	@Override
	protected void setDefaultNodeData(TreeNode node, Object data) {
		CacheableSwingTreeNodeImpl cacheableTreeNode = (CacheableSwingTreeNodeImpl) node;
		cacheableTreeNode.setData(data);
		cacheableTreeNode.setNotLeaf(!isLeaf());
	}
}


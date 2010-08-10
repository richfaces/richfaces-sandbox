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

/**
 * {@link CacheableTreeDataModel} implementation for classic {@link TreeNode} instances
 * 
 * Created 01.11.2007
 * 
 * @author Nick Belaevski
 * @since 3.2
 */

public class ClassicCacheableTreeDataModel extends CacheableTreeDataModel<TreeNode> {

	private static final MissingNodeHandler<TreeNode> missingNodeHandler = new MissingNodeHandler<TreeNode>() {

		public TreeNode handleMissingNode(TreeNode parentNode,
				Object pathSegment) {
			
			TreeNodeImpl childNode = new CacheableTreeNodeImpl();

			if (parentNode != null) {
				parentNode.addChild(pathSegment, childNode);
			}

			return childNode;
		}
		
	};
	
	public ClassicCacheableTreeDataModel(TreeDataModel<TreeNode> model) {
		super(model, missingNodeHandler);
	}

	@Override
	protected void setDefaultNodeData(TreeNode node, Object data) {
		if (node != null) {
			CacheableTreeNodeImpl cacheableTreeNode = (CacheableTreeNodeImpl) node;
			cacheableTreeNode.setData(data);
			cacheableTreeNode.setNotLeaf(!isLeaf());
		}
	}

	@Override
	public TreeNode getTreeNode() {
		if (isRowAvailable()) {
			return locateTreeNode((TreeRowKey) getRowKey());
		}

		throw new IllegalStateException(
				"No tree element available or row key not set!");
	}	
}

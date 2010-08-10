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
 * This class provides the solution for "false leaves" issue of cacheable trees
 * 
 * @author Nick Belaevski
 * @since 3.3.1
 */

public class CacheableTreeNodeImpl<T> extends TreeNodeImpl<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5918388225735277820L;
	
	private boolean notLeaf = false;
	
	@Override
	public boolean isLeaf() {
		return !this.notLeaf && super.isLeaf();
	}
	
	public void setNotLeaf(boolean notLeaf) {
		this.notLeaf = notLeaf;
	}
}

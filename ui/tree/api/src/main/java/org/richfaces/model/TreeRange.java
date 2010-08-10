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

import org.ajax4jsf.model.Range;

/**
 * {@link Range} implmentation variant for {@link AbstractTreeDataModel}
 * 
 * @author Nick Belaevski - nbelaevski@exadel.com
 * created 23.11.2006
 */
public interface TreeRange extends Range {

	/**
	 * Returns <code>true</code> if node with that rowKey should be processed.
	 * If <code>false</code> is returned, then skips processing child nodes also.
	 * @param rowKey {@link TreeRowKey} key instance
	 * @return <code>true</code> or <code>false</code>
	 */
	public boolean processNode(TreeRowKey rowKey);

	/**
	 * Returns <code>true</code> if children of the node with that rowKey should be processed
	 * @param rowKey {@link TreeRowKey} key instance
	 * @return <code>true</code> or <code>false</code>
	 */
	public boolean processChildren(TreeRowKey rowKey);


	/**
	 * Unconstrained variant of {@link TreeRange}. 
	 * Is stateless and safe to use in multi-threaded environments.
	 * 
	 * @since 3.2.2
	 * @author Nick Belaevski
	 */
	public final static TreeRange RANGE_UNCONSTRAINED = new TreeRange() {
		public boolean processChildren(TreeRowKey rowKey) {
			return true;
		}

		public boolean processNode(TreeRowKey rowKey) {
			return true;
		}
	};
}

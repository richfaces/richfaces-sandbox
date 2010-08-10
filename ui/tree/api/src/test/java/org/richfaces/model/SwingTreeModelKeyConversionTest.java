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

import javax.swing.tree.TreeNode;

import junit.framework.TestCase;

/**
 * Created 26.08.2008
 * @author Nick Belaevski
 * @since 3.2.2
 */

public class SwingTreeModelKeyConversionTest extends TestCase {

	private SwingTreeDataModel dataModel;
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		dataModel = new SwingTreeDataModel();
	
		//1
		SwingTreeNodeImpl node = new SwingTreeNodeImpl();
		//1:0
		node.addChild(new SwingTreeNodeImpl());
		//1:1
		node.addChild(new SwingTreeNodeImpl());
		//1:2
		node.addChild(new SwingTreeNodeImpl());
		
		dataModel.setWrappedData(new TreeNode[] {
			new SwingTreeNodeImpl(), node
		});
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();

		dataModel = null;
	}

	public void testEmptyString() throws Exception {
		//TODO make it work
//		Object key = dataModel.convertToKey(null, "", null, null);
//		assertNull(key);
	}
	
	public void testStraightWay() throws Exception {
		TreeRowKey<Integer> key;
		Iterator<Integer> iterator;
		
		key = (TreeRowKey<Integer>) dataModel.convertToKey(null, "0", null, null);
		iterator = key.iterator();
		assertEquals(Integer.valueOf(0), iterator.next());
		assertFalse(iterator.hasNext());
	
		key = (TreeRowKey<Integer>) dataModel.convertToKey(null, "1", null, null);
		iterator = key.iterator();
		assertEquals(Integer.valueOf(1), iterator.next());
		assertFalse(iterator.hasNext());

		key = (TreeRowKey<Integer>) dataModel.convertToKey(null, "1:0", null, null);
		iterator = key.iterator();
		assertEquals(Integer.valueOf(1), iterator.next());
		assertEquals(Integer.valueOf(0), iterator.next());
		assertFalse(iterator.hasNext());

		key = (TreeRowKey<Integer>) dataModel.convertToKey(null, "1:2", null, null);
		iterator = key.iterator();
		assertEquals(Integer.valueOf(1), iterator.next());
		assertEquals(Integer.valueOf(2), iterator.next());
		assertFalse(iterator.hasNext());
	}
	
	public void testMissingNodes() throws Exception {
		assertNull(dataModel.convertToKey(null, "2", null, null));
		assertNull(dataModel.convertToKey(null, "0:3", null, null));
	}
}


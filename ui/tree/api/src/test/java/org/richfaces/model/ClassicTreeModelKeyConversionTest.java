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

import junit.framework.TestCase;

/**
 * Created 26.08.2008
 * @author Nick Belaevski
 * @since 3.2.2
 */

public class ClassicTreeModelKeyConversionTest extends TestCase {

	private ClassicTreeDataModel dataModel;
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		dataModel = new ClassicTreeDataModel();

		TreeNodeImpl<String> rootNode = new TreeNodeImpl<String>();
		
		TreeNodeImpl<String> node;
		
		node = new TreeNodeImpl<String>();
		node.addChild("0", new TreeNodeImpl<String>());
		node.addChild("1", new TreeNodeImpl<String>());
		for (int i = 2; i < 5; i++) {
			node.addChild(Long.valueOf(i), new TreeNodeImpl<String>());
		}
		rootNode.addChild("6", node);
		
		node = new TreeNodeImpl<String>();
		for (int i = 0; i < 3; i++) {
			node.addChild(Integer.valueOf(i), new TreeNodeImpl<String>());
		}
		rootNode.addChild("abc_cde:fgh", node);
		
		dataModel.setWrappedData(rootNode);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();

		dataModel = null;
	}

	public void testEmptyString() throws Exception {
		TreeRowKey<Object> key = (TreeRowKey<Object>) dataModel.convertToKey(null, "", null, null);
		assertNull(key);
	}
	
	public void testStraightWayShort() throws Exception {
		TreeRowKey<Object> key = (TreeRowKey<Object>) dataModel.convertToKey(null, "6", null, null);
		assertNotNull(key);
		
		Iterator<Object> iterator = key.iterator();
		assertEquals("6", iterator.next());
		assertFalse(iterator.hasNext());
	}

	public void testStraightWay() throws Exception {
		TreeRowKey<Object> key = (TreeRowKey<Object>) dataModel.convertToKey(null, "6:1", null, null);
		assertNotNull(key);
		
		Iterator<Object> iterator = key.iterator();
		assertEquals("6", iterator.next());
		assertEquals("1", iterator.next());
		assertFalse(iterator.hasNext());

		key = (TreeRowKey<Object>) dataModel.convertToKey(null, "6:1", null, null);
		assertNotNull(key);
		
		iterator = key.iterator();
		assertEquals("6", iterator.next());
		assertEquals("1", iterator.next());
		assertFalse(iterator.hasNext());
	}
	
	public void testSearch() throws Exception {
		TreeRowKey<Object> key = (TreeRowKey<Object>) dataModel.convertToKey(null, "6:4", null, null);
		assertNotNull(key);
		
		Iterator<Object> iterator = key.iterator();
		assertEquals("6", iterator.next());
		assertEquals(Long.valueOf(4), iterator.next());
		assertFalse(iterator.hasNext());
		
	}

	public void testSearchMissing() throws Exception {
		assertNull(dataModel.convertToKey(null, "6:10", null, null));
		assertNull(dataModel.convertToKey(null, "7", null, null));
	}
	
	public void testUnescape() throws Exception {
		TreeRowKey<Object> key = (TreeRowKey<Object>) dataModel.convertToKey(null, "abc__cde_:fgh:1", null, null);
		assertNotNull(key);
		
		Iterator<Object> iterator = key.iterator();
		assertEquals("abc_cde:fgh", iterator.next());
		assertEquals(Integer.valueOf(1), iterator.next());
		assertFalse(iterator.hasNext());
	}

	public void testUnescapeShort() throws Exception {
		TreeRowKey<Object> key = (TreeRowKey<Object>) dataModel.convertToKey(null, "abc__cde_:fgh", null, null);
		assertNotNull(key);
		
		Iterator<Object> iterator = key.iterator();
		assertEquals("abc_cde:fgh", iterator.next());
		assertFalse(iterator.hasNext());
	}
}

/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.bootstrap.demo.ftest.webdriver.pickList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.support.FindBy;
import org.richfaces.bootstrap.demo.ftest.webdriver.AbstractWebDriverTest;
import org.richfaces.bootstrap.demo.ftest.webdriver.pickList.fragment.BootstrapPickListImpl;
import org.richfaces.bootstrap.demo.ftest.webdriver.pickList.fragment.PickListList;
import org.richfaces.bootstrap.demo.ftest.webdriver.pickList.fragment.PickListSelection.With;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestPickList extends AbstractWebDriverTest {

    private static final String[] CAPITALS = new String[]{ "Montgomery", "Phoenix", "Denver", "Atlanta" };
    private int initListSize;
    //
    @FindBy(css = "div[class='pickList outer']")
    BootstrapPickListImpl pickList;

    private void assertListContainsCapitals(PickListList list, String... capitals) {
        for (String capital : capitals) {
            assertTrue("List don't contains capital " + capital, list.contains(capital));
        }
    }

    private void assertListDontContainsCapitals(PickListList list, String... capitals) {
        for (String capital : capitals) {
            assertFalse("List contains capital " + capital, list.contains(capital));
        }
    }

    private void assertLeftListSize(int size) {
        assertListSize(pickList.getSourceList(), size);
    }

    private void assertRightListSize(int size) {
        assertListSize(pickList.getTargetList(), size);
    }

    private void assertListSize(PickListList list, int size) {
        assertEquals(size, list.size());
    }

    private void assertCapitalsInOrderInRightList(String... capitals) {
        assertCapitalsInOrderInList(pickList.getTargetList(), capitals);
    }

    private void assertCapitalsInOrderInList(PickListList list, String... capitals) {
        assertEquals(list.size(), capitals.length);
        for (int i = 0; i < capitals.length; i++) {
            String capital = list.getKeyAtIndex(i);
            assertEquals(capitals[i], capital);
        }
    }

    @Override
    protected String getComponentName() {
        return "pickList";
    }

    @Before
    public void initializeInitListSize() {
        this.initListSize = pickList.getSourceList().size();
        assertTrue(initListSize > 0);
    }

    @Test
    public void testAddAllRemoveAllButtons() {
        pickList.addAll();
        assertLeftListSize(0);
        assertRightListSize(initListSize);
        pickList.removeAll();
        assertLeftListSize(initListSize);
        assertRightListSize(0);
    }

    @Test
    public void testAddButtonSimple() {
        pickList.selectFromSourceList(CAPITALS[1]).transferByOne(With.button);
        assertListContainsCapitals(pickList.getTargetList(), CAPITALS[1]);
        assertListDontContainsCapitals(pickList.getSourceList(), CAPITALS[1]);
        assertLeftListSize(initListSize - 1);
        assertRightListSize(1);
    }

    @Test
    public void testAddByDnD() {
        pickList.selectFromSourceList(CAPITALS[1]).transferByOne(With.dnd);
        assertListContainsCapitals(pickList.getTargetList(), CAPITALS[1]);
        assertListDontContainsCapitals(pickList.getSourceList(), CAPITALS[1]);
        assertLeftListSize(initListSize - 1);
        assertRightListSize(1);
    }

    @Ignore("cannot select multiple elements with ctrl")
    @Test
    public void testAddMultipleByButton() {
        pickList.selectFromSourceList(CAPITALS).transferMultiple(With.button);

        assertRightListSize(CAPITALS.length);
        assertLeftListSize(initListSize - CAPITALS.length);
        assertListContainsCapitals(pickList.getTargetList(), CAPITALS);
        assertListDontContainsCapitals(pickList.getSourceList(), CAPITALS);
    }

    @Ignore(value = "cannot select multiple elements with ctrl")
    @Test
    public void testAddMultipleByDnD() {
        pickList.selectFromSourceList(CAPITALS).transferMultiple(With.dnd);

        assertRightListSize(CAPITALS.length);
        assertLeftListSize(initListSize - CAPITALS.length);
        assertListContainsCapitals(pickList.getTargetList(), CAPITALS);
        assertListDontContainsCapitals(pickList.getSourceList(), CAPITALS);
    }

    @Test
    public void testInit() {
        assertTrue("PickList is not visible", pickList.isVisible());
        assertFalse("Source list of pickList should not be empty", pickList.getSourceList().isEmpty());
        assertTrue("Target list of pickList should be empty", pickList.getTargetList().isEmpty());
    }

    @Test
    public void testListOrderingSimpleByButtons() {
        pickList.selectFromSourceList(CAPITALS[2], CAPITALS[1], CAPITALS[0]).transferByOne(With.button);
        //reverse order, the last added will be on peak
        assertCapitalsInOrderInRightList(CAPITALS[0], CAPITALS[1], CAPITALS[2]);

        //test 'first' button
        pickList.selectFromTargetList(CAPITALS[2]).first();
        assertCapitalsInOrderInRightList(CAPITALS[2], CAPITALS[0], CAPITALS[1]);
        pickList.selectFromTargetList(CAPITALS[2]).first();
        assertCapitalsInOrderInRightList(CAPITALS[2], CAPITALS[0], CAPITALS[1]);

        //test 'up' button
        pickList.selectFromTargetList(CAPITALS[0]).up();
        assertCapitalsInOrderInRightList(CAPITALS[0], CAPITALS[2], CAPITALS[1]);
        pickList.selectFromTargetList(CAPITALS[0]).up();
        assertCapitalsInOrderInRightList(CAPITALS[0], CAPITALS[2], CAPITALS[1]);

        //test 'down' button
        pickList.selectFromTargetList(CAPITALS[2]).down();
        assertCapitalsInOrderInRightList(CAPITALS[0], CAPITALS[1], CAPITALS[2]);
        pickList.selectFromTargetList(CAPITALS[2]).down();
        assertCapitalsInOrderInRightList(CAPITALS[0], CAPITALS[1], CAPITALS[2]);

        //test 'last' button
        pickList.selectFromTargetList(CAPITALS[0]).last();
        assertCapitalsInOrderInRightList(CAPITALS[1], CAPITALS[2], CAPITALS[0]);
        pickList.selectFromTargetList(CAPITALS[0]).last();
        assertCapitalsInOrderInRightList(CAPITALS[1], CAPITALS[2], CAPITALS[0]);
    }

    @Test
    public void testRemoveButtonSimple() {
        pickList.selectFromSourceList(CAPITALS[1]).transferByOne(With.button);
        pickList.selectFromTargetList(CAPITALS[1]).transferByOne(With.button);
        assertLeftListSize(initListSize);
        assertListContainsCapitals(pickList.getSourceList(), CAPITALS[1]);
        assertRightListSize(0);
    }

    @Test
    public void testRemoveByDnD() {
        pickList.selectFromSourceList(CAPITALS[1]).transferByOne(With.dnd);
        pickList.selectFromTargetList(CAPITALS[1]).transferByOne(With.dnd);
        assertLeftListSize(initListSize);
        assertListContainsCapitals(pickList.getSourceList(), CAPITALS[1]);
        assertRightListSize(0);
    }
}

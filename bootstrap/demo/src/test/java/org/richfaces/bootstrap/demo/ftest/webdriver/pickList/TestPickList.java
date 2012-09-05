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

import static org.testng.Assert.*;

import com.google.common.base.Predicate;
import java.util.List;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.richfaces.bootstrap.demo.ftest.webdriver.AbstractWebDriverTest;
import org.richfaces.bootstrap.demo.ftest.webdriver.BootstrapDemoPageImpl;
import org.richfaces.bootstrap.demo.ftest.webdriver.pickList.TestPickList.PickListPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestPickList extends AbstractWebDriverTest<PickListPage> {

    private static final String[] CAPITALS = new String[]{ "Montgomery", "Phoenix", "Denver", "Atlanta" };
    private int initListSize;

    @Override
    protected PickListPage createPage() {
        return new PickListPage();
    }

    @BeforeMethod(alwaysRun = true)
    public void initializeInitListSize() {
        this.initListSize = page.leftList.size();
        assertTrue(initListSize > 0);
    }

    @Test
    public void testInit() {
        assertTrue(page.pickList.isDisplayed());
        assertFalse(page.leftList.isEmpty());
        assertTrue(page.rightList.isEmpty());
    }

    @Test
    public void testAddAllButton() {
        page.addAllButton.click();
        page.waitForListSizesChange(0, initListSize);
        page.assertLeftListSize(0);
        page.assertRightListSize(initListSize);
    }

    @Test
    public void testRemoveAllButton() {
        page.addAllButton.click();
        page.waitForListSizesChange(0, initListSize);
        page.removeAllButton.click();
        page.waitForListSizesChange(initListSize, 0);
        page.assertLeftListSize(initListSize);
        page.assertRightListSize(0);
    }

    @Test
    public void testAddButtonSimple() {
        page.addByOneWithButton(CAPITALS[1]);
        page.assertListContainsCapitals(page.rightList, CAPITALS[1]);
        page.assertListDontContainsCapitals(page.leftList, CAPITALS[1]);
        page.assertLeftListSize(initListSize - 1);
        page.assertRightListSize(1);
    }

    @Test
    public void testRemoveButtonSimple() {
        page.addByOneWithButton(CAPITALS[1]);
        page.removeByOneWithButton(CAPITALS[1]);
        page.assertLeftListSize(initListSize);
        page.assertListContainsCapitals(page.leftList, CAPITALS[1]);
        page.assertRightListSize(0);
    }

    @Test
    public void testListOrderingSimpleByButton() {
        page.addByOneWithButton(CAPITALS[2], CAPITALS[1], CAPITALS[0]);
        //reverse order, the last added will be on peak
        page.assertCapitalsInOrderInRightList(CAPITALS[0], CAPITALS[1], CAPITALS[2]);

        //test 'first' button
        page.selectItemsFromRightList(CAPITALS[2]);
        page.firstButton.click();
        page.assertCapitalsInOrderInRightList(CAPITALS[2], CAPITALS[0], CAPITALS[1]);
        page.firstButton.click();
        page.assertCapitalsInOrderInRightList(CAPITALS[2], CAPITALS[0], CAPITALS[1]);

        //test 'up' button
        page.selectItemsFromRightList(CAPITALS[0]);
        page.upButton.click();
        page.assertCapitalsInOrderInRightList(CAPITALS[0], CAPITALS[2], CAPITALS[1]);
        page.upButton.click();
        page.assertCapitalsInOrderInRightList(CAPITALS[0], CAPITALS[2], CAPITALS[1]);

        //test 'down' button
        page.selectItemsFromRightList(CAPITALS[2]);
        page.downButton.click();
        page.assertCapitalsInOrderInRightList(CAPITALS[0], CAPITALS[1], CAPITALS[2]);
        page.downButton.click();
        page.assertCapitalsInOrderInRightList(CAPITALS[0], CAPITALS[1], CAPITALS[2]);

        //test 'last' button
        page.selectItemsFromRightList(CAPITALS[0]);
        page.lastButton.click();
        page.assertCapitalsInOrderInRightList(CAPITALS[1], CAPITALS[2], CAPITALS[0]);
        page.lastButton.click();
        page.assertCapitalsInOrderInRightList(CAPITALS[1], CAPITALS[2], CAPITALS[0]);
    }

    @Test
    public void testAddByDnD() {
        page.addByOneWithDnD(CAPITALS[1]);
        page.assertListContainsCapitals(page.rightList, CAPITALS[1]);
        page.assertListDontContainsCapitals(page.leftList, CAPITALS[1]);
        page.assertLeftListSize(initListSize - 1);
        page.assertRightListSize(1);
    }

    @Test
    public void testRemoveByDnD() {
        page.addByOneWithDnD(CAPITALS[1]);
        page.removeByOneWithDnD(CAPITALS[1]);
        page.assertListContainsCapitals(page.leftList, CAPITALS[1]);
        page.assertLeftListSize(initListSize);
        page.assertRightListSize(0);
    }

    /**
     * Following tests do not work. Due to WD DnD problems and not working 
     * multiple selecting with CTRL button.
     */
    @Test(groups="broken")
    public void testListOrderingByDnDSimple() {
        //perform ordering in right list (less capitals)
        page.addByOneWithButton(CAPITALS[2], CAPITALS[1], CAPITALS[0]);
        //reverse order, the last added will be on peak
        page.assertCapitalsInOrderInRightList(CAPITALS[0], CAPITALS[1], CAPITALS[2]);

        //unselect other capitals
        page.selectItemsFromRightList(CAPITALS[2]);
        Actions builder = new Actions(driver);
        //drag and drop the last capital up before the first capital
        page.dragAndDropActionWithNegativeOffset(builder, page.findCapitalInList(page.rightList, CAPITALS[2]), page.findCapitalInList(page.rightList, CAPITALS[0]));
        builder.perform();

        page.assertCapitalsInOrderInRightList(CAPITALS[2], CAPITALS[0], CAPITALS[1]);
    }
    
    @Test(groups = "broken")
    public void testAddMultipleByButton() {
        page.addMultipleWithButton(CAPITALS);

        page.assertRightListSize(CAPITALS.length);
        page.assertLeftListSize(initListSize - CAPITALS.length);
        page.assertListContainsCapitals(page.rightList, CAPITALS);
        page.assertListDontContainsCapitals(page.leftList, CAPITALS);
    }

    @Test(groups = "broken")
    public void testAddMultipleByDnD() {
        page.selectItemsFromLeftList(CAPITALS);

        page.addMultipleWithDnD(CAPITALS);

        page.assertRightListSize(CAPITALS.length);
        page.assertLeftListSize(initListSize - CAPITALS.length);
        page.assertListContainsCapitals(page.rightList, CAPITALS);
        page.assertListDontContainsCapitals(page.leftList, CAPITALS);
    }

    @Test(groups = "broken")
    public void testRemoveMultipleByButton() {
        page.addByOneWithButton(CAPITALS);

        page.removeMultipleWithButton(CAPITALS);

        page.assertRightListSize(0);
        page.assertLeftListSize(initListSize);
        page.assertListContainsCapitals(page.leftList, CAPITALS);
    }

    @Test(groups = "broken")
    public void testRemoveMultipleByDnD() {
        page.addByOneWithButton(CAPITALS);

        page.removeMultipleWithDnD(CAPITALS);

        page.assertRightListSize(0);
        page.assertLeftListSize(initListSize);
        page.assertListContainsCapitals(page.leftList, CAPITALS);
    }

    @Test(groups = "broken")
    public void testMultipleOrderingByButton() {
        page.addByOneWithButton(CAPITALS[2], CAPITALS[1], CAPITALS[0]);
        //reverse order, the last added will be on peak
        page.assertCapitalsInOrderInRightList(CAPITALS[0], CAPITALS[1], CAPITALS[2]);

        //test 'first' button
        page.selectItemsFromRightList(CAPITALS[2], CAPITALS[1]);
        page.firstButton.click();
        page.assertCapitalsInOrderInRightList(CAPITALS[1], CAPITALS[2], CAPITALS[0]);

        //test 'up' button
        page.selectItemsFromRightList(CAPITALS[0], CAPITALS[2]);
        page.upButton.click();
        page.assertCapitalsInOrderInRightList(CAPITALS[2], CAPITALS[0], CAPITALS[1]);

        //test 'down' button
        page.selectItemsFromRightList(CAPITALS[2], CAPITALS[0]);
        page.downButton.click();
        page.assertCapitalsInOrderInRightList(CAPITALS[1], CAPITALS[2], CAPITALS[0]);

        //test 'last' button
        page.selectItemsFromRightList(CAPITALS[1], CAPITALS[2]);
        page.lastButton.click();
        page.assertCapitalsInOrderInRightList(CAPITALS[0], CAPITALS[1], CAPITALS[2]);
    }

    @Test(groups = "broken")
    public void testMultipleOrderingByDnD() {
        page.addByOneWithButton(CAPITALS[2], CAPITALS[1], CAPITALS[0]);
        //reverse order, the last added will be on peak
        page.assertCapitalsInOrderInRightList(CAPITALS[0], CAPITALS[1], CAPITALS[2]);

        //select the last two
        page.selectItemsFromRightList(CAPITALS[2], CAPITALS[1]);
        Actions builder = new Actions(driver);
        //drag and drop it before the first capital
        page.dragAndDropActionWithNegativeOffset(builder, page.findCapitalInList(page.rightList, CAPITALS[2]), page.findCapitalInList(page.rightList, CAPITALS[0]));
        builder.perform();

        page.assertCapitalsInOrderInRightList(CAPITALS[1], CAPITALS[2], CAPITALS[0]);
    }

    protected enum With {

        dnd,
        button
    }

    protected enum WithOffset {

        negative(-10),
        positive(10);
        private final int offset;

        private WithOffset(int offset) {
            this.offset = offset;
        }

        public int getOffset() {
            return offset;
        }
    }

    protected class PickListPage extends BootstrapDemoPageImpl {

        @FindBy(css = "div[class='pickList outer']")
        WebElement pickList;
        @FindBy(css = "button[class='btn right']")
        WebElement addButton;
        @FindBy(css = "button[class='btn rightAll']")
        WebElement addAllButton;
        @FindBy(css = "button[class='btn left']")
        WebElement removeButton;
        @FindBy(css = "button[class='btn leftAll']")
        WebElement removeAllButton;
        @FindBy(css = "button[class='btn first']")
        WebElement firstButton;
        @FindBy(css = "button[class='btn last']")
        WebElement lastButton;
        @FindBy(css = "button[class='btn up']")
        WebElement upButton;
        @FindBy(css = "button[class='btn down']")
        WebElement downButton;
        @FindBy(css = "ol[id$=SourceList] li")
        List<WebElement> leftList;
        @FindBy(css = "ol[id$=TargetList] li")
        List<WebElement> rightList;
        @FindBy(css = "ol[id$=SourceList]")
        WebElement leftListRoot;
        @FindBy(css = "ol[id$=TargetList]")
        WebElement rightListRoot;

        public void addByOne(With with, String... capitals) {
            int leftSize = leftList.size();
            int rightSize = rightList.size();
            switch (with) {
                case button:
                    for (String capital : capitals) {
                        selectItemsFromLeftList(capital);
                        addButton.click();
                    }
                    break;
                case dnd:
                    Actions builder = new Actions(driver);
                    for (String capital : capitals) {
                        dragAndDropActionWithPositiveOffset(builder, findCapitalInList(leftList, capital), rightListRoot);
//                        with native events enabled
//                        builder.dragAndDrop(findCapitalInList(leftList, capital), rightListRoot);
                    }
                    builder.build().perform();
                    break;
            }
            waitForListSizesChange(leftSize - capitals.length, rightSize + capitals.length);
        }

        public void addByOneWithButton(String... capitals) {
            addByOne(With.button, capitals);
        }

        public void addByOneWithDnD(String... capitals) {
            addByOne(With.dnd, capitals);
        }

        public void addMultiple(With with, String... capitals) {
            int leftSize = leftList.size();
            int rightSize = rightList.size();
            switch (with) {
                case button:
                    selectItemsFromLeftList(capitals);
                    addButton.click();
                    break;
                case dnd:
                    //select all capitals
                    selectItemsFromLeftList(capitals);
                    Actions builder = new Actions(driver);
                    //click on the first and drag and drop it
                    dragAndDropActionWithPositiveOffset(builder, findCapitalInList(leftList, capitals[0]), page.rightListRoot);
                    builder.build().perform();
                    break;
            }
            waitForListSizesChange(leftSize - capitals.length, rightSize + capitals.length);
        }

        public void addMultipleWithButton(String... capitals) {
            removeMultiple(With.button, capitals);
        }

        public void addMultipleWithDnD(String... capitals) {
            removeMultiple(With.dnd, capitals);
        }

        public void removeByOne(With with, String... capitals) {
            int leftSize = leftList.size();
            int rightSize = rightList.size();
            switch (with) {
                case button:
                    for (String capital : capitals) {
                        selectItemsFromRightList(capital);
                        removeButton.click();
                    }
                    break;
                case dnd:
                    Actions builder = new Actions(driver);
                    for (String capital : capitals) {
                        dragAndDropActionWithPositiveOffset(builder, findCapitalInList(rightList, capital), leftListRoot);
//                        with native events enabled
//                        builder.dragAndDrop(findCapitalInList(rightList, capital), leftListRoot);
                    }
                    builder.build().perform();
                    break;
            }
            waitForListSizesChange(leftSize + capitals.length, rightSize - capitals.length);
        }

        public void removeByOneWithButton(String... capitals) {
            removeByOne(With.button, capitals);
        }

        public void removeByOneWithDnD(String... capitals) {
            removeByOne(With.dnd, capitals);
        }

        public void removeMultiple(With with, String... capitals) {
            int leftSize = leftList.size();
            int rightSize = rightList.size();
            switch (with) {
                case button:
                    selectItemsFromRightList(capitals);
                    addButton.click();
                    break;
                case dnd:
                    //select all capitals
                    selectItemsFromRightList(capitals);
                    Actions builder = new Actions(driver);
                    //click on the first and drag and drop it
                    dragAndDropActionWithPositiveOffset(builder, findCapitalInList(rightList, capitals[0]), page.leftListRoot);
                    builder.build().perform();
                    break;
            }
            waitForListSizesChange(leftSize - capitals.length, rightSize + capitals.length);
        }

        public void removeMultipleWithButton(String... capitals) {
            removeMultiple(With.button, capitals);
        }

        public void removeMultipleWithDnD(String... capitals) {
            removeMultiple(With.dnd, capitals);
        }

        public void dragAndDropActionWithPositiveOffset(Actions builder, WebElement source, WebElement target) {
            dragAndDropAction(WithOffset.positive, builder, source, target);
        }

        public void dragAndDropActionWithNegativeOffset(Actions builder, WebElement source, WebElement target) {
            dragAndDropAction(WithOffset.negative, builder, source, target);
        }

        /**
         * Workaround for Drag and Drop
         */
        public void dragAndDropAction(WithOffset offset, Actions builder, WebElement source, WebElement target) {
            Point sourceLocation = source.getLocation();
            Point targetLocation = target.getLocation();
            int x = targetLocation.getX() - sourceLocation.getX() + offset.getOffset();
            int y = targetLocation.getY() - sourceLocation.getY() + offset.getOffset();
            builder.dragAndDropBy(source, x, y);
//            builder.clickAndHold(source);
//            builder.moveToElement(target);
//            builder.moveByOffset(offset.getOffset(),offset.getOffset());//second move will throw exception
//            builder.release();
        }

        public void selectItemsFromRightList(String... capitals) {
            selectItemsFromList(rightList, capitals);
        }

        public void selectItemsFromLeftList(String... capitals) {
            selectItemsFromList(leftList, capitals);
        }

        public void selectItemsFromList(List<WebElement> list, String... capitals) {
            if (capitals.length > 1) {
                //multiple selection is not working
                Actions builder = new Actions(driver);
                boolean firstrun = true;
                for (String capital : capitals) {
                    builder.click(findCapitalInList(list, capital));
                    if (firstrun) {
                        firstrun = false;
                        builder.keyDown(Keys.CONTROL);
                    }
                }
                builder.keyUp(Keys.CONTROL);
                builder.build().perform();
            } else {
                findCapitalInList(list, capitals[0]).click();
            }
        }

        public WebElement findCapitalInList(List<WebElement> list, String capital) {
            for (WebElement webElement : list) {
                if (capital.equals(webElement.getAttribute("data-key"))) {
                    return webElement;
                }
            }
            return null;
        }

        public void assertListContainsCapitals(List<WebElement> list, String... capitals) {
            boolean contains;
            for (String capital : capitals) {
                contains = (findCapitalInList(list, capital) != null ? true : false);
                assertTrue(contains, "List don't contains capital " + capital);
            }
        }

        public void assertListDontContainsCapitals(List<WebElement> list, String... capitals) {
            boolean contains;
            for (String capital : capitals) {
                contains = (findCapitalInList(list, capital) != null ? true : false);
                assertFalse(contains, "List contains capital " + capital);
            }
        }

        public void assertLeftListSize(int size) {
            assertListSize(leftList, size);
        }

        public void assertRightListSize(int size) {
            assertListSize(rightList, size);
        }

        public void assertListSize(List<WebElement> list, int size) {
            assertEquals(list.size(), size);
        }

        public void assertCapitalsInOrderInRightList(String... capitals) {
            assertCapitalsInOrderInList(rightList, capitals);
        }

        public void assertCapitalsInOrderInList(List<WebElement> list, String... capitals) {
            assertEquals(list.size(), capitals.length);
            for (int i = 0; i < capitals.length; i++) {
                String elementTextValue = list.get(i).getAttribute("data-key");
                assertEquals(elementTextValue, capitals[i]);
            }
        }

        /**
         * Waits a maximum of 2 seconds for list sizes change. No TimeoutException will be thrown.
         * @param expectedLeftListSize expected size of the left list
         * @param expectedRightListSize expected size of the right list
         */
        public void waitForListSizesChange(final int expectedLeftListSize, final int expectedRightListSize) {
            try {
                new WebDriverWait(driver, 2).until(new Predicate<WebDriver>() {
                    @Override
                    public boolean apply(WebDriver driver) {
                        return page.leftList.size() == expectedLeftListSize && page.rightList.size() == expectedRightListSize;
                    }
                });
            } catch (TimeoutException ignored) {
            }
        }
    }
}

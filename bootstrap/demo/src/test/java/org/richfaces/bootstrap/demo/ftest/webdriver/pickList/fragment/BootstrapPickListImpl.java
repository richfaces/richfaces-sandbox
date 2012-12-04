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
package org.richfaces.bootstrap.demo.ftest.webdriver.pickList.fragment;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import java.util.List;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class BootstrapPickListImpl implements BootstrapPickList {

    @Root
    WebElement root;
    //
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
    //
    @FindBy(css = "ol[id$=SourceList]")
    PickListListImpl sourceList;
    @FindBy(css = "ol[id$=TargetList]")
    PickListListImpl targetList;
    //
    WebDriver driver = GrapheneContext.getProxy();

    @Override
    public void addAll() {
        int size = sourceList.size() + targetList.size();
        this.addAllButton.click();
        waitForListSizesChange(0, size);
    }

    WebElement findElementInList(PickListListImpl list, String key) {
        return list.getElementForKey(key);
    }

    @Override
    public PickListList getSourceList() {
        return sourceList;
    }

    @Override
    public PickListList getTargetList() {
        return targetList;
    }

    @Override
    public boolean isVisible() {
        return Graphene.element(root).isVisible().apply(driver);
    }

    @Override
    public void removeAll() {
        int size = sourceList.size() + targetList.size();
        this.removeAllButton.click();
        waitForListSizesChange(size, 0);
    }

    @Override
    public PickListSelection selectFrom(From from, String... keys) {
        switch (from) {
            case sourceList:
                return selectFromSourceList(keys);
            case targetList:
                return selectFromTargetList(keys);
            default:
                throw new UnsupportedOperationException("Unknown switch " + from);
        }
    }

    @Override
    public PickListSelection selectFromSourceList(String... keys) {
        List<WebElement> selection = Lists.newArrayList();
        for (String key : keys) {
            selection.add(findElementInList(sourceList, key));
        }
        return new PickListSelectionImpl(selection, this, targetList);
    }

    @Override
    public PickListSelection selectFromTargetList(String... keys) {
        List<WebElement> selection = Lists.newArrayList();
        for (String key : keys) {
            selection.add(findElementInList(targetList, key));
        }
        return new PickListSelectionImpl(selection, this, sourceList);
    }

    /**
     * Waits for expected list sizes change.
     * @param expectedLeftListSize expected size of the left list
     * @param expectedRightListSize expected size of the right list
     * @throws TimeoutException if the lists size don't change as expected
     */
    void waitForListSizesChange(final int expectedLeftListSize, final int expectedRightListSize) {
        Graphene.waitGui().withMessage("Lists did not change as expected")
                .until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver driver) {
                boolean b1 = sourceList.size() == expectedLeftListSize;
                boolean b2 = targetList.size() == expectedRightListSize;
                return b1 & b2;
            }
        });
    }
}

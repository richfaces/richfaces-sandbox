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

import com.google.common.base.Preconditions;
import java.util.List;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class PickListSelectionImpl implements PickListSelection {

    private final List<WebElement> selection;
    private final BootstrapPickListImpl picklist;
    private final PickListListImpl to;
    //
    WebDriver driver = GrapheneContext.getProxy();

    private enum WithOffset {

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

    PickListSelectionImpl(List<WebElement> selection, BootstrapPickListImpl picklist, PickListListImpl to) {
        Preconditions.checkNotNull(picklist);
        Preconditions.checkNotNull(to);
        Preconditions.checkNotNull(selection);
        Preconditions.checkArgument(selection.size() > 0);
        this.selection = selection;
        this.picklist = picklist;
        this.to = to;
    }

    @Override
    public void down() {
        select();
        picklist.downButton.click();
    }

    private void dragAndDropActionWithPositiveOffset(Actions builder, WebElement source, WebElement target) {
        dragAndDropAction(WithOffset.positive, builder, source, target);
    }

    private void dragAndDropActionWithNegativeOffset(Actions builder, WebElement source, WebElement target) {
        dragAndDropAction(WithOffset.negative, builder, source, target);
    }

    /**
     * Workaround for Drag and Drop
     */
    private void dragAndDropAction(WithOffset offset, Actions builder, WebElement source, WebElement target) {
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

    @Override
    public void first() {
        select();
        picklist.firstButton.click();
    }

    @Override
    public void last() {
        select();
        picklist.lastButton.click();
    }

    private void select() {
        if (selection.size() > 1) {
            //multiple selection is not working
            Actions builder = new Actions(driver);
            boolean firstrun = true;
            for (WebElement keyElement : selection) {
                builder.click(keyElement);//also deselect previous selected elements
                if (firstrun) {
                    firstrun = false;
                    builder.keyDown(Keys.CONTROL);
                }
            }
            builder.keyUp(Keys.CONTROL);
            builder.build().perform();
        } else {
            selection.get(0).click();
        }
    }

    @Override
    public void transferByOne(With with) {
        int sourceListSizeFinalSize, targetListFinalSize;
        WebElement button;
        if (to.getRoot().getAttribute("id").contains("SourceList")) {
            sourceListSizeFinalSize = picklist.getSourceList().size() + selection.size();
            targetListFinalSize = picklist.getTargetList().size() - selection.size();
            button = picklist.removeButton;
        } else {
            sourceListSizeFinalSize = picklist.getSourceList().size() - selection.size();
            targetListFinalSize = picklist.getTargetList().size() + selection.size();
            button = picklist.addButton;
        }
        switch (with) {
            case button:
                for (WebElement keyElement : selection) {
                    keyElement.click();
                    button.click();
                }
                break;
            case dnd:
                Actions builder = new Actions(driver);
                for (WebElement keyElement : selection) {
                    dragAndDropActionWithPositiveOffset(builder, keyElement, to.getRoot());
                }
                builder.build().perform();
                break;
            default:
                throw new UnsupportedOperationException("Unknown switch " + with);

        }
        picklist.waitForListSizesChange(sourceListSizeFinalSize, targetListFinalSize);
    }

    @Override
    public void transferMultiple(With with) {
        int sourceListSizeFinalSize, targetListFinalSize;
        WebElement button;
        if (to.getRoot().getAttribute("id").contains("SourceList")) {
            sourceListSizeFinalSize = picklist.getSourceList().size() + selection.size();
            targetListFinalSize = picklist.getTargetList().size() - selection.size();
            button = picklist.removeButton;
        } else {
            sourceListSizeFinalSize = picklist.getSourceList().size() - selection.size();
            targetListFinalSize = picklist.getTargetList().size() + selection.size();
            button = picklist.addButton;
        }
        select();
        switch (with) {
            case button:
                button.click();
                break;
            case dnd:
                Actions builder = new Actions(driver);
                dragAndDropActionWithPositiveOffset(builder, selection.get(0), to.getRoot());
                builder.build().perform();
                break;
            default:
                throw new UnsupportedOperationException("Unknown switch " + with);
        }
        picklist.waitForListSizesChange(sourceListSizeFinalSize, targetListFinalSize);
    }

    @Override
    public void up() {
        select();
        picklist.upButton.click();
    }
}

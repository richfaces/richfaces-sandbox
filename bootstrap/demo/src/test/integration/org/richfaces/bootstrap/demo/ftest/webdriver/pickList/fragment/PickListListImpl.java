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
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class PickListListImpl implements PickListList {

    @Root
    WebElement root;
    //
    @FindBy(tagName = "li")
    List<WebElement> list;
    //
    WebDriver driver = GrapheneContext.getProxy();

    @Override
    public boolean contains(String key) {
        for (String s : this) {
            if (s.equals(key)) {
                return true;
            }
        }
        return false;
    }

    WebElement getElementForKey(String key) {
        int i = 0;
        for (String s : this) {
            if (s.equals(key)) {
                return list.get(i);
            }
            i++;
        }
        return null;
    }

    @Override
    public String getKeyAtIndex(int index) {
        Preconditions.checkArgument(list.size() > index, "The size of list is lesser than index!");
        return list.get(index).getAttribute("data-key");
    }

    WebElement getRoot() {
        return root;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean isVisible() {
        return Graphene.element(root).isVisible().apply(driver);
    }

    @Override
    public Iterator<String> iterator() {
        List<String> resultList = Lists.newArrayList();
        for (WebElement we : list) {
            resultList.add(we.getAttribute("data-key"));
        }
        return resultList.iterator();
    }

    @Override
    public int size() {
        return list.size();
    }
}

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

/**
 * Not fully completed fragment for b:pickList.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface BootstrapPickList {

    public enum From {

        sourceList,
        targetList
    }

    /**
     * Transfers all items from source list to target list by pressing 'addAll' button.
     */
    void addAll();

    PickListList getSourceList();

    PickListList getTargetList();

    boolean isVisible();

    /**
     * Transfers all items from target list to source list by pressing 'removeAll' button.
     */
    void removeAll();

    /**
     * Prepares a selection from one of list, items to be selected are specified by @keys
     * @param from source or target list
     * @param key key specifier of the item (attribute 'data-key')
     * @return selection object, which can be manipulated with
     */
    PickListSelection selectFrom(From from, String... keys);

    /**
     * Prepares a selection from source list, items to be selected are specified by @keys
     * @param keys
     * @return selection object, which can be manipulated with
     */
    PickListSelection selectFromSourceList(String... keys);

    /**
     * Prepares a selection from target list, items to be selected are specified by @keys
     * @param keys
     * @return selection object, which can be manipulated with
     */
    PickListSelection selectFromTargetList(String... keys);
}

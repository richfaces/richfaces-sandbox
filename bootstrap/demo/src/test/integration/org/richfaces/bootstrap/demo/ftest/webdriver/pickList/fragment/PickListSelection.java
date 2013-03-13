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
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public interface PickListSelection {

    public enum With {

        dnd,
        button
    }

    /**
     * Selects elements and clicks on 'down' button.
     */
    void down();

    /**
     * Selects elements and clicks on 'first' button.
     */
    void first();

    /**
     * Selects elements and clicks on 'last' button.
     */
    void last();

    /**
     * Selects and transfers the elements one by one. Then waits until lists update.
     * @param with dnd or by buttons
     */
    void transferByOne(With with);

    /**
     * Selects and transfers all elements by one action. Then waits until lists update.
     * @param with dnd or button
     */
    void transferMultiple(With with);

    /**
     * Selects elements and clicks on 'up' button.
     */
    void up();
}

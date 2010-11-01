/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.component;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.Iterators;

/**
 * @author Nick Belaevski
 * 
 */
public class SelectionImpl implements Selection, Serializable {

    private static final long serialVersionUID = 7738700662652136887L;

    private Set<Object> selectedKeys = new HashSet<Object>();

    public boolean addToSelection(Object rowKey) {
        return selectedKeys.add(rowKey);
    }

    public boolean isSelected(Object rowKey) {
        return selectedKeys.contains(rowKey);
    }

    public Iterator<Object> getSelectionIterator() {
        return Iterators.unmodifiableIterator(selectedKeys.iterator());
    }

    public boolean removeFromSelection(Object rowKey) {
        return selectedKeys.remove(rowKey);
    }

    public boolean isEmpty() {
        return selectedKeys.isEmpty();
    }
    
    public void clear() {
        selectedKeys = new HashSet<Object>();
    }
    
}

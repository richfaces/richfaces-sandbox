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
package org.richfaces.model;

import java.util.Iterator;

/**
 * @author Nick Belaevski
 * 
 */
public class SequenceRowKeyIterator<T> implements Iterator<Object> {

    private int counter = 0;
    
    private SequenceRowKey<Integer> baseKey;
    
    private Iterator<T> itr;

    private T element;

    private SequenceRowKey<Integer> elementKey;
    
    public SequenceRowKeyIterator(SequenceRowKey<Integer> baseKey, Iterator<T> itr) {
        super();
        this.baseKey = baseKey;
        this.itr = itr;
    }

    public boolean hasNext() {
        return itr.hasNext();
    }

    public Object next() {
        element = itr.next();
        elementKey = baseKey.append(counter++);
        
        return elementKey;
    }
    
    public T getElement() {
        return element;
    }

    public SequenceRowKey<Integer> getElementKey() {
        return elementKey;
    }
    
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
}

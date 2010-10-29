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

import java.io.Serializable;
import java.util.Arrays;

import com.google.common.collect.ObjectArrays;

/**
 * @author Nick Belaevski
 * 
 */
public class SequenceRowKey<T> implements Serializable {

    private static final long serialVersionUID = 5605581090240141910L;

    private T[] simpleKeys;

    public SequenceRowKey(T... keys) {
        super();
        this.simpleKeys = keys;
    }
    
    public T[] getSimpleKeys() {
        return simpleKeys;
    }
    
    public SequenceRowKey<T> append(T segment) {
        return new SequenceRowKey<T>(ObjectArrays.concat(simpleKeys, segment));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(simpleKeys);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SequenceRowKey<?> other = (SequenceRowKey<?>) obj;
        if (!Arrays.equals(simpleKeys, other.simpleKeys)) {
            return false;
        }
        return true;
    }
}

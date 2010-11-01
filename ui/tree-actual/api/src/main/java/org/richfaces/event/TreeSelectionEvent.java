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
package org.richfaces.event;

import java.util.Collection;
import java.util.HashSet;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

/**
 * @author Nick Belaevski
 * 
 */
public class TreeSelectionEvent extends FacesEvent {

    private static final long serialVersionUID = 6292604445872458007L;

    private Collection<Object> addedKeys = new HashSet<Object>();

    private Collection<Object> removedKeys = new HashSet<Object>();

    public TreeSelectionEvent(UIComponent component, Collection<Object> addedKeys, Collection<Object> removedKeys) {
        super(component);
        
        this.addedKeys = addedKeys;
        this.removedKeys = removedKeys;
    }

    @Override
    public boolean isAppropriateListener(FacesListener listener) {
        return listener instanceof TreeSelectionListener;
    }

    @Override
    public void processListener(FacesListener listener) {
        ((TreeSelectionListener) listener).processSelection(this);
    }

    public Collection<Object> getAddedKeys() {
        return addedKeys;
    }
    
    public Collection<Object> getRemovedKeys() {
        return removedKeys;
    }
}

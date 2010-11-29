/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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

import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.event.BehaviorEvent;
import javax.faces.event.FacesListener;

/**
 * @author abelevich
 *
 */
public class DropEvent extends BehaviorEvent {

    private static final long serialVersionUID = 3717071628237886288L;
   
    private Object dropValue;
    
    private Object dragValue;
    
    private Set<String> acceptedTypes;
    
    private UIComponent dragSource;
    
    
    public DropEvent(UIComponent component, Behavior behavior) {
        super(component, behavior);
    }
        
    public Set<String> getAcceptedTypes() {
        return acceptedTypes;
    }

    public void setAcceptedTypes(Set<String> acceptedTypes) {
        this.acceptedTypes = acceptedTypes;
    }
      
    public Object getDropValue() {
        return dropValue;
    }

    public void setDropValue(Object dropValue) {
        this.dropValue = dropValue;
    }

    public Object getDragValue() {
        return dragValue;
    }

    public void setDragValue(Object dragValue) {
        this.dragValue = dragValue;
    }

    public UIComponent getDragSource() {
        return dragSource;
    }

    public void setDragSource(UIComponent dragSource) {
        this.dragSource = dragSource;
    }

    @Override
    public boolean isAppropriateListener(FacesListener listener) {
        return (listener instanceof DropListener);
    }
    
    @Override
    public void processListener(FacesListener listener) {
        ((DropListener) listener).processDrop(this);
    }
 
}
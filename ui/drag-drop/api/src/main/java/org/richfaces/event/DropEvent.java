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

import javax.faces.component.UIComponent;
import javax.faces.event.BehaviorEvent;
import javax.faces.event.FacesListener;

import org.richfaces.component.behavior.ClientDragBehavior;
import org.richfaces.component.behavior.ClientDropBehavior;

/**
 * @author abelevich
 *
 */
public class DropEvent extends BehaviorEvent {

    private static final long serialVersionUID = 3717071628237886288L;
    
    private ClientDragBehavior dragSource;
    
    private UIComponent dragComponent;
    
    public DropEvent(UIComponent component, ClientDropBehavior behavior) {
        super(component, behavior);
    }
    
    public ClientDragBehavior getDragSource() {
        return dragSource;
    }


    public void setDragSource(ClientDragBehavior dragSource) {
        this.dragSource = dragSource;
    }

    public UIComponent getDragComponent() {
        return dragComponent;
    }

    public void setDragComponent(UIComponent dragComponent) {
        this.dragComponent = dragComponent;
    }

    public ClientDropBehavior getDropSource() {
        return (ClientDropBehavior)getBehavior();
    }
    
    public UIComponent getDropComponent() {
        return getComponent();
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
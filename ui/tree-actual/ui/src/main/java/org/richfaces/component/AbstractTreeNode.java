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

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.event.TreeToggleEvent;

/**
 * @author Nick Belaevski
 * 
 */
@JsfComponent(
    type = AbstractTreeNode.COMPONENT_TYPE,
    family = AbstractTreeNode.COMPONENT_FAMILY, 
    tag = @Tag(name = "treeNode"),
    renderer = @JsfRenderer(type = "org.richfaces.TreeNodeRenderer")
)
public abstract class AbstractTreeNode extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.richfaces.TreeNode";
    
    public static final String COMPONENT_FAMILY = "org.richfaces.TreeNode";

    public AbstractTreeNode() {
        setRendererType("org.richfaces.TreeNodeRenderer");
    }
    
    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }
    
    @Attribute(defaultValue = "findTreeComponent().isImmediate()")
    public abstract boolean isImmediate();
    
    public abstract String getType();
    
    protected AbstractTree findTreeComponent() {
        UIComponent c = this;
        while (c != null && !(c instanceof AbstractTree)) {
            c = c.getParent();
        }
        
        return (AbstractTree) c;
    }
    
    @Override
    public void queueEvent(FacesEvent event) {
        if (this.equals(event.getComponent())) {
            if (event instanceof TreeToggleEvent) {
                PhaseId targetPhase = isImmediate() ? PhaseId.APPLY_REQUEST_VALUES : PhaseId.PROCESS_VALIDATIONS;
                event.setPhaseId(targetPhase);
            }
        }
        
        super.queueEvent(event);
    }
    
    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);
        
        if (event instanceof TreeToggleEvent) {
            TreeToggleEvent toggleEvent = (TreeToggleEvent) event;
            new TreeToggleEvent(findTreeComponent(), toggleEvent.isExpanded()).queue();
        }
    }
}

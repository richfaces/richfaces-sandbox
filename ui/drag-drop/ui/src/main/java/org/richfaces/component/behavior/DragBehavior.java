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


package org.richfaces.component.behavior;

import org.ajax4jsf.component.behavior.ClientBehavior;
import org.richfaces.cdk.annotations.JsfBehavior;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;

/**
 * @author abelevich
 *
 */

@JsfBehavior(
    id = DragBehavior.BEHAVIOR_ID, tag = @Tag(name = "dragBehavior", handler = "org.richfaces.view.facelets.html.CustomBehaviorHandler", type = TagType.Facelets)
)
public class DragBehavior extends ClientBehavior implements ClientDragBehavior {
    
    public static final String BEHAVIOR_ID = "org.richfaces.component.behavior.DragBehavior";
    
    enum PropertyKeys {
        type, indicator, dragValue;
    }
    
    public void setDragValue(Object dragValue) {
        getStateHelper().put(PropertyKeys.dragValue, dragValue);
    }
    
    public Object getDragValue() {
        return getStateHelper().get(PropertyKeys.dragValue);
    }
    
    public void setIndicator(String indicator) {
        getStateHelper().put(PropertyKeys.indicator, indicator);
    }
    
    public String getIndicator() {
        return (String)getStateHelper().get(PropertyKeys.indicator);
    }
    
    public void setType(String type) {
        getStateHelper().put(PropertyKeys.type, type);
    }
    
    public String getType() {
        return (String)getStateHelper().eval(PropertyKeys.type);
    }
    
    @Override
    public void setLiteralAttribute(String name, Object value) {
        if(compare(PropertyKeys.type, name)) {
            setType((String)value);
        } else if(compare(PropertyKeys.indicator, name)){
            setIndicator((String)value);
        } else if(compare(PropertyKeys.dragValue, name)) {
            setDragValue(value);
        }
    }
    
    public String getEvent() {
        return "mousedown";
    }
    
    @Override
    public String getRendererType() {
        return BEHAVIOR_ID;
    }
}

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

import java.util.Set;

import org.ajax4jsf.component.behavior.ClientBehavior;
import org.richfaces.cdk.annotations.JsfBehavior;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.event.DropListener;
import org.richfaces.renderkit.util.CoreAjaxRendererUtils;

/**
 * @author abelevich
 *
 */

@JsfBehavior(
    id = DropBehavior.BEHAVIOR_ID, tag = @Tag(name = "dropBehavior", handler = "org.richfaces.view.facelets.DropBehaviorHandler", type = TagType.Facelets)
)
public class DropBehavior extends ClientBehavior implements ClientDropBehavior {
    
    public static final String BEHAVIOR_ID = "org.richfaces.component.behavior.DropBehavior";

    enum PropertyKeys {
        acceptType, dropValue
    }
    
    public void setDropValue(Object dropValue) {
        getStateHelper().put(PropertyKeys.dropValue, dropValue);
    }
    
    public Object getDropValue() {
        return getStateHelper().get(PropertyKeys.dropValue);
    }
    
    public void setAcceptType(Set<String> acceptType) {
        getStateHelper().put(PropertyKeys.acceptType, acceptType);
    }
    
    public Set<String> getAcceptType() {
        return (Set<String>)getStateHelper().eval(PropertyKeys.acceptType);
    }
    
    public void addDropListener(DropListener listener) {
        addBehaviorListener(listener);
    }

    public void removeDropListener(DropListener listener) {
        removeBehaviorListener(listener);
    }

    @Override
    public void setLiteralAttribute(String name, Object value) {
        if(compare(PropertyKeys.acceptType, name)) {
            setAcceptType(CoreAjaxRendererUtils.asSimpleSet(value));
        } else if(compare(PropertyKeys.dropValue, name)) {
            setDropValue(value);
        }
    }

    @Override
    public String getRendererType() {
        return BEHAVIOR_ID;
    }

}

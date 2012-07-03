/**
 * JBoss, Home of Professional Open Source
 * Copyright , Red Hat, Inc. and individual contributors
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
package org.richfaces.bootstrap.component;

import javax.faces.component.UIComponent;
import org.richfaces.bootstrap.RenderMenuGroupCapable;
import org.richfaces.bootstrap.renderkit.GroupRendererBase;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;

/**
 * Base class for the menuGroup component.
 *
 * @author <a href="http://www.pauldijou.fr">Paul Dijou</a>
 */
@JsfComponent(
        type = AbstractMenuGroup.COMPONENT_TYPE,
        family = AbstractMenuGroup.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = GroupRendererBase.RENDERER_TYPE),
        tag = @Tag(name="menuGroup"))
public abstract class AbstractMenuGroup extends AbstractSemanticComponent<RenderMenuGroupCapable> {
    public static final String COMPONENT_FAMILY = "org.richfaces.bootstrap.MenuGroup";
    public static final String COMPONENT_TYPE = "org.richfaces.bootstrap.MenuGroup";
    public static final String ACTIVE_ATTRIBUTE_NAME = "active";

    @Attribute
    public abstract String getLabel();
    
    @Attribute
    public abstract boolean isActive();
    
    public int getLevel() {
        return getLevel(null);
    }
    
    public int getLevel(String stopParentFamily) {
        UIComponent parent = this.getParent();
        int level = 0;
        
        while(parent != null) {
            if(parent instanceof AbstractMenuGroup) {
                ++level;
            }
            
            if(stopParentFamily != null && stopParentFamily.equals(parent.getFamily())) {
                parent = null;
            }
            else {
                parent = parent.getParent();
            }
        }
        
        return level;
    }
    
    @Override
    public Class<RenderMenuGroupCapable> getRendererCapability() {
        return RenderMenuGroupCapable.class;
    }
    
    @Override
    public String getRendererType(RenderMenuGroupCapable container) {
        return container.getMenuGroupRendererType();
    }
}

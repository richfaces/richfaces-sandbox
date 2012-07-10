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
package org.richfaces.bootstrap.renderkit;

import java.util.List;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import org.richfaces.bootstrap.component.AbstractMenuGroup;
import org.richfaces.bootstrap.component.AbstractTabPane;
import org.richfaces.bootstrap.component.AbstractTabbable;
import org.richfaces.renderkit.RendererBase;

/**
 * Base class for the tabbable renderer
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@ResourceDependencies({
        @ResourceDependency(library = "org.richfaces", name = "ajax.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "bootstrap-css.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "bootstrap-js.reslib")})
public abstract class TabbableRendererBase extends RendererBase {
    public static final String RENDERER_TYPE = "org.richfaces.bootstrap.TabbableRenderer";
    
    private String indexSeparator;
    
    public void setDefaultChild(UIComponent component) {
        AbstractTabbable tabbable = (AbstractTabbable) component;
        
        // First, check is there is no tabPane with "default" attribute to "true"
        boolean hasDefaultChild = hasDefaultChild(tabbable.getChildren(), false);
        
        // Next, if not, let's use names
        if(!hasDefaultChild && tabbable.getActiveName() != null && !"".equals(tabbable.getActiveName())) {
            hasDefaultChild = setDefaultChildByName(tabbable.getChildren(), tabbable.getActiveName(), false);
        }
        
        // Finally, if still nothing, let's use index
        if(!hasDefaultChild) {
            indexSeparator = tabbable.getIndexSeparator();
            setDefaultChildByIndex(tabbable.getChildren(), tabbable.getActiveIndex(), "", false);
        }
    }
    
    private boolean hasDefaultChild(List<UIComponent> children, boolean hasDefaultChildSoFar) {
        for(UIComponent child : children) {
            if(child instanceof AbstractTabPane) {
                AbstractTabPane tabPane = (AbstractTabPane) child;
                
                // If we already find a child so far, we need to disable any other tabPane#isDefault()
                if(hasDefaultChildSoFar) {
                    tabPane.getAttributes().put(AbstractTabPane.DEFAULT_ATTRIBUTE_NAME, false);
                } 
                // If not, we can take the value of the current tabPane
                else {
                    hasDefaultChildSoFar = tabPane.isDefault();
                }
                
            } else if(child instanceof AbstractMenuGroup) {
                boolean hasDefaultChildSoFarOld = hasDefaultChildSoFar;
                hasDefaultChildSoFar = hasDefaultChild(child.getChildren(), hasDefaultChildSoFar);
                
                // If the value has changed, it means this menuGroup contains the current tabPane
                // so it's the active one!
                if(hasDefaultChildSoFarOld != hasDefaultChildSoFar) {
                    AbstractMenuGroup menuGroup = (AbstractMenuGroup) child;
                    menuGroup.getAttributes().put(AbstractMenuGroup.ACTIVE_ATTRIBUTE_NAME, true);
                }
            }
        }
        
        return hasDefaultChildSoFar;
    }
    
    private boolean setDefaultChildByName(List<UIComponent> children, String tabName, boolean hasDefaultChildSoFar) {
        for(UIComponent child : children) {
            if(child instanceof AbstractTabPane) {
                AbstractTabPane tabPane = (AbstractTabPane) child;
                
                // If we didn't found a corresponding name so far and the current one match,
                // then it's the default one!
                if(!hasDefaultChildSoFar && tabName.equals(tabPane.getName())) {
                    tabPane.getAttributes().put(AbstractTabPane.DEFAULT_ATTRIBUTE_NAME, true);
                    hasDefaultChildSoFar = true;
                }
                
            } else if(child instanceof AbstractMenuGroup) {
                boolean hasDefaultChildSoFarOld = hasDefaultChildSoFar;
                hasDefaultChildSoFar = setDefaultChildByName(child.getChildren(), tabName, hasDefaultChildSoFar);
                
                // If the value has changed, it means this menuGroup contains the current tabPane
                // so it's the active one!
                if(hasDefaultChildSoFarOld != hasDefaultChildSoFar) {
                    AbstractMenuGroup menuGroup = (AbstractMenuGroup) child;
                    menuGroup.getAttributes().put(AbstractMenuGroup.ACTIVE_ATTRIBUTE_NAME, true);
                }
            }
        }
        
        return hasDefaultChildSoFar;
    }
    
    private boolean setDefaultChildByIndex(List<UIComponent> children, String index, String currentIndex, boolean hasDefaultChildSoFar) {
        int intLocalIndex = Integer.valueOf(AbstractTabbable.ACTIVE_INDEX_DEFAULT);
        
        for(UIComponent child : children) {
            String localIndex;
            
            if(currentIndex.length() > 0) {
                localIndex = currentIndex + indexSeparator + intLocalIndex;
            } else {
                localIndex = ""+intLocalIndex;
            }
            
            if(child instanceof AbstractTabPane && localIndex.equals(index)) {
                AbstractTabPane tabPane = (AbstractTabPane) child;
                tabPane.getAttributes().put(AbstractTabPane.DEFAULT_ATTRIBUTE_NAME, true);
                hasDefaultChildSoFar = true;
                
            } else if(child instanceof AbstractMenuGroup) {
                boolean hasDefaultChildSoFarOld = hasDefaultChildSoFar;
                hasDefaultChildSoFar = setDefaultChildByIndex(child.getChildren(), index, localIndex, hasDefaultChildSoFar);
                
                // If the value has changed, it means this menuGroup contains the current tabPane
                // so it's the active one!
                if(hasDefaultChildSoFarOld != hasDefaultChildSoFar) {
                    AbstractMenuGroup menuGroup = (AbstractMenuGroup) child;
                    menuGroup.getAttributes().put(AbstractMenuGroup.ACTIVE_ATTRIBUTE_NAME, true);
                }
            }
            
            ++intLocalIndex;
        }
        
        return hasDefaultChildSoFar;
    }
}

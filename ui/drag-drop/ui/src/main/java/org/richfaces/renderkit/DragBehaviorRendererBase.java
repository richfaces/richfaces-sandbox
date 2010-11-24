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

package org.richfaces.renderkit;

import java.util.HashMap;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;
import javax.faces.render.ClientBehaviorRenderer;
import javax.faces.render.FacesBehaviorRenderer;
import javax.faces.render.RenderKitFactory;

import org.ajax4jsf.javascript.JSFunction;
import org.richfaces.component.behavior.DragBehavior;
import org.richfaces.renderkit.util.RendererUtils;

/**
 * @author abelevich
 *
 */


@FacesBehaviorRenderer(rendererType = DragBehavior.BEHAVIOR_ID, renderKitId = RenderKitFactory.HTML_BASIC_RENDER_KIT)
        
@ResourceDependencies({
    @ResourceDependency(name = "jquery.js"),
    @ResourceDependency(name = "richfaces.js"),
    @ResourceDependency(library = "org.richfaces", name = "jquery-ui-core.js"),
    @ResourceDependency(library = "org.richfaces", name = "jquery-dnd.js"),
    @ResourceDependency(library = "org.richfaces", name = "dnd-draggable.js"),
    @ResourceDependency(library = "org.richfaces", name = "dnd-manager.js")
})
public class DragBehaviorRendererBase extends ClientBehaviorRenderer {
    
    private static final RendererUtils UTILS = RendererUtils.getInstance();
    
    @Override
    public String getScript(ClientBehaviorContext behaviorContext, ClientBehavior behavior) {
        UIComponent parent = behaviorContext.getComponent();
        JSFunction function = new JSFunction("RichFaces.ui.DnDManager.draggable");
        function.addParameter(parent.getClientId(behaviorContext.getFacesContext()));
        function.addParameter(getOptions(behaviorContext, behavior));
        return function.toString();
    }
    
    public Map<String, Object> getOptions(ClientBehaviorContext clientBehaviorContext, ClientBehavior behavior) {
        Map<String, Object> options = new HashMap<String, Object>();
        
        if(behavior instanceof DragBehavior) {
            DragBehavior dragBehavior = (DragBehavior)behavior;
            options.put("indicator", getDragIndicatorClientId(clientBehaviorContext, dragBehavior));
            options.put("type", dragBehavior.getType());
        }
        
        return options;
    }
    
    public String getDragIndicatorClientId(ClientBehaviorContext clientBehaviorContext, DragBehavior dragBehavior) {
        String indicatorId = dragBehavior.getIndicator();
        
        if(indicatorId != null) {
            FacesContext facesContext = clientBehaviorContext.getFacesContext();
            UIComponent clientBehaviorHolder = clientBehaviorContext.getComponent();
            UIComponent indicator = getUtils().findComponentFor(facesContext, clientBehaviorHolder, indicatorId);
            
            if(indicator != null) {
                indicatorId = indicator.getClientId(facesContext);
            }
            
        }
        
        return indicatorId;
    }
    
    public RendererUtils getUtils() {
        return UTILS;
    }

}

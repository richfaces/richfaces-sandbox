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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesBehaviorRenderer;
import javax.faces.render.RenderKitFactory;

import org.richfaces.component.behavior.ClientDragBehavior;
import org.richfaces.component.behavior.ClientDropBehavior;
import org.richfaces.component.behavior.DropBehavior;
import org.richfaces.event.DropEvent;


/**
 * @author abelevich
 *
 */


@FacesBehaviorRenderer(rendererType = DropBehavior.BEHAVIOR_ID, renderKitId = RenderKitFactory.HTML_BASIC_RENDER_KIT)

@ResourceDependencies({
    @ResourceDependency(name = "jquery.js"),
    @ResourceDependency(name = "richfaces.js"),
    @ResourceDependency(name = "richfaces.js"),
    @ResourceDependency(library = "org.richfaces", name = "jquery-ui-core.js"),
    @ResourceDependency(library = "org.richfaces", name = "jquery-dnd.js"),
    @ResourceDependency(library = "org.richfaces", name = "dnd-droppable.js"),
    @ResourceDependency(library = "org.richfaces", name = "dnd-manager.js")
})
public class DropBehaviorRendererBase extends DnDBehaviorRenderBase {
    
    @Override
    public void decode(FacesContext facesContext, UIComponent component, ClientBehavior behavior) {
        if (null == facesContext || null == component || behavior == null) {
            throw new NullPointerException();
        }

        Map<String, String> requestParamMap = facesContext.getExternalContext().getRequestParameterMap();
        String dragSource = (String) requestParamMap.get("dragSource");
        facesContext.getViewRoot().invokeOnComponent(facesContext, dragSource, new DropBehaviorContextCallBack(component, (ClientDropBehavior)behavior));
    }
    
    @Override
    protected String getScriptName() {
        return "RichFaces.ui.DnDManager.droppable";
    }
    
    public Map<String, Object> getOptions(ClientBehaviorContext behaviorContext, ClientBehavior behavior) {
        Map<String, Object> options = new HashMap<String, Object>();
        
        if(behavior instanceof ClientDropBehavior) {
            ClientDropBehavior dropBehavior = (ClientDropBehavior)behavior;
            options.put("acceptType", dropBehavior.getAcceptType());
        }
        
        return options;
    }
    
    private final class DropBehaviorContextCallBack implements ContextCallback {
        
        private ClientDropBehavior dropBehavior;
        
        private UIComponent dropSource;
        
        public DropBehaviorContextCallBack(UIComponent dropSource, ClientDropBehavior dropBehavior) {
            this.dropSource = dropSource;
            this.dropBehavior = dropBehavior;
        }
        
        public void invokeContextCallback(FacesContext context, UIComponent target) {
            ClientDragBehavior dragBehavior = getDragBehavior(target, "mouseover");
            
            if(dragBehavior != null) {
                DropEvent dropEvent = new DropEvent(dropSource, dropBehavior);
                dropEvent.setDragSource(target);
                dropEvent.setDragValue(dragBehavior.getDragValue());
                dropEvent.setDropValue(dropBehavior.getDropValue());
                dropEvent.setAcceptType(dropBehavior.getAcceptType());
                dropEvent.queue();
            } else {
                //TODO: log
            }
        }
        
        private ClientDragBehavior getDragBehavior(UIComponent parent, String event) {
            if(parent instanceof ClientBehaviorHolder) {
                Map<String, List<ClientBehavior>> behaviorsMap = ((ClientBehaviorHolder)parent).getClientBehaviors();
                Set<Map.Entry<String, List<ClientBehavior>>> entries = behaviorsMap.entrySet();
                
                for(Entry<String, List<ClientBehavior>> entry: entries) {
                    if(event.equals(entry.getKey())){
                        List<ClientBehavior> behaviors = entry.getValue();
                        for(ClientBehavior behavior: behaviors) {
                            if(behavior instanceof ClientDragBehavior) {
                                return (ClientDragBehavior)behavior;
                            }
                        }
                    }
                }
                
            }
            return null;
        }
        
    }

}

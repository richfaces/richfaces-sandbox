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

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.render.ClientBehaviorRenderer;

import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.javascript.JSReference;
import org.richfaces.renderkit.util.RendererUtils;

/**
 * @author abelevich
 *
 */
public abstract class DnDBehaviorRenderBase extends ClientBehaviorRenderer {
    
    private static final RendererUtils UTILS = RendererUtils.getInstance();
    
    
    protected abstract Map<String, Object> getOptions(ClientBehaviorContext behaviorContext, ClientBehavior clientBehavior);   
    
    protected abstract String getScriptName();
    
    public String getScript(ClientBehaviorContext behaviorContext, ClientBehavior behavior) {
        UIComponent parent = behaviorContext.getComponent();
        String scriptName = getScriptName();
        String script = null;
        if(!"".equals(scriptName)) {
            JSFunction function = new JSFunction(scriptName);
            function.addParameter(JSReference.EVENT);
            function.addParameter(parent.getClientId(behaviorContext.getFacesContext()));
            function.addParameter(getOptions(behaviorContext, behavior));
            script = function.toScript();
        }
        return script;
    }

    public RendererUtils getUtils() {
        return UTILS;
    }

}

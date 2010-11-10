/*
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

package org.richfaces.renderkit.html;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.javascript.JSFunction;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractNotifyStack;
import org.richfaces.renderkit.AjaxComponentRendererBase;
import org.richfaces.renderkit.HtmlConstants;
import org.richfaces.renderkit.util.RendererUtils;

@JsfRenderer(family = AbstractNotifyStack.COMPONENT_FAMILY, type = NotifyStackRenderer.RENDERER_TYPE)
@ResourceDependencies({
    @ResourceDependency(name = "jquery.js", target = "head"),
    @ResourceDependency(name = "richfaces.js", target = "head"),
    @ResourceDependency(name = "richfaces.notify.js", target = "head")
})
public class NotifyStackRenderer extends AjaxComponentRendererBase {

    public static final String RENDERER_TYPE = "org.richfaces.NotifyStackRenderer";

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (!(component instanceof AbstractNotifyStack)) {
            return;
        }
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement(HtmlConstants.SCRIPT_ELEM, null);
        writer.writeText(new JSFunction("RichFaces.NotifyStack.register",
            RendererUtils.getInstance().clientId(context, component),
            getOptions((AbstractNotifyStack) component)
        ), null);
        writer.endElement(HtmlConstants.SCRIPT_ELEM);
    }

    protected Map<String, Object> getOptions(AbstractNotifyStack stack) throws IOException {
        /**
         * Include only attributes that are actually set.
         */
        Map<String, Object> options = new HashMap<String, Object>();
        addOptionIfSet("dir1", stack.getStackDir1(), options);
        addOptionIfSet("dir2", stack.getStackDir2(), options);
        addOptionIfSet("push", stack.getPush(), options);
        return options;
    }

    protected void addOptionIfSet(String optionName, Object value, Map<String, Object> options) {
        if (value != null && !"".equals(value)) {
            options.put(optionName, value);
        }
    }
}

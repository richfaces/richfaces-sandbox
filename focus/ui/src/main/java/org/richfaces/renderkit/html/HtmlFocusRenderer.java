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

import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.javascript.JSFunctionDefinition;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractFocus;
import org.richfaces.component.AbstractFocusModifier;
import org.richfaces.renderkit.HtmlConstants;
import org.richfaces.renderkit.RendererBase;

import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

@JsfRenderer(family = AbstractFocus.COMPONENT_FAMILY, type = HtmlFocusRenderer.RENDERER_TYPE)
@ResourceDependencies({@ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(name = "jquery.js", target = "head"),
        @ResourceDependency(name = "richfaces.js", target = "head"),
        @ResourceDependency(name = "richfaces-base-component.js", target = "head"),
        @ResourceDependency(name = "richfaces.focus.js", target = "head")})
public class HtmlFocusRenderer extends RendererBase {
// ------------------------------ FIELDS ------------------------------

    public static final String RENDERER_TYPE = "org.richfaces.HtmlFocusRenderer";

    private void checkValidity(FacesContext context, AbstractFocus component) {
        String clientId = getUtils().clientId(context, component);
        String name = component.getName();
        String timing = component.getTiming();
        String _for = component.getFor();
        String targetClientId = component.getTargetClientId();
        if ((name == null || "".equals(name.trim())) && "onJScall".equals(timing)) {
            throw new FacesException(
                    "The name attribute of the focus component (id='" + clientId + "') must be specified when timing attribute equals to 'onJScall'");
        }
        if (name != null && !"".equals(name.trim()) && !"onJScall".equals(timing)) {
            throw new FacesException(
                    "The timing attribute of the focus component (id='" + clientId + "') must be set to 'onJScall' when name attribute is specified");
        }
        if ((_for == null || "".equals(_for)) && (targetClientId == null || "".equals(targetClientId)) && !(component.getParent() instanceof UIInput)
                && getUtils().getNestingForm(context, component) == null) {
            throw new FacesException("Focus component must have either one of 'for' or 'targetClientId' attributes specified or be nested within UIForm or UIInput component");
        }
    }

    @Override
    protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component)
            throws IOException {
        if (!(component instanceof AbstractFocus)) {
            return;
        }
        AbstractFocus uiFocus = (AbstractFocus) component;
        final String clientId = getUtils().clientId(context, component);
        checkValidity(context, uiFocus);
        Integer priority = uiFocus.getPriority();
        String targetClientId = uiFocus.getTargetClientId();
        if (targetClientId == null || "".equals(targetClientId)) {
            String targetComponentId = uiFocus.getTargetComponentId(context);
            String suffix = uiFocus.getSuffix();
            if (targetComponentId == null || "".equals(targetComponentId)) {
                return;
            }
            UIComponent forcomp = getUtils().findComponentFor(component, targetComponentId);
            if (forcomp == null) {
                throw new FacesException("No component with id=" + targetComponentId + " found!");
            }
            targetClientId = forcomp.getClientId(context);
            AbstractFocusModifier modifier = AbstractFocus.findModifier(forcomp);
            if (modifier != null) {
                final String modifiedTargetClientId = modifier.getTargetClientId();
                if (modifiedTargetClientId != null && !modifiedTargetClientId.equals("")) {
                    targetClientId = modifiedTargetClientId;
                } else {
                    suffix = modifier.getSuffix();
                }
            }
            if (priority == null) {
                priority = uiFocus.calculatePriority(forcomp);
            }
            if (suffix != null && !"".equals(suffix)) {
                targetClientId += suffix;
            }
        }
        if (targetClientId == null || targetClientId.equals("")) {
            return;
        }
        if (priority == null) {
            priority = AbstractFocus.DEFAULT_PRIORITY;
        }
        writer.startElement(HtmlConstants.SCRIPT_ELEM, null);
        writer.writeAttribute(HtmlConstants.TYPE_ATTR, "text/javascript", "type");
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, clientId, HtmlConstants.ID_ATTRIBUTE);
        if (AbstractFocus.TIMING_ON_LOAD.equals(uiFocus.getTiming())) {
            writer.write(new JSFunction("RichFaces.ui.Focus.setFocus", targetClientId, priority).toScript());
            writer.write(";");
        } else {
            writer.write(new JSFunction("RichFaces.ui.Focus.setFocus", targetClientId, priority, clientId,
                    uiFocus.getTiming()).toScript());
            writer.write(";");
        }
        if (uiFocus.getName() != null && !uiFocus.getName().trim().equals("")) {
            final JSFunctionDefinition definition = new JSFunctionDefinition().addToBody(new JSFunction("RichFaces.ui.Focus.focusStored", clientId));
            definition.setName(uiFocus.getName());
            writer.write(definition.toScript());
            writer.write(";");
        }
        writer.endElement(HtmlConstants.SCRIPT_ELEM);
    }

    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractFocus.class;
    }
}

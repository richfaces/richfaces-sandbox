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
package org.richfaces.sandbox.renderkit.html;

import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.ajax4jsf.javascript.JSObject;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.sandbox.component.AbstractAccordion;
import org.richfaces.component.util.HtmlUtil;

/**
 * @author akolonitsky
 */
@ResourceDependencies({ @ResourceDependency(library = "org.richfaces", name = "ajax.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "togglePanel.js"),
        @ResourceDependency(library = "org.richfaces", name = "accordion.js"),
        @ResourceDependency(library = "org.richfaces", name = "icons.ecss"),
        @ResourceDependency(library = "org.richfaces", name = "accordion.ecss") })
@JsfRenderer(type = "org.richfaces.sandbox.AccordionRenderer", family = AbstractAccordion.COMPONENT_FAMILY)
public class AccordionRenderer extends TogglePanelRenderer {
    @Override
    protected String getStyle(UIComponent component) {
        return HtmlUtil.concatStyles(attributeAsStyle(component, "height"), attributeAsStyle(component, "width"),
            super.getStyle(component));
    }

    @Override
    protected String getStyleClass(UIComponent component) {
        return HtmlUtil.concatClasses("rf-ac", attributeAsString(component, "styleClass"));
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractAccordion.class;
    }

    @Override
    protected JSObject getScriptObject(FacesContext context, UIComponent component) {
        return new JSObject("RichFaces.ui.Accordion", component.getClientId(context),
            getScriptObjectOptions(context, component));
    }

    @Override
    protected Map<String, Object> getScriptObjectOptions(FacesContext context, UIComponent component) {
        Map<String, Object> options = super.getScriptObjectOptions(context, component);
        options.put("isKeepHeight", attributeAsString(component, "height").length() > 0);

        return options;
    }
}

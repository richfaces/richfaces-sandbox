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

import static org.richfaces.renderkit.RenderKitUtils.renderPassThroughAttributes;
import static org.richfaces.ui.common.HtmlConstants.CLASS_ATTRIBUTE;
import static org.richfaces.ui.common.HtmlConstants.DIV_ELEM;
import static org.richfaces.ui.common.HtmlConstants.ID_ATTRIBUTE;
import static org.richfaces.ui.common.HtmlConstants.STYLE_ATTRIBUTE;

import java.io.IOException;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.javascript.JSObject;
import org.richfaces.renderkit.RenderKitUtils;
import org.richfaces.sandbox.component.AbstractAccordionItem;
import org.richfaces.sandbox.component.AbstractTogglePanelTitledItem;

/**
 *
 * @author akolonitsky
 * @since 2010-08-05
 */
@ResourceDependencies({ @ResourceDependency(library = "org.richfaces", name = "ajax.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
        @ResourceDependency(name = "richfaces-event.js"),
        @ResourceDependency(library = "org.richfaces", name = "togglePanelItem.js"),
        @ResourceDependency(library = "org.richfaces", name = "accordionItem.js") })
@JsfRenderer(type = "org.richfaces.sandbox.AccordionItemRenderer", family = AbstractAccordionItem.COMPONENT_FAMILY)
public class AccordionItemRenderer extends TogglePanelItemRenderer {
    private static final RenderKitUtils.Attributes HEADER_ATTRIBUTES = RenderKitUtils.attributes()
        .generic("style", "headerStyle").generic("onclick", "onheaderclick", "headerclick")
        .generic("ondblclick", "onheaderdblclick", "headerdblclick")
        .generic("onmousedown", "onheadermousedown", "headermousedown")
        .generic("onmousemove", "onheadermousemove", "headermousemove")
        .generic("onmouseup", "onheadermouseup", "headermouseup");
    private final TableIconsRendererHelper<AbstractAccordionItem> headerRenderer = new AccordionItemHeaderRenderer();

    public AccordionItemRenderer() {
        super(false);
    }

    @Override
    protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        doEncodeItemBegin(writer, context, component);
    }

    @Override
    protected void doEncodeItemBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {

        super.doEncodeItemBegin(writer, context, component);
        encodeHeader(context, (AbstractAccordionItem) component);
        encodeContentBegin(context, component);
    }

    @Override
    protected String getStyleClass(UIComponent component) {
        return concatClasses("rf-ac-itm", attributeAsString(component, "styleClass"));
    }

    @Override
    protected void doEncodeChildren(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {

        AbstractAccordionItem item = (AbstractAccordionItem) component;

        if (!item.isDisabled()) {
            super.doEncodeChildren(writer, context, item);
        }
    }

    @Override
    protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        doEncodeItemEnd(writer, context, component);
    }

    @Override
    protected void doEncodeItemEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {

        encodeContentEnd(writer, component);

        super.doEncodeItemEnd(writer, context, component);
    }

    private void encodeContentBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement(DIV_ELEM, component);
        writer.writeAttribute(CLASS_ATTRIBUTE, concatClasses("rf-ac-itm-cnt", attributeAsString(component, "contentClass")),
            null);
        writer.writeAttribute("id", component.getClientId(context) + ":content", null);

        AbstractTogglePanelTitledItem item = (AbstractTogglePanelTitledItem) component;
        if (!item.isActive() || item.isDisabled()) {
            writer.writeAttribute(STYLE_ATTRIBUTE, "display: none", null);
        }
    }

    private void encodeContentEnd(ResponseWriter writer, UIComponent component) throws IOException {
        writer.endElement(DIV_ELEM);
    }

    private void encodeHeader(FacesContext context, AbstractAccordionItem component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement(DIV_ELEM, component);

        String stateCssClass = "rf-ac-itm-hdr-" + (component.isDisabled() ? "dis" : (component.isActive() ? "act" : "inact"));
        writer.writeAttribute(CLASS_ATTRIBUTE,
            concatClasses("rf-ac-itm-hdr", stateCssClass, attributeAsString(component, "headerClass")), null);

        writer.writeAttribute(ID_ATTRIBUTE, component.getClientId(context) + ":header", null);
        renderPassThroughAttributes(context, component, HEADER_ATTRIBUTES);

        headerRenderer.encodeHeader(writer, context, component);

        writer.endElement(DIV_ELEM);
    }

    @Override
    protected JSObject getScriptObject(FacesContext context, UIComponent component) {
        return new JSObject("RichFaces.ui.AccordionItem", component.getClientId(context), getScriptObjectOptions(context,
            component));
    }

    @Override
    protected Map<String, Object> getScriptObjectOptions(FacesContext context, UIComponent component) {
        Map<String, Object> res = super.getScriptObjectOptions(context, component);
        res.put("disabled", ((AbstractTogglePanelTitledItem) component).isDisabled());

        return res;
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractAccordionItem.class;
    }
}

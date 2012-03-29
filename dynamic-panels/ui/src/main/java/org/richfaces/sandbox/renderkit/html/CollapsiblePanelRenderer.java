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

import org.ajax4jsf.javascript.JSObject;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.sandbox.component.AbstractCollapsiblePanel;
import org.richfaces.sandbox.component.AbstractTogglePanel;
import org.richfaces.renderkit.util.PanelIcons;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.richfaces.renderkit.HtmlConstants.CLASS_ATTRIBUTE;
import static org.richfaces.renderkit.HtmlConstants.DIV_ELEM;
import static org.richfaces.renderkit.HtmlConstants.ID_ATTRIBUTE;
import static org.richfaces.renderkit.HtmlConstants.STYLE_ATTRIBUTE;

/**
 * @author akolonitsky
 * @since 2010-08-27
 */
@ResourceDependencies({ @ResourceDependency(library = "org.richfaces", name = "ajax.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
        @ResourceDependency(name = "richfaces-event.js"),
        @ResourceDependency(library = "org.richfaces", name = "togglePanel.js"),
        @ResourceDependency(library = "org.richfaces", name = "togglePanelItem.js"),
        @ResourceDependency(library = "org.richfaces", name = "collapsiblePanel.js"),
        @ResourceDependency(library = "org.richfaces", name = "collapsiblePanelItem.js"),
        @ResourceDependency(library = "org.richfaces", name = "icons.ecss"),
        @ResourceDependency(library = "org.richfaces", name = "collapsiblePanel.ecss") })
@JsfRenderer(type = "org.richfaces.sandbox.CollapsiblePanelRenderer", family = AbstractCollapsiblePanel.COMPONENT_FAMILY)
public class CollapsiblePanelRenderer extends TogglePanelRenderer {
    public static final String SWITCH = "switch";
    public static final String BEFORE_SWITCH = "beforeswitch";
    private final TableIconsRendererHelper<AbstractCollapsiblePanel> headerRenderer = new TableIconsRendererHelper<AbstractCollapsiblePanel>(
        "header", "rf-cp") {
        protected void encodeHeaderLeftIcon(ResponseWriter writer, FacesContext context, AbstractCollapsiblePanel panel)
            throws IOException {
            String leftCollapsedIcon = panel.getLeftCollapsedIcon();
            if (leftCollapsedIcon == null || leftCollapsedIcon.trim().length() == 0) {
                leftCollapsedIcon = PanelIcons.chevronUp.toString();
            }
            String leftExpandedIcon = panel.getLeftExpandedIcon();
            if (leftExpandedIcon == null || leftExpandedIcon.trim().length() == 0) {
                leftExpandedIcon = PanelIcons.chevronDown.toString();
            }

            encodeTdIcon(writer, context, cssClassPrefix + "-ico", leftCollapsedIcon, leftExpandedIcon, PanelIcons.State.header);
        }

        protected void encodeHeaderRightIcon(ResponseWriter writer, FacesContext context, AbstractCollapsiblePanel panel)
            throws IOException {
            // TODO nick - should this be "-ico-exp"? also why expanded icon state is connected with right icon alignment?
            encodeTdIcon(writer, context, cssClassPrefix + "-exp-ico", panel.getRightCollapsedIcon(),
                panel.getRightExpandedIcon(), PanelIcons.State.header);
        }

        @Override
        protected void encodeHeaderTextValue(ResponseWriter writer, FacesContext context, AbstractCollapsiblePanel component)
            throws IOException {
            writer.startElement(DIV_ELEM, null);
            writer.writeAttribute(CLASS_ATTRIBUTE, cssClassPrefix + "-lbl-exp", null);
            writeFacetOrAttr(writer, context, component, text, text + "Expanded");
            writer.endElement(DIV_ELEM);

            writer.startElement(DIV_ELEM, null);
            writer.writeAttribute(CLASS_ATTRIBUTE, cssClassPrefix + "-lbl-colps", null);
            writeFacetOrAttr(writer, context, component, text, text + "Collapsed");
            writer.endElement(DIV_ELEM);
        }
    };

    @Override
    protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        super.doEncodeBegin(writer, context, component);

        encodeHeader(writer, context, (AbstractCollapsiblePanel) component);
    }

    @Override
    protected String getStyleClass(UIComponent component) {
        return concatClasses("rf-cp", super.getStyleClass(component));
    }

    @Override
    protected JSObject getScriptObject(FacesContext context, UIComponent component) {
        return new JSObject("RichFaces.ui.CollapsiblePanel", component.getClientId(context), getScriptObjectOptions(context,
            component));
    }

    @Override
    protected Map<String, Object> getScriptObjectOptions(FacesContext context, UIComponent component) {
        AbstractTogglePanel panel = (AbstractTogglePanel) component;

        Map<String, Object> options = new HashMap<String, Object>();
        options.put("activeItem", panel.getActiveItem());
        options.put("ajax", getAjaxOptions(context, panel));
        options.put("switchMode", panel.getSwitchType());

        TogglePanelRenderer.addEventOption(context, panel, options, SWITCH);
        TogglePanelRenderer.addEventOption(context, panel, options, BEFORE_SWITCH);

        return options;
    }

    private void encodeHeader(ResponseWriter writer, FacesContext context, AbstractCollapsiblePanel component)
        throws IOException {
        writer.startElement(DIV_ELEM, null);
        writer.writeAttribute(ID_ATTRIBUTE, component.getClientId(context) + ":header", null);
        writer.writeAttribute(
            CLASS_ATTRIBUTE,
            concatClasses("rf-cp-hdr", "rf-cp-hdr-" + (component.isExpanded() ? "exp" : "colps"),
                attributeAsString(component, "headerClass")), null);

        headerRenderer.encodeHeader(writer, context, component);

        writer.endElement(DIV_ELEM);
    }

    @Override
    protected void doEncodeChildren(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {

        AbstractCollapsiblePanel panel = (AbstractCollapsiblePanel) component;

        encodeContentChild(writer, context, component, panel);
        encodeEmptyChild(writer, context, component, panel);
    }

    private void encodeContentChild(ResponseWriter writer, FacesContext context, UIComponent component,
        AbstractCollapsiblePanel panel) throws IOException {
        if (panel.isExpanded()) {
            encodeContent(writer, context, component, true);
        } else {
            switch (panel.getSwitchType()) {
                case client:
                    encodeContent(writer, context, component, false);
                    break;

                case ajax:
                    context.getResponseWriter().write(getPlaceHolder(panel.getClientId(context) + ":content"));
                    break;

                case server:
                    // Do nothing.
                    break;

                default:
                    throw new IllegalStateException("Unknown switch type : " + panel.getSwitchType());
            }
        }
    }

    private void encodeEmptyChild(ResponseWriter writer, FacesContext context, UIComponent component,
        AbstractCollapsiblePanel panel) throws IOException {
        if (!panel.isExpanded()) {
            encodeEmptyDiv(writer, context, component, true);
        } else {
            switch (panel.getSwitchType()) {
                case client:
                    encodeEmptyDiv(writer, context, component, false);
                    break;

                case ajax:
                    writer.write(getPlaceHolder(panel.getClientId(context) + ":empty"));
                    break;

                case server:
                    // Do nothing.
                    break;

                default:
                    throw new IllegalStateException("Unknown switch type : " + panel.getSwitchType());
            }
        }
    }

    private String getPlaceHolder(String id) {
        return "<div id=\"" + id + "\" style=\"display:none\" ></div>";
    }

    private void encodeContent(ResponseWriter writer, FacesContext context, UIComponent component, boolean visible)
        throws IOException {
        writer.startElement(DIV_ELEM, component);
        writer.writeAttribute(ID_ATTRIBUTE, component.getClientId(context) + ":content", null);
        writer.writeAttribute(CLASS_ATTRIBUTE, concatClasses("rf-cp-b", attributeAsString(component, "bodyClass")), null);
        writer.writeAttribute(STYLE_ATTRIBUTE, styleElement("display", visible ? "block" : "none"), null);

        renderChildren(context, component);

        writer.endElement(DIV_ELEM);
    }

    private void encodeEmptyDiv(ResponseWriter writer, FacesContext context, UIComponent component, boolean visible)
        throws IOException {
        writer.startElement(DIV_ELEM, component);
        writer.writeAttribute(ID_ATTRIBUTE, component.getClientId(context) + ":empty", null);
        writer.writeAttribute(CLASS_ATTRIBUTE, "rf-cp-empty", null);
        writer.writeAttribute(STYLE_ATTRIBUTE, styleElement("display", visible ? "block" : "none"), null);
        writer.endElement(DIV_ELEM);
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractCollapsiblePanel.class;
    }
}

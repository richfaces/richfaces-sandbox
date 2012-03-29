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
import org.ajax4jsf.model.DataVisitResult;
import org.ajax4jsf.model.DataVisitor;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.sandbox.component.AbstractTab;
import org.richfaces.sandbox.component.AbstractTabPanel;
import org.richfaces.sandbox.component.AbstractTogglePanel;
import org.richfaces.sandbox.component.AbstractTogglePanelItemInterface;
import org.richfaces.sandbox.component.AbstractTogglePanelItemVisitor;
import org.richfaces.sandbox.component.AbstractTogglePanelTitledItem;
import org.richfaces.component.util.HtmlUtil;
import org.richfaces.context.ExtendedPartialViewContext;
import org.richfaces.renderkit.HtmlConstants;
import org.richfaces.renderkit.RenderKitUtils;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;

import static org.richfaces.sandbox.component.AbstractTogglePanelTitledItem.HeaderStates.active;
import static org.richfaces.sandbox.component.AbstractTogglePanelTitledItem.HeaderStates.disabled;
import static org.richfaces.sandbox.component.AbstractTogglePanelTitledItem.HeaderStates.inactive;
import static org.richfaces.renderkit.HtmlConstants.CLASS_ATTRIBUTE;
import static org.richfaces.renderkit.HtmlConstants.DIV_ELEM;
import static org.richfaces.renderkit.HtmlConstants.ID_ATTRIBUTE;
import static org.richfaces.renderkit.HtmlConstants.SPAN_ELEM;
import static org.richfaces.renderkit.HtmlConstants.STYLE_ATTRIBUTE;
import static org.richfaces.renderkit.HtmlConstants.TBODY_ELEMENT;
import static org.richfaces.renderkit.HtmlConstants.TD_ELEM;
import static org.richfaces.renderkit.HtmlConstants.TR_ELEMENT;
import static org.richfaces.renderkit.RenderKitUtils.renderPassThroughAttributes;

/**
 * @author akolonitsky
 * @since 2010-08-24
 */
@ResourceDependencies({ @ResourceDependency(library = "org.richfaces", name = "ajax.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
        @ResourceDependency(name = "richfaces-event.js"),
        @ResourceDependency(library = "org.richfaces", name = "togglePanel.js"),
        @ResourceDependency(library = "org.richfaces", name = "tabPanel.js"),
        @ResourceDependency(library = "org.richfaces", name = "tabPanel.ecss") })
@JsfRenderer(type = "org.richfaces.sandbox.TabPanelRenderer", family = AbstractTabPanel.COMPONENT_FAMILY)
public class TabPanelRenderer extends TogglePanelRenderer {
    private static final RenderKitUtils.Attributes HEADER_ATTRIBUTES = RenderKitUtils.attributes()
            .generic("onclick", "onheaderclick", "headerclick").generic("ondblclick", "onheaderdblclick", "headerdblclick")
            .generic("onmousedown", "onheadermousedown", "headermousedown")
            .generic("onmousemove", "onheadermousemove", "headermousemove")
            .generic("onmouseup", "onheadermouseup", "headermouseup");
    private static final String DIV = DIV_ELEM;
    private static final String STYLE = STYLE_ATTRIBUTE;
    private static final String CLASS = CLASS_ATTRIBUTE;

    protected static void addOnCompleteParam(FacesContext context, String newValue, String panelId) {
        StringBuilder onComplete = new StringBuilder();
        onComplete.append("RichFaces.$('").append(panelId).append("').onCompleteHandler('").append(newValue).append("');");

        ExtendedPartialViewContext.getInstance(context).appendOncomplete(onComplete.toString());
    }

    @Override
    protected boolean isSubmitted(FacesContext context, AbstractTogglePanel panel) {
        UIComponent item = (UIComponent) panel.getItem(panel.getSubmittedActiveItem());

        if (item == null) {
            return false;
        }

        Map<String, String> parameterMap = context.getExternalContext().getRequestParameterMap();
        return parameterMap.get(item.getClientId(context)) != null;
    }

    @Override
    protected void doEncodeBegin(ResponseWriter w, FacesContext context, UIComponent component) throws IOException {
        super.doEncodeBegin(w, context, component);

        AbstractTabPanel tabPanel = (AbstractTabPanel) component;
        if (tabPanel.isHeaderPositionedTop()) {
            writeTabsLine(w, context, component);
            writeTabsLineSeparator(w, component);
        }
    }

    private void writeTabsLineSeparator(ResponseWriter writer, UIComponent component) throws IOException {
        writer.startElement(DIV, component);
        writer.writeAttribute(CLASS_ATTRIBUTE, "rf-tab-hdr-brd", null);
        writer.endElement(DIV);
    }

    private void writeTabsLine(final ResponseWriter w, final FacesContext context, UIComponent comp) throws IOException {
        w.startElement(DIV, comp);
        final AbstractTabPanel tabPanel = (AbstractTabPanel) comp;
        if (tabPanel.isHeaderPositionedTop()) {
            w.writeAttribute(CLASS, "rf-tab-hdr-tabline-vis rf-tab-hdr-tabline-top", null);
        } else {
            w.writeAttribute(CLASS, "rf-tab-hdr-tabline-vis rf-tab-hdr-tabline-btm", null);
        }
        w.startElement("table", comp);
        w.writeAttribute(CLASS_ATTRIBUTE, "rf-tab-hdr-tabs", null);
        w.writeAttribute("cellspacing", "0", null);
        w.startElement(TBODY_ELEMENT, comp);
        w.startElement(TR_ELEMENT, comp);

        writeTopTabFirstSpacer(w, comp);


        if (tabPanel.getValue() != null) {
            try {
                DataVisitor visitor = new AbstractTogglePanelItemVisitor(tabPanel, new AbstractTogglePanelItemVisitor.TabVisitorCallback() {
                    @Override
                    public DataVisitResult visit(AbstractTogglePanelItemInterface item)
                    {
                        AbstractTab tab = (AbstractTab) item;
                        try {
                            writeTopTabHeader(context, w, tab);
                            writeTopTabSpacer(w, tabPanel);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return DataVisitResult.CONTINUE;
                    }
                });
                tabPanel.walk(context, visitor, null);
            } finally {
                tabPanel.setRowKey(context, null);
            }
        } else {
            for (AbstractTogglePanelItemInterface item : ((AbstractTogglePanel) comp).getRenderedItems()) {
                AbstractTab tab = (AbstractTab) item;
                writeTopTabHeader(context, w, tab);
                writeTopTabSpacer(w, comp);
            }
        }

        writeTopTabLastSpacer(w, comp);

        w.endElement(TR_ELEMENT);
        w.endElement(TBODY_ELEMENT);
        w.endElement("table");

        writeTopTabsControl(w, comp, "rf-tab-hdr-scrl-lft rf-tab-hdn", "\u00AB");
        writeTopTabsControl(w, comp, "rf-tab-hdr-tablst rf-tab-hdn", "\u2193");
        writeTopTabsControl(w, comp, "rf-tab-hdr-scrl-rgh rf-tab-hdn", "\u00BB");

        w.endElement(DIV_ELEM);
    }

    @Override
    protected String getStyle(UIComponent component) {
        return attributeAsString(component, "style");
    }

    @Override
    protected String getStyleClass(UIComponent component) {
        return HtmlUtil.concatClasses("rf-tbp", attributeAsString(component, "styleClass"));
    }

    private void writeTopTabHeader(FacesContext context, ResponseWriter writer, AbstractTab tab) throws IOException {
        boolean isActive = tab.isActive();
        boolean isDisabled = tab.isDisabled();
        // TODO: Ilya, review. Much HTML because we always encoding all states. Need to optimize somehow.
        encodeTabHeader(context, tab, writer, inactive, !isActive && !isDisabled);
        encodeTabHeader(context, tab, writer, active, isActive && !isDisabled);
        encodeTabHeader(context, tab, writer, disabled, isDisabled);
    }

    private String positionAbbreviation(AbstractTab tab) {
        if (tab.getParentPanel().isHeaderPositionedTop()) {
            return "top";
        } else {
            return "btm";
        }
    }

    private void encodeTabHeader(FacesContext context, AbstractTab tab, ResponseWriter writer,
            AbstractTogglePanelTitledItem.HeaderStates state, Boolean isDisplay) throws IOException {

        String headerStateClass = "rf-tab-hdr-" + state.abbreviation();
        String headerPositionClass = "rf-tab-hdr-" + positionAbbreviation(tab);

        writer.startElement(TD_ELEM, tab);
        writer.writeAttribute(ID_ATTRIBUTE, tab.getClientId(context) + ":header:" + state.toString(), null);
        renderPassThroughAttributes(context, tab, HEADER_ATTRIBUTES);
        writer.writeAttribute(
                CLASS_ATTRIBUTE,
                concatClasses("rf-tab-hdr", headerStateClass, headerPositionClass,
                        attributeAsString(tab, "headerClass"), attributeAsString(tab, state.headerClass())), null);
        writer.writeAttribute(STYLE_ATTRIBUTE,
                concatStyles(isDisplay ? "" : "display : none", attributeAsString(tab, "headerStyle")), null);

        writer.startElement(SPAN_ELEM, tab);
        writer.writeAttribute(CLASS_ATTRIBUTE, "rf-tab-lbl", null);

        UIComponent headerFacet = tab.getHeaderFacet(state);
        if (headerFacet != null && headerFacet.isRendered()) {
            headerFacet.encodeAll(context);
        } else {
            Object headerText = tab.getAttributes().get("header");
            if (headerText != null && !headerText.equals("")) {
                writer.writeText(headerText, null);
            }
        }

        writer.endElement(SPAN_ELEM);

        writer.endElement(TD_ELEM);
    }

    private void writeTopTabsControl(ResponseWriter w, UIComponent comp, String styles, String text) throws IOException {
        w.startElement(DIV_ELEM, comp);
        w.writeAttribute(CLASS_ATTRIBUTE, styles, null);
        w.writeText(text, null);
        w.endElement(DIV_ELEM);
    }

    private void writeTopTabFirstSpacer(ResponseWriter w, UIComponent comp) throws IOException {
        AbstractTabPanel tabPanel = (AbstractTabPanel) comp;
        if (tabPanel.isHeaderAlignedLeft()) {
            writeTopTabSpacer(w, comp, "padding-left: 5px;", "rf-tab-hdr-spcr");
        } else {
            writeTopTabSpacer(w, comp, "padding-left: 5px; width:100%", "rf-tab-hdr-spcr");
        }
    }

    private void writeTopTabSpacer(ResponseWriter w, UIComponent comp) throws IOException {
        writeTopTabSpacer(w, comp, "", "rf-tab-hdr-spcr rf-tab-hortab-tabspcr-wdh");
    }

    private void writeTopTabLastSpacer(ResponseWriter w, UIComponent comp) throws IOException {
        AbstractTabPanel tabPanel = (AbstractTabPanel) comp;
        if (tabPanel.isHeaderAlignedLeft()) {
            writeTopTabSpacer(w, comp, "padding-right: 5px; width: 100%;", "rf-tab-hdr-spcr");
        } else {
            writeTopTabSpacer(w, comp, "padding-right: 5px;", "rf-tab-hdr-spcr");
        }
    }

    private void writeTopTabSpacer(ResponseWriter w, UIComponent comp, String style, String styleClass) throws IOException {
        w.startElement(TD_ELEM, comp);
        w.writeAttribute(STYLE, style, null);
        w.writeAttribute(CLASS, styleClass, null);
        w.write("<br />");
        w.endElement(TD_ELEM);
    }

    @Override
    protected void doEncodeEnd(final ResponseWriter writer, final FacesContext context, UIComponent component) throws IOException
    {
        final AbstractTabPanel tabPanel = (AbstractTabPanel) component;
        if (!tabPanel.isHeaderPositionedTop()) {
            writeTabsLineSeparator(writer, component);
            writeTabsLine(writer, context, component);
        }
        if (tabPanel.getValue() != null) {
            try {
                final DataVisitor visitor = new AbstractTogglePanelItemVisitor(tabPanel, new AbstractTogglePanelItemVisitor.TabVisitorCallback() {
                    @Override
                    public DataVisitResult visit(AbstractTogglePanelItemInterface item)
                    {
                        AbstractTab tab = (AbstractTab) item;
                        TabRenderer renderer = (TabRenderer) tab.getRenderer(context);
                        try {
                            renderer.writeJavaScript(writer, context, tab);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return DataVisitResult.CONTINUE;
                    }
                });
                tabPanel.walk(context, visitor, null);
            } finally {
                tabPanel.setRowKey(context, null);
            }
        } else {
            if (tabPanel.getChildCount() > 0) {
                for (UIComponent child : tabPanel.getChildren()) {
                    if (child instanceof AbstractTab) {
                        AbstractTab tab = (AbstractTab) child;
                        TabRenderer renderer = (TabRenderer) tab.getRenderer(context);
                        renderer.writeJavaScript(writer, context, tab);
                    }
                }
            }
        }


        writer.endElement(HtmlConstants.DIV_ELEM);
    }

    @Override
    protected JSObject getScriptObject(FacesContext context, UIComponent component) {
        return new JSObject("RichFaces.ui.TabPanel", component.getClientId(context), getScriptObjectOptions(context, component));
    }

    @Override
    protected Map<String, Object> getScriptObjectOptions(FacesContext context, UIComponent component) {
        Map<String, Object> options = super.getScriptObjectOptions(context, component);

        options.put("isKeepHeight", attributeAsString(component, "height").length() > 0);

        return options;
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractTabPanel.class;
    }
}

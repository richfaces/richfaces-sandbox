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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.ResponseWriter;

import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.context.ExtendedPartialViewContext;
import org.richfaces.javascript.JSFunctionDefinition;
import org.richfaces.javascript.JSObject;
import org.richfaces.javascript.JSReference;
import org.richfaces.model.DataVisitResult;
import org.richfaces.model.DataVisitor;
import org.richfaces.sandbox.component.AbstractTogglePanel;
import org.richfaces.sandbox.component.AbstractTogglePanelItemInterface;
import org.richfaces.sandbox.component.AbstractTogglePanelItemVisitor;
import org.richfaces.ui.behavior.HandlersChain;
import org.richfaces.ui.common.AjaxOptions;
import org.richfaces.ui.common.HtmlConstants;
import org.richfaces.ui.common.meta.MetaComponentRenderer;
import org.richfaces.ui.common.meta.MetaComponentResolver;
import org.richfaces.util.AjaxRendererUtils;
import org.richfaces.util.FormUtil;
import org.richfaces.util.HtmlUtil;

/**
 * @author akolonitsky
 */
@ResourceDependencies({ @ResourceDependency(library = "org.richfaces", name = "ajax.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
        @ResourceDependency(name = "richfaces-event.js"),
        @ResourceDependency(library = "org.richfaces", name = "togglePanel.js") })
@JsfRenderer(type = "org.richfaces.sandbox.TogglePanelRenderer", family = AbstractTogglePanel.COMPONENT_FAMILY)
public class TogglePanelRenderer extends DivPanelRenderer implements MetaComponentRenderer {
    public static final String VALUE_POSTFIX = "-value";
    protected static final String ITEM_CHANGE = "itemchange";
    protected static final String BEFORE_ITEM_CHANGE = "beforeitemchange";
    private static final String ON = "on";

    @Override
    protected void doDecode(FacesContext context, UIComponent component) {
        AbstractTogglePanel panel = (AbstractTogglePanel) component;

        Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();

        // Get the new panel value to show
        String newValue = requestParameterMap.get(getValueRequestParamName(context, component));
        if (newValue != null) {
            panel.setSubmittedActiveItem(newValue);

            if (isSubmitted(context, panel)) {
                PartialViewContext pvc = context.getPartialViewContext();

                pvc.getRenderIds().add(
                    component.getClientId(context) + MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR
                        + AbstractTogglePanel.ACTIVE_ITEM_META_COMPONENT);
            }
        }
    }

    protected boolean isSubmitted(FacesContext context, AbstractTogglePanel panel) {
        Map<String, String> parameterMap = context.getExternalContext().getRequestParameterMap();
        return parameterMap.get(panel.getClientId(context)) != null;
    }

    protected static void addOnCompleteParam(FacesContext context, String newValue, String panelId) {
        StringBuilder onComplete = new StringBuilder();
        onComplete.append("RichFaces.$('").append(panelId).append("').onCompleteHandler('").append(newValue).append("');");

        ExtendedPartialViewContext.getInstance(context).appendOncomplete(onComplete.toString());
    }

    static String getValueRequestParamName(FacesContext context, UIComponent component) {
        return component.getClientId(context) + VALUE_POSTFIX;
    }

    @Override
    protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        FormUtil.throwEnclFormReqExceptionIfNeed(context, component);

        super.doEncodeBegin(writer, context, component);
        AbstractTogglePanel panel = (AbstractTogglePanel) component;

        writer.startElement(HtmlConstants.INPUT_ELEM, component);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, getValueRequestParamName(context, component), null);
        writer.writeAttribute(HtmlConstants.NAME_ATTRIBUTE, getValueRequestParamName(context, component), null);
        writer.writeAttribute(HtmlConstants.TYPE_ATTR, HtmlConstants.INPUT_TYPE_HIDDEN, null);
        writer.writeAttribute(HtmlConstants.VALUE_ATTRIBUTE, panel.getActiveItem(), null);
        writer.endElement(HtmlConstants.INPUT_ELEM);

        writeJavaScript(writer, context, component);
    }

    @Override
    protected String getStyleClass(UIComponent component) {
        return HtmlUtil.concatClasses("rf-tgp", attributeAsString(component, "styleClass"));
    }

    @Override
    protected void doEncodeChildren(final ResponseWriter writer, final FacesContext context, UIComponent component) throws IOException
    {
        final AbstractTogglePanel abstractTogglePanel = (AbstractTogglePanel) component;
        if (abstractTogglePanel.getValue() != null) {
                    try {
                        writer.startElement(HtmlConstants.DIV_ELEM, null);
                        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-crl-sl", "class");
                        DataVisitor visitor = new AbstractTogglePanelItemVisitor(abstractTogglePanel, new AbstractTogglePanelItemVisitor.TabVisitorCallback() {
                            @Override
                            public DataVisitResult visit(AbstractTogglePanelItemInterface item)
                            {
                                try {
                                    ((UIComponent) item).encodeAll(context);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return DataVisitResult.CONTINUE;
                            }
                        });
                        abstractTogglePanel.walk(context, visitor, null);
                        writer.endElement(HtmlConstants.DIV_ELEM);
                    } finally {
                        abstractTogglePanel.setRowKey(context, null);
                    }
        } else {
            renderChildren(context, component);
        }
    }

    @Override
    protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        writer.endElement(HtmlConstants.DIV_ELEM);
    }

    @Override
    protected JSObject getScriptObject(FacesContext context, UIComponent component) {
        return new JSObject("RichFaces.ui.TogglePanel", component.getClientId(context), getScriptObjectOptions(context,
            component));
    }

    @Override
    protected Map<String, Object> getScriptObjectOptions(FacesContext context, UIComponent component) {
        AbstractTogglePanel panel = (AbstractTogglePanel) component;

        Map<String, Object> options = new HashMap<String, Object>();
        options.put("activeItem", panel.getActiveItem());
        options.put("cycledSwitching", panel.isCycledSwitching());
        options.put("ajax", getAjaxOptions(context, panel));

        addEventOption(context, panel, options, ITEM_CHANGE);
        addEventOption(context, panel, options, BEFORE_ITEM_CHANGE);

        return options;
    }

    public static void addEventOption(FacesContext context, UIComponent component, Map<String, Object> options, String eventName) {

        HandlersChain handlersChain = new HandlersChain(context, component);
        handlersChain.addInlineHandlerFromAttribute(ON + eventName);
        handlersChain.addBehaviors(eventName);
        // handlersChain.addAjaxSubmitFunction();

        String handler = handlersChain.toScript();
        if (handler != null) {
            options.put(ON + eventName, new JSFunctionDefinition(JSReference.EVENT).addToBody(handler));
        }
    }

    public static AjaxOptions getAjaxOptions(FacesContext context, UIComponent panel) {
        return AjaxRendererUtils.buildEventOptions(context, panel);
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractTogglePanel.class;
    }

    public void encodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId) throws IOException {
        if (AbstractTogglePanel.ACTIVE_ITEM_META_COMPONENT.equals(metaComponentId)) {
            AbstractTogglePanel panel = (AbstractTogglePanel) component;
            AbstractTogglePanelItemInterface item = panel.getItem(panel.getActiveItem());

            if (item != null) {
                partialStart(context, ((UIComponent) item).getClientId(context));
                ((UIComponent) item).encodeAll(context);
                partialEnd(context);
                addOnCompleteParam(context, item.getName(), panel.getClientId(context));
            } else {
                partialStart(context, component.getClientId(context));
                component.encodeAll(context);
                partialEnd(context);
                addOnCompleteParam(context, panel.getActiveItem(), panel.getClientId(context));
            }
        } else {
            throw new IllegalArgumentException(metaComponentId);
        }
    }

    public void decodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId) {
        // TODO Auto-generated method stub
    }

    protected void partialStart(FacesContext facesContext, String id) throws IOException {
        facesContext.getPartialViewContext().getPartialResponseWriter().startUpdate(id);
    }

    protected void partialEnd(FacesContext facesContext) throws IOException {
        facesContext.getPartialViewContext().getPartialResponseWriter().endUpdate();
    }
}

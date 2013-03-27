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

import org.ajax4jsf.javascript.JSFunctionDefinition;
import org.ajax4jsf.javascript.JSObject;
import org.ajax4jsf.javascript.JSReference;
import org.ajax4jsf.javascript.ScriptString;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractDashboard;
import org.richfaces.component.SwitchType;
import org.richfaces.event.PositionChangeEvent;
import org.richfaces.renderkit.AjaxFunction;
import org.richfaces.renderkit.HtmlConstants;
import org.richfaces.renderkit.RendererBase;
import org.richfaces.renderkit.util.AjaxRendererUtils;
import org.richfaces.renderkit.util.RendererUtils;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@JsfRenderer(family = AbstractDashboard.COMPONENT_FAMILY, type = DashboardRenderer.RENDERER_TYPE)
@ResourceDependencies({@ResourceDependency(library = "javax.faces", name = "jsf.js"), @ResourceDependency(name = "jquery.js", target = "head"),
    @ResourceDependency(name = "richfaces.js", target = "head"),
    @ResourceDependency(name = "richfaces-base-component.js", target = "head"),
    @ResourceDependency(name = "richfaces-event.js", target = "head"),
    @ResourceDependency(library = "org.richfaces", name = "jquery-ui-core.js", target = "head"),
    @ResourceDependency(name = "jquery.ui.sortable.js", target = "head"), @ResourceDependency(name = "richfaces.dashboard.js", target = "head"),
    @ResourceDependency(name = "dashboard.css", target = "head")})
public class DashboardRenderer extends RendererBase {
// ------------------------------ FIELDS ------------------------------

    public static final String RENDERER_TYPE = "org.richfaces.DashboardRenderer";

    private static final Map<String, Object> DEFAULTS;

    private static final String END_X_PARAM = "eX";

    private static final String END_Y_PARAM = "eY";

    private static final String EVENT_TYPE_PARAM = "eventType";

    private static final String POSITION_CHANGE_EVENT = "change";

    private static final String START_X_PARAM = "sX";

    private static final String START_Y_PARAM = "sY";

// -------------------------- STATIC METHODS --------------------------

    static {
        Map<String, Object> defaults = new HashMap<String, Object>();
        DEFAULTS = Collections.unmodifiableMap(defaults);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public void decode(FacesContext context, UIComponent component) {
        super.decode(context, component);
        if (!component.isRendered()) {
            return;
        }
        Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        if (requestParameterMap.get(component.getClientId(context)) != null) {
            String endx = requestParameterMap.get(getFieldId(context, (AbstractDashboard) component, END_X_PARAM));
            String endy = requestParameterMap.get(getFieldId(context, (AbstractDashboard) component, END_Y_PARAM));
            String starty = requestParameterMap.get(getFieldId(context, (AbstractDashboard) component, START_Y_PARAM));
            String startx = requestParameterMap.get(getFieldId(context, (AbstractDashboard) component, START_X_PARAM));
            String eventTypeParam = requestParameterMap.get(getFieldId(context, (AbstractDashboard) component, EVENT_TYPE_PARAM));

            if (POSITION_CHANGE_EVENT.equals(eventTypeParam)) {
                new PositionChangeEvent(component, Integer.parseInt(startx), Integer.parseInt(starty), Integer.parseInt(endx), Integer.parseInt(endy)).queue();
            }
        }
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    protected void addOptionIfSetAndNotDefault(String optionName, Object value, Map<String, Object> options) {
        if (value != null && !"".equals(value) && !value.equals(DEFAULTS.get(optionName)) && !(value instanceof Collection && ((Collection) value).size() == 0)
            && !(value instanceof Map && ((Map) value).size() == 0)) {
            options.put(optionName, value);
        }
    }

    protected Object createSubmitEventFunction(FacesContext context, AbstractDashboard component) {
        ScriptString jsFunction;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(getFieldId(context, component, START_X_PARAM), new JSReference(START_X_PARAM));
        params.put(getFieldId(context, component, START_Y_PARAM), new JSReference(START_Y_PARAM));
        params.put(getFieldId(context, component, END_X_PARAM), new JSReference(END_X_PARAM));
        params.put(getFieldId(context, component, END_Y_PARAM), new JSReference(END_Y_PARAM));
        params.put(getFieldId(context, component, EVENT_TYPE_PARAM), new JSReference(EVENT_TYPE_PARAM));
        String clientId = component.getClientId();
        params.put(clientId, clientId);
        if (isAjaxMode(component)) {
            AjaxFunction ajaxFunction = AjaxRendererUtils.buildAjaxFunction(context, component);
            ajaxFunction.getOptions().getParameters().putAll(params);
            jsFunction = ajaxFunction;
        } else {
            return null;
        }
        return new JSFunctionDefinition("event", EVENT_TYPE_PARAM, START_X_PARAM, START_Y_PARAM, END_X_PARAM, END_Y_PARAM).addToBody(jsFunction);
    }

    @Override
    protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        if (!(component instanceof AbstractDashboard)) {
            return;
        }
        String clientId = component.getClientId(context);
        writer.startElement(HtmlConstants.DIV_ELEM, null);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, clientId, HtmlConstants.ID_ATTRIBUTE);
        Object styleClass = component.getAttributes().get("styleClass");
        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-db" + (styleClass == null ? "" : styleClass), HtmlConstants.STYLE_CLASS_ATTR);
        getUtils().encodePassThruAttribute(context, component.getAttributes(), writer, "style");
        getUtils().encodePassThruAttribute(context, component.getAttributes(), writer, "title");
    }

    @Override
    protected void doEncodeChildren(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        for (UIComponent child : component.getChildren()) {
            if (child instanceof UIColumn) {
                if (!child.isRendered()) {
                    continue;
                }
                writer.startElement(HtmlConstants.DIV_ELEM, child);
                writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, child.getClientId(context), HtmlConstants.ID_ATTRIBUTE);
                Object styleClass = child.getAttributes().get("styleClass");
                Object headerClass = child.getAttributes().get("headerClass");
                Object footerClass = child.getAttributes().get("footerClass");
                StringBuilder className = new StringBuilder("rf-db-cl");
                if (styleClass != null) {
                    className.append(" ").append(styleClass);
                }
                if (headerClass != null) {
                    className.append(" ").append(headerClass);
                }
                if (footerClass != null) {
                    className.append(" ").append(footerClass);
                }
                writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, className.toString(), HtmlConstants.CLASS_ATTRIBUTE);
                for (UIComponent grandChild : child.getChildren()) {
                    grandChild.encodeAll(context);
                }
                writer.endElement(HtmlConstants.DIV_ELEM);
            } else {
                child.encodeAll(context);
            }
        }
    }

    @Override
    protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        if (!(component instanceof AbstractDashboard)) {
            return;
        }
        String clientId = component.getClientId(context);
        writer.startElement(HtmlConstants.SCRIPT_ELEM, null);
        writer.writeAttribute(HtmlConstants.TYPE_ATTR, "text/javascript", "type");
        final Map<String, Object> options = getOptions(context, (AbstractDashboard) component);
        options.put("submitEventFunction", createSubmitEventFunction(context, (AbstractDashboard) component));
        writer.writeText(new JSObject("RichFaces.ui.Dashboard", clientId, options), null);
        writer.writeText(";", null);
        writer.endElement(HtmlConstants.SCRIPT_ELEM);
        writer.startElement(HtmlConstants.DIV_ELEM, component);
        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-db-clr", HtmlConstants.CLASS_ATTRIBUTE);
        writer.endElement(HtmlConstants.DIV_ELEM);
        writer.endElement(HtmlConstants.DIV_ELEM);
    }

    protected String getFieldId(FacesContext context, AbstractDashboard component, String attribute) {
        return RendererUtils.getInstance().clientId(context, component) + UINamingContainer.getSeparatorChar(context) + attribute;
    }

    protected Map<String, Object> getOptions(FacesContext context, AbstractDashboard dashboard) throws IOException {
        /**
         * Include only attributes that are actually set.
         */
        Map<String, Object> options = new HashMap<String, Object>();
        addOptionIfSetAndNotDefault("placeholderClass", dashboard.getPlaceholderClass(), options);
        addOptionIfSetAndNotDefault("forcePlaceholderSize", dashboard.isForcePlaceholderSize(), options);
        addOptionIfSetAndNotDefault("enabled", dashboard.isEnabled(), options);
        addOptionIfSetAndNotDefault("beforechange", dashboard.getOnbeforechange(), options);
        addOptionIfSetAndNotDefault("change", dashboard.getOnchange(), options);
        return options;
    }

    protected boolean isAjaxMode(AbstractDashboard component) {
        SwitchType mode = component.getSwitchType();
        return SwitchType.ajax.equals(mode) || null == mode;
    }
}

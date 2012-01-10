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
import org.richfaces.component.AbstractVisualsearch;
import org.richfaces.component.SwitchType;
import org.richfaces.component.event.FacetSuggestionEvent;
import org.richfaces.component.event.SearchEvent;
import org.richfaces.component.event.ValueSuggestionEvent;
import org.richfaces.renderkit.AjaxFunction;
import org.richfaces.renderkit.HtmlConstants;
import org.richfaces.renderkit.InputRendererBase;
import org.richfaces.renderkit.util.AjaxRendererUtils;
import org.richfaces.renderkit.util.RendererUtils;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@JsfRenderer(family = AbstractVisualsearch.COMPONENT_FAMILY, type = VisualsearchRenderer.RENDERER_TYPE)
@ResourceDependencies({@ResourceDependency(library = "javax.faces", name = "jsf.js"), @ResourceDependency(name = "jquery.js", target = "head"),
    @ResourceDependency(name = "richfaces.js", target = "head"), @ResourceDependency(name = "richfaces-event.js", target = "head"),
    @ResourceDependency(name = "richfaces-base-component.js", target = "head"), @ResourceDependency(name = "jquery.ui.core.js", target = "head"),
    @ResourceDependency(name = "jquery.ui.position.js", target = "head"), @ResourceDependency(name = "jquery.ui.widget.js", target = "head"),
    @ResourceDependency(name = "jquery.ui.autocomplete.js", target = "head"), @ResourceDependency(name = "underscore.js", target = "head"),
    @ResourceDependency(name = "backbone.js", target = "head"), @ResourceDependency(name = "visualsearch.js", target = "head"),
    @ResourceDependency(name = "richfaces.visualsearch.js", target = "head"), @ResourceDependency(name = "visualsearch-datauri.css", target = "head")})
public class VisualsearchRenderer extends InputRendererBase {
// ------------------------------ FIELDS ------------------------------

    public static final String RENDERER_TYPE = "org.richfaces.VisualsearchRenderer";

    private static final String CALLBACK = "callback";

    private static final Map<String, Object> DEFAULTS;

    private static final String EVENT_TYPE_PARAM = "eventType";

    private static final String FACET_PARAM = "facet";

    private static final String QUERY_JSON_PARAM = "queryJSON";

    private static final String QUERY_PARAM = "query";

    private static final String SEARCH_EVENT = "search";

    private static final String SEARCH_TERM_PARAM = "searchTerm";

    private static final String SUGGEST_FACET_EVENT = "suggestFacet";

    private static final String SUGGEST_VALUE_EVENT = "suggestValue";

// -------------------------- STATIC METHODS --------------------------

    static {
        Map<String, Object> defaults = new HashMap<String, Object>();
        DEFAULTS = Collections.unmodifiableMap(defaults);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public void decode(FacesContext context, UIComponent component)
    {
        super.decode(context, component);
        if (!component.isRendered()) {
            return;
        }
        Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        if (requestParameterMap.get(component.getClientId(context)) != null) {
            String queryParam = requestParameterMap.get(getFieldId(context, (AbstractVisualsearch) component, QUERY_PARAM));
            String queryJSONParam = requestParameterMap.get(getFieldId(context, (AbstractVisualsearch) component, QUERY_JSON_PARAM));
            String searchTermParam = requestParameterMap.get(getFieldId(context, (AbstractVisualsearch) component, SEARCH_TERM_PARAM));
            String facetParam = requestParameterMap.get(getFieldId(context, (AbstractVisualsearch) component, FACET_PARAM));
            String eventTypeParam = requestParameterMap.get(getFieldId(context, (AbstractVisualsearch) component, EVENT_TYPE_PARAM));

            if (null != queryParam) {
                UIInput input = (UIInput) component;
                input.setSubmittedValue(queryParam);
            }

            if (SUGGEST_VALUE_EVENT.equals(eventTypeParam)) {
                new ValueSuggestionEvent(component, searchTermParam, facetParam).queue();
            } else if (SUGGEST_FACET_EVENT.equals(eventTypeParam)) {
                new FacetSuggestionEvent(component, searchTermParam).queue();
            } else if (SEARCH_EVENT.equals(eventTypeParam)) {
                final SearchEvent searchEvent = new SearchEvent(component);
                searchEvent.setPhaseId(PhaseId.INVOKE_APPLICATION);
                searchEvent.queue();
            }
        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException
    {
        if (!(component instanceof AbstractVisualsearch)) {
            return;
        }
        String clientId = component.getClientId(context);
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement(HtmlConstants.DIV_ELEM, null);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, getUtils().clientId(context, component), "type");
        writer.startElement(HtmlConstants.SCRIPT_ELEM, null);
        writer.writeAttribute(HtmlConstants.TYPE_ATTR, "text/javascript", "type");
        final Map<String, Object> options = getOptions(context, (AbstractVisualsearch) component);
        options.put("submitEventFunction", createSubmitEventFunction(context, (AbstractVisualsearch) component));
        writer.writeText(new JSObject("RichFaces.ui.Visualsearch", clientId, options), null);
        writer.writeText(";", null);
        writer.endElement(HtmlConstants.SCRIPT_ELEM);
        writer.endElement(HtmlConstants.DIV_ELEM);
    }

    protected void addOptionIfSetAndNotDefault(String optionName, Object value, Map<String, Object> options)
    {
        if (value != null && !"".equals(value) && !value.equals(DEFAULTS.get(
            optionName)) && !(value instanceof Collection && ((Collection) value).size() == 0) && !(value instanceof Map && ((Map) value).size() == 0)) {
            options.put(optionName, value);
        }
    }

    protected Object createSubmitEventFunction(FacesContext context, AbstractVisualsearch component)
    {
        ScriptString jsFunction;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(getFieldId(context, component, FACET_PARAM), new JSReference(FACET_PARAM));
        params.put(getFieldId(context, component, SEARCH_TERM_PARAM), new JSReference(SEARCH_TERM_PARAM));
        params.put(getFieldId(context, component, QUERY_PARAM), new JSReference(QUERY_PARAM));
        params.put(getFieldId(context, component, QUERY_JSON_PARAM), new JSReference(QUERY_JSON_PARAM));
        params.put(getFieldId(context, component, EVENT_TYPE_PARAM), new JSReference(EVENT_TYPE_PARAM));
        String clientId = component.getClientId();
        params.put(clientId, clientId);
        if (isAjaxMode(component)) {
            AjaxFunction ajaxFunction = AjaxRendererUtils.buildAjaxFunction(context, component);
            ajaxFunction.getOptions().getParameters().putAll(params);
            ajaxFunction.getOptions().set("complete", new JSReference(CALLBACK));
            jsFunction = ajaxFunction;
        } else {
            return null;
        }
        return new JSFunctionDefinition("event", EVENT_TYPE_PARAM, QUERY_PARAM, QUERY_JSON_PARAM, FACET_PARAM, SEARCH_TERM_PARAM, CALLBACK).addToBody(
            jsFunction);
    }

    protected String getFieldId(FacesContext context, AbstractVisualsearch component, String attribute)
    {
        return RendererUtils.getInstance().clientId(context, component) + UINamingContainer.getSeparatorChar(context) + attribute;
    }

    protected Map<String, Object> getOptions(FacesContext context, AbstractVisualsearch visualsearch) throws IOException
    {
        /**
         * Include only attributes that are actually set.
         */
        Map<String, Object> options = new HashMap<String, Object>();
        addOptionIfSetAndNotDefault("query", visualsearch.getQuery(), options);
        addOptionIfSetAndNotDefault("onsearch", visualsearch.getOnsearch(), options);
        addOptionIfSetAndNotDefault("facets", visualsearch.getFacetSuggestions(), options);
        addOptionIfSetAndNotDefault("values", visualsearch.getValueSuggestions(), options);
        return options;
    }

    protected boolean isAjaxMode(AbstractVisualsearch component)
    {
        SwitchType mode = component.getSwitchType();
        return SwitchType.ajax.equals(mode) || null == mode;
    }
}

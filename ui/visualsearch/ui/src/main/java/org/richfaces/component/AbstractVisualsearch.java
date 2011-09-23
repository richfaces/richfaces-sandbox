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

package org.richfaces.component;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Event;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Signature;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.event.FacetSuggestionEvent;
import org.richfaces.component.event.FacetSuggestionListener;
import org.richfaces.component.event.SearchEvent;
import org.richfaces.component.event.ValueSuggestionEvent;
import org.richfaces.component.event.ValueSuggestionListener;
import org.richfaces.context.ExtendedPartialViewContext;
import org.richfaces.renderkit.html.VisualsearchRenderer;

import javax.el.MethodExpression;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import java.util.List;
import java.util.Map;

@JsfComponent(tag = @Tag(name = "visualsearch", handler = "org.richfaces.view.facelets.html.VisualsearchTagHandler", generate = true, type = TagType.Facelets),
    fires = {@Event(value = FacetSuggestionEvent.class, listener = FacetSuggestionListener.class),
        @Event(value = ValueSuggestionEvent.class, listener = ValueSuggestionListener.class)},
    renderer = @JsfRenderer(family = AbstractVisualsearch.COMPONENT_FAMILY, type = VisualsearchRenderer.RENDERER_TYPE),
    attributes = {"ajax-props.xml"})
public abstract class AbstractVisualsearch extends UIInput {
// ------------------------------ FIELDS ------------------------------

    public static final String COMPONENT_FAMILY = "org.richfaces.Visualsearch";

    public static final String COMPONENT_TYPE = "org.richfaces.Visualsearch";

    public static final String SWITCH_TYPE_AJAX = "ajax";

    public static final String SWITCH_TYPE_CLIENT = "client";

    public static final String _DEFAULT_SWITCH_TYPE = SWITCH_TYPE_AJAX;

// -------------------------- OTHER METHODS --------------------------

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException
    {
        if (event instanceof FacetSuggestionEvent) {
            super.broadcast(event);
            FacesContext facesContext = getFacesContext();
            MethodExpression expression = getFacetSuggestionListener();
            if (expression != null) {
                setResponseData(expression.invoke(facesContext.getELContext(), new Object[]{event}));
            }
        } else if (event instanceof ValueSuggestionEvent) {
            super.broadcast(event);
            FacesContext facesContext = getFacesContext();
            MethodExpression expression = getValueSuggestionListener();
            if (expression != null) {
                setResponseData(expression.invoke(facesContext.getELContext(), new Object[]{event}));
            }
        } else if (event instanceof SearchEvent) {
            super.broadcast(event);
            FacesContext facesContext = getFacesContext();
            MethodExpression expression = getSearchListener();
            if (expression != null) {
                expression.invoke(facesContext.getELContext(), new Object[]{event});
            }
        } else {
            super.broadcast(event);
        }
    }

    @Attribute(signature = @Signature(parameters = FacetSuggestionEvent.class))
    public abstract MethodExpression getFacetSuggestionListener();

    @Attribute(events = @EventName(value = "search", defaultEvent = true))
    public abstract String getOnsearch();

    @Attribute
    public abstract String getQuery();

    @Attribute(signature = @Signature(parameters = SearchEvent.class))
    public abstract MethodExpression getSearchListener();

    @Attribute(defaultValue = "SwitchType." + _DEFAULT_SWITCH_TYPE,
        suggestedValue = SWITCH_TYPE_AJAX + "," + SWITCH_TYPE_CLIENT)
    public abstract SwitchType getSwitchType();

    /**
     * Use this to pass predefined facet suggestions (FacetSuggestionListener will not be used).
     *
     * @return list of facet suggestions
     */
    @Attribute
    public abstract List<String> getFacetSuggestions();

    /**
     * Use this to pass predefined value suggestions (ValueSuggestionListener will not be used).
     *
     * @return map of value suggestions where key is facet name and value is list of values for that facet
     */
    @Attribute
    public abstract Map<String, List<String>> getValueSuggestions();

    @Attribute(signature = @Signature(parameters = ValueSuggestionEvent.class))
    public abstract MethodExpression getValueSuggestionListener();

    private void setResponseData(Object data)
    {
        ExtendedPartialViewContext.getInstance(getFacesContext()).getResponseComponentDataMap().put(getClientId(getFacesContext()), data);
    }
}

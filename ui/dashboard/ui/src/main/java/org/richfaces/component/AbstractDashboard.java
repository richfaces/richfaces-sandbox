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
import org.richfaces.context.ExtendedPartialViewContext;
import org.richfaces.event.PositionChangeEvent;
import org.richfaces.event.PositionChangeListener;
import org.richfaces.renderkit.html.DashboardRenderer;

import javax.el.MethodExpression;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

@JsfComponent(tag = @Tag(name = "dashboard", handler = "org.richfaces.view.facelets.html.DashboardTagHandler", generate = true, type = TagType.Facelets),
    fires = {@Event(value = PositionChangeEvent.class, listener = PositionChangeListener.class)},
    renderer = @JsfRenderer(family = AbstractDashboard.COMPONENT_FAMILY, type = DashboardRenderer.RENDERER_TYPE),
    attributes = {"ajax-props.xml", "core-props.xml"})
public abstract class AbstractDashboard extends UIComponentBase {
// ------------------------------ FIELDS ------------------------------

    public static final String COMPONENT_FAMILY = "org.richfaces.Dashboard";

    public static final String COMPONENT_TYPE = "org.richfaces.Dashboard";

    public static final String SWITCH_TYPE_AJAX = "ajax";

    public static final String _DEFAULT_SWITCH_TYPE = SWITCH_TYPE_AJAX;

// -------------------------- OTHER METHODS --------------------------

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException
    {
        if (event instanceof PositionChangeEvent) {
            super.broadcast(event);
            FacesContext facesContext = getFacesContext();
            MethodExpression expression = getPositionChangeListener();
            if (expression != null) {
                setResponseData(expression.invoke(facesContext.getELContext(), new Object[]{event}));
            }
        } else {
            super.broadcast(event);
        }
    }

    @Attribute(events = @EventName(value = "beforechange"))
    public abstract String getOnbeforechange();

    @Attribute(events = @EventName(value = "change", defaultEvent = true))
    public abstract String getOnchange();

    @Attribute
    public abstract String getPlaceholderClass();

    @Attribute(signature = @Signature(parameters = PositionChangeEvent.class))
    public abstract MethodExpression getPositionChangeListener();

    @Attribute(defaultValue = "SwitchType." + _DEFAULT_SWITCH_TYPE,
        suggestedValue = SWITCH_TYPE_AJAX)
    public abstract SwitchType getSwitchType();

    @Attribute(defaultValue = "true")
    public abstract boolean isEnabled();

    @Attribute(defaultValue = "true")
    public abstract boolean isForcePlaceholderSize();

    private void setResponseData(Object data)
    {
        ExtendedPartialViewContext.getInstance(getFacesContext()).getResponseComponentDataMap().put(getClientId(getFacesContext()), data);
    }
}

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

import org.ajax4jsf.component.AjaxOutput;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.renderkit.html.NotifyMessagesRenderer;

import javax.faces.component.UIMessages;

@JsfComponent(tag = @Tag(name = "notifyMessages", type = TagType.Facelets),
    renderer = @JsfRenderer(family = AbstractNotifyMessages.COMPONENT_FAMILY, type = NotifyMessagesRenderer.RENDERER_TYPE),
    attributes = {"ajax-props.xml"})
public abstract class AbstractNotifyMessages extends UIMessages implements AjaxOutput, NotifyAttributes {
// ------------------------------ FIELDS ------------------------------

    public static final String COMPONENT_FAMILY = "org.richfaces.Notify";

    public static final String COMPONENT_TYPE = "org.richfaces.NotifyMessages";

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface AjaxOutput ---------------------

    @Attribute(defaultValue = "true")
    public abstract boolean isAjaxRendered();

    @Attribute
    public abstract boolean isKeepTransient();

// -------------------------- OTHER METHODS --------------------------

    @Attribute
    public abstract Integer getInterval();
}

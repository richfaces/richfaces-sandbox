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
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.renderkit.html.ContextmenuRenderer;

/**
 * We extend from AbstractDropDownMenu because AbstractMenuItem copies value of mode from parent only if it's AbstractDropDownMenu.
 */
@JsfComponent(tag = @Tag(name = "contextmenu", handler = "org.richfaces.view.facelets.html.contextmenuTagHandler", generate = true, type = TagType.Facelets),
    renderer = @JsfRenderer(family = AbstractContextmenu.COMPONENT_FAMILY, type = ContextmenuRenderer.RENDERER_TYPE),
    attributes = {"ajax-props.xml", "core-props.xml"})
public abstract class AbstractContextmenu extends AbstractDropDownMenu {
// ------------------------------ FIELDS ------------------------------

    public static final String COMPONENT_FAMILY = "org.richfaces.ContextMenu";

    public static final String COMPONENT_TYPE = "org.richfaces.ContextMenu";

// -------------------------- OTHER METHODS --------------------------

    @Attribute
    public abstract String getAttachTo();

    @Attribute(hidden = true)
    public abstract Positioning getDirection();

    @Attribute(hidden = true)
    public abstract int getHideDelay();

    @Attribute(hidden = true)
    public abstract int getHorizontalOffset();

    @Attribute(hidden = true)
    public abstract Positioning getJointPoint();

    @Attribute(hidden = true)
    public abstract String getLabel();

    @Attribute(hidden = true)
    public abstract String getOngrouphide();

    @Attribute(hidden = true)
    public abstract String getOngroupshow();

    @Attribute(hidden = true)
    public abstract String getOnhide();

    @Attribute(hidden = true)
    public abstract String getOnitemclick();

    @Attribute(hidden = true)
    public abstract String getOnshow();

    @Attribute(hidden = true)
    public abstract int getPopupWidth();

    @Attribute(hidden = true)
    public abstract int getShowDelay();

    @Attribute(hidden = true)
    public abstract String getShowEvent();

    @Attribute(hidden = true)
    public abstract int getVerticalOffset();

    @Attribute(hidden = true)
    public abstract boolean isDisabled();
}

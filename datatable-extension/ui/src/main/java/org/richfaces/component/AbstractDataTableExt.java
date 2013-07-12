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

import javax.faces.component.UIComponent;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Facet;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;

/**
 * Base cla ss for the component.
 */
@JsfComponent(type = AbstractDataTableExt.COMPONENT_TYPE,
        family = AbstractDataTableExt.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = "org.richfaces.DataTableExtRenderer"),
        tag = @Tag(name = "dataTable", handler = "org.richfaces.taglib.DataTableHandler",type = TagType.Facelets),
        attributes = {
        "style-prop.xml", "styleClass-prop.xml", "iteration-props.xml", "rows-prop.xml", "sequence-props.xml",
        "events-row-props.xml" })
public abstract class AbstractDataTableExt extends UIDataTableBase {
    public static final String COMPONENT_FAMILY = "org.richfaces.DataTableExtFamily";
    public static final String COMPONENT_TYPE = "org.richfaces.DataTableExt";
    public static final String CAPTION_FACET_NAME = "caption";

    /**
     * Assigns one or more space-separated CSS class names to the component caption
     */
    @Attribute
    public abstract String getCaptionClass();

    @Attribute
    public abstract String getStyleClass();

    @Facet
    public abstract UIComponent getCaption();

}

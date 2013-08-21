/**
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
 **/
package org.richfaces.bootstrap.ui.pickList;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.ui.select.AbstractSelectManyComponent;

/**
 *  Base class for the pickList component
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@JsfComponent(
        type = AbstractPickList.COMPONENT_TYPE,
        family = AbstractPickList.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = PickListRendererBase.RENDERER_TYPE),
        tag = @Tag(name="pickList"),
        attributes = {"events-mouse-props.xml", "events-key-props.xml", "multiselect-props.xml"})
abstract public class AbstractPickList extends AbstractSelectManyComponent {
    public static final String COMPONENT_FAMILY = "org.richfaces.bootstrap.PickList";
    public static final String COMPONENT_TYPE = "org.richfaces.bootstrap.PickList";

    /**
     * The text placed above the list of items
     */
    @Attribute
    public abstract String getCaption();

    @Attribute(events = @EventName(value = "change", defaultEvent = true))
    public abstract String getOnchange();

}

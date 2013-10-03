/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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

package org.richfaces.sandbox.select.orderingList;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.ui.attribute.EventsKeyProps;
import org.richfaces.ui.attribute.EventsMouseProps;
import org.richfaces.ui.attribute.MultiSelectProps;
import org.richfaces.ui.select.AbstractOrderingComponent;
import org.richfaces.ui.select.SelectItemsInterface;

/**
 * <p>The &lt;r:orderingList&gt; is a component for ordering items in a list (client-side).</p>
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@JsfComponent(type = AbstractOrderingList.COMPONENT_TYPE, family = AbstractOrderingList.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = "org.richfaces.select.OrderingListRenderer"), tag = @Tag(name = "orderingList"))
public abstract class AbstractOrderingList extends AbstractOrderingComponent implements SelectItemsInterface, EventsKeyProps, EventsMouseProps, MultiSelectProps {
    public static final String COMPONENT_TYPE = "org.richfaces.select.OrderingList";
    public static final String COMPONENT_FAMILY = "org.richfaces.SelectMany";


    public Object getItemValues() {
        return getValue();
    }

    /**
     * Value to be returned to the server if the corresponding option is selected by the user.
     */
    @Attribute()
    public abstract Object getItemValue();

    /**
     * Label to be displayed to the user for the corresponding option.
     */
    @Attribute()
    public abstract Object getItemLabel();

    /**
     * The text placed above the list of items
     */
    @Attribute
    public abstract String getCaption();

    //-------- List Events

    /**
     * Javascript code executed when a pointer button is clicked over the list element .
     */
    @Attribute(events = @EventName("listclick"))
    public abstract String getOnlistclick();

    /**
     * Javascript code executed when a pointer button is double clicked over the list element .
     */
    @Attribute(events = @EventName("listdblclick"))
    public abstract String getOnlistdblclick();

    /**
     * Javascript code executed when a pointer button is pressed down over the list element .
     */
    @Attribute(events = @EventName("listmousedown"))
    public abstract String getOnlistmousedown();

    /**
     * Javascript code executed when a pointer button is released over the list element .
     */
    @Attribute(events = @EventName("listmouseup"))
    public abstract String getOnlistmouseup();

    /**
     * Javascript code executed when a pointer button is moved onto the list element .
     */
    @Attribute(events = @EventName("listmouseover"))
    public abstract String getOnlistmouseover();

    /**
     * Javascript code executed when a pointer button is moved within the list element .
     */
    @Attribute(events = @EventName("listmousemove"))
    public abstract String getOnlistmousemove();

    /**
     * Javascript code executed when a pointer button is moved away from the list element .
     */
    @Attribute(events = @EventName("listmouseout"))
    public abstract String getOnlistmouseout();

    /**
     * Javascript code executed when a key is pressed and released over the list element .
     */
    @Attribute(events = @EventName("listkeypress"))
    public abstract String getOnlistkeypress();

    /**
     * Javascript code executed when a key is pressed down over the list element .
     */
    @Attribute(events = @EventName("listkeydown"))
    public abstract String getOnlistkeydown();

    /**
     * Javascript code executed when a key is released over the list element .
     */
    @Attribute(events = @EventName("listkeyup"))
    public abstract String getOnlistkeyup();

}
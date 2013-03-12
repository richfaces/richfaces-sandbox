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
package org.richfaces.bootstrap.ui.orderingList;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.component.AbstractSelectManyComponent;
import org.richfaces.component.util.SelectItemsInterface;

/**
 *  Base class for the orderingList component
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@JsfComponent(
        type = AbstractOrderingList.COMPONENT_TYPE,
        family = AbstractOrderingList.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = OrderingListRendererBase.RENDERER_TYPE),
        tag = @Tag(name="orderingList"),
        attributes = {"events-mouse-props.xml", "events-key-props.xml", "multiselect-props.xml"})
abstract public class AbstractOrderingList extends AbstractSelectManyComponent implements SelectItemsInterface {
    public static final String COMPONENT_FAMILY = "org.richfaces.bootstrap.OrderingList";
    public static final String COMPONENT_TYPE = "org.richfaces.bootstrap.OrderingList";

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
    
    /**
     * CSS class(es) to be applied to the caption.
     */
    @Attribute
    public abstract String getCaptionStyleClass();

    @Attribute(events = @EventName(value = "change", defaultEvent = true))
    public abstract String getOnchange();
    
    /**
     * CSS class(es) to be applied to the selected items.
     */
    @Attribute
    public abstract String getSelectedItemStyleClass();
    
    /**
     * CSS class(es) to be applied to the items of the list.
     */
    @Attribute
    public abstract String getItemStyleClass();
    
    /**
     * CSS class(es) to be applied to the buttons.
     */
    @Attribute
    public abstract String getButtonsStyleClass();
    
    /**
     * If "true" multiple items can be selected by dragging, as opposed to Ctrl+click. Default: "false".
     */
    @Attribute
    public abstract boolean isDragSelect();
    
    /**
     * If "true" the buttons are hidden. Default: "false".
     */
    @Attribute
    public abstract boolean isHideButtons();
    
    /**
     * If "true" the items cannot be dragged. Default: "false".
     */
    @Attribute
    public abstract boolean isDisableMouse();
    
    /**
     * If "true" the items cannot be dragged outside of the list. Default: "true".
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isContained();
    
    /**
     * CSS class(es) to be applied to the placeholder - the empty space below the dragged item.
     */
    @Attribute
    public abstract String getPlaceholderStyleClass();
    
    /**
     * CSS class(es) to be applied to the helper - the dragged item(s).
     */
    @Attribute
    public abstract String getHelperStyleClass();
}

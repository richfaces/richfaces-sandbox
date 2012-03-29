/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.richfaces.sandbox.component;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;

/**
 * <p>The &lt;rich:accordion&gt; is a series of panels stacked on top of each other, each collapsed such that only the
 * header of the panel is showing. When the header of a panel is clicked, it is expanded to show the content of the
 * panel. Clicking on a different header will collapse the previous panel and epand the selected one. Each panel
 * contained in a &lt;rich:accordion&gt; component is a &lt;rich:accordionItem&gt; component.</p>
 *
 * @author akolonitsky
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets, handler = "org.richfaces.sandbox.view.facelets.html.TogglePanelTagHandler"),
        renderer = @JsfRenderer(type = "org.richfaces.sandbox.AccordionRenderer"), attributes = {"events-mouse-props.xml", "i18n-props.xml", "core-props.xml"})
public abstract class AbstractAccordion extends AbstractTogglePanel {
    public static final String COMPONENT_TYPE = "org.richfaces.sandbox.Accordion";
    public static final String COMPONENT_FAMILY = "org.richfaces.sandbox.Accordion";

    protected AbstractAccordion() {
        setRendererType("org.richfaces.sandbox.AccordionRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    /**
     * Holds the active tab name. This name is a reference to the name identifier of the active child &lt;rich:tab&gt;
     * component.
     */
    @Override
    @Attribute
    public String getActiveItem() {
        String res = super.getActiveItem();
        Object rowKey = getRowKey();
        try {
            if (res == null) {
                res = getFirstItem().getName();
            } else {
                AbstractTogglePanelTitledItem item = (AbstractTogglePanelTitledItem) super.getItemByIndex(super.getChildIndex(res));
                if (item==null || item.isDisabled()) {
                    res = getFirstItem().getName();
                }
            }
        } finally {
            setRowKey(rowKey);
        }
        return res;
    }

    // ------------------------------------------------ Html Attributes

    /**
     * The icon displayed on the left of the panel header when the panel is active
     */
    @Attribute
    public abstract String getItemActiveLeftIcon();

    /**
     * The icon displayed on the left of the panel header when the panel is not active
     */
    @Attribute
    public abstract String getItemInactiveLeftIcon();

    /**
     * The icon displayed on the left of the panel header when the panel is disabled
     */
    @Attribute
    public abstract String getItemDisabledLeftIcon();

    /**
     * The icon displayed on the right of the panel header when the panel is active
     */
    @Attribute
    public abstract String getItemActiveRightIcon();

    /**
     * The icon displayed on the right of the panel header when the panel is not active
     */
    @Attribute
    public abstract String getItemInactiveRightIcon();

    /**
     * The icon displayed on the right of the panel header when the panel is disabled
     */
    @Attribute
    public abstract String getItemDisabledRightIcon();

    /**
     * The width of the panel
     */
    @Attribute
    public abstract String getWidth();

    /**
     * The height of the panel
     */
    @Attribute
    public abstract String getHeight();

    /**
     * The CSS class applied to the panel header when the panel is active
     */
    @Attribute
    public abstract String getItemActiveHeaderClass();

    /**
     * The CSS class applied to the panel header when the panel is disabled
     */
    @Attribute
    public abstract String getItemDisabledHeaderClass();

    /**
     * The CSS class applied to the panel header when the panel is not active
     */
    @Attribute
    public abstract String getItemInactiveHeaderClass();

    /**
     * A CSS class applied to each of the accordionItem children
     */
    @Attribute
    public abstract String getItemContentClass();

    /**
     * The CSS class applied to the panel header
     */
    @Attribute
    public abstract String getItemHeaderClass();

    /**
     * Points to the function to perform when the switchable item is changed.
     */
    @Attribute(events = @EventName(value = "itemchange", defaultEvent = true))
    public abstract String getOnitemchange();

    /**
     *  Points to the function to perform when before the switchable item is changed
     */
    @Attribute(events = @EventName("beforeitemchange"))
    public abstract String getOnbeforeitemchange();
}

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
import org.richfaces.sandbox.renderkit.html.DivPanelRenderer;

/**
 * @author akolonitsky
 * @since 2010-08-05
 */
public interface AbstractTogglePanelTitledItem extends AbstractTogglePanelItemInterface {
    public enum HeaderStates {
        active("act"),
        inactive("inact"),
        disabled("dis");
        private String abbreviation;

        HeaderStates(String abbreviation) {
            this.abbreviation = abbreviation;
        }

        public String abbreviation() {
            return abbreviation;
        }

        public String headerClass() {
            return new StringBuilder("header").append(DivPanelRenderer.capitalize(this.toString())).append("Class").toString();
        }
    }

    // ------------------------------------------------ Component Attributes

    /**
     * Flag indicating whether toggling of this panel is disabled
     */
    @Attribute
    boolean isDisabled();

    String getHeader();

    // ------------------------------------------------ Html Attributes

    /**
     * The CSS class applied to the panel content
     */
    @Attribute
    String getContentClass();

    // ------------------------------------------------ Header Attributes

    /**
     * The CSS class applied to the header when this panel is active
     */
    @Attribute
    String getHeaderActiveClass();

    /**
     * The CSS class applied to the header when this panel is disabled
     */
    @Attribute
    String getHeaderDisabledClass();

    /**
     * The CSS class applied to the header when this panel is inactive
     */
    @Attribute
    String getHeaderInactiveClass();

    /**
     * The CSS class applied to the header
     */
    @Attribute
    String getHeaderClass();

    /**
     * The CSS style applied to the header
     */
    @Attribute
    String getHeaderStyle();

    /**
     * Javascript code executed when a pointer button is clicked over the header of this element.
     */
    @Attribute(events = @EventName("headerclick"))
    String getOnheaderclick();

    /**
     * Javascript code executed when a pointer button is double clicked over the header of this element.
     */
    @Attribute(events = @EventName("headerdblclick"))
    String getOnheaderdblclick();

    /**
     * Javascript code executed when a pointer button is pressed down over the header of this element.
     */
    @Attribute(events = @EventName("headermousedown"))
    String getOnheadermousedown();

    /**
     * Javascript code executed when a pointer button is moved within the header of this element.
     */
    @Attribute(events = @EventName("headermousemove"))
    String getOnheadermousemove();

    /**
     * Javascript code executed when a pointer button is released over the header of this element.
     */
    @Attribute(events = @EventName("headermouseup"))
    String getOnheadermouseup();
}

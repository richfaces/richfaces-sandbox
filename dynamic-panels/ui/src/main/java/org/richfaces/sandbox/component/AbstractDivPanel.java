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
import org.richfaces.cdk.annotations.Description;
import org.richfaces.cdk.annotations.EventName;

/**
 * @author akolonitsky
 * @version 1.0
 *
 */
public interface AbstractDivPanel {
    // -------- i18n-props.xml
    @Attribute
    String getLang();

    @Attribute
    String getDir();

    // -------- core-props.xml
    @Attribute
    String getTitle();

    @Attribute
    String getStyle();

    @Attribute
    String getStyleClass();

    // -------- events-mouse-props.xml
    @Attribute(events = @EventName("click"))
    String getOnclick();

    @Attribute(events = @EventName("dblclick"))
    String getOndblclick();

    @Attribute(events = @EventName("mousedown"))
    String getOnmousedown();

    @Attribute(events = @EventName("mousemove"))
    String getOnmousemove();

    @Attribute(events = @EventName("mouseout"))
    String getOnmouseout();

    @Attribute(events = @EventName("mouseover"))
    String getOnmouseover();

    @Attribute(events = @EventName("mouseup"))
    String getOnmouseup();
}

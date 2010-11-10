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

import java.util.Date;

import javax.faces.component.UIComponentBase;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;

@JsfComponent(tag = @Tag(name = "scheduleItem", type = TagType.Facelets))
public abstract class AbstractScheduleItem extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.richfaces.ScheduleItem";
    public static final String COMPONENT_FAMILY = "org.richfaces.Schedule";
    public static final boolean DEFAULT_ALL_DAY = true;
    public static final boolean DEFAULT_EDITABLE = AbstractSchedule.DEFAULT_EDITABLE;

    @Attribute
    public abstract String getStyleClass();

    @Attribute
    public abstract String getTitle();

    @Attribute
    public abstract Date getStartDate();

    @Attribute
    public abstract Date getEndDate();

    @Attribute
    public abstract String getEventId();

    @Attribute(defaultValue = "" + DEFAULT_ALL_DAY)
    public abstract Boolean isAllDay();

    @Attribute
    public abstract String getUrl();

    @Attribute(defaultValue = "false")
    public abstract Boolean isEditable();

    public abstract Object getData();
}

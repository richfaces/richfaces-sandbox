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

import org.richfaces.cdk.annotations.*;
import org.richfaces.renderkit.html.CarouselRenderer;

@JsfComponent(tag = @Tag(name = "carousel", type = TagType.Facelets),
        renderer = @JsfRenderer(family = AbstractCarousel.COMPONENT_FAMILY, type = CarouselRenderer.RENDERER_TYPE))
public abstract class AbstractCarousel extends UIRepeat {
// ------------------------------ FIELDS ------------------------------

    public static final String COMPONENT_FAMILY = "org.richfaces.Carousel";

    public static final String COMPONENT_TYPE = "org.richfaces.Carousel";

// -------------------------- OTHER METHODS --------------------------

    @Attribute
    public abstract String getChangeOnHover();

    @Attribute
    public abstract String getControl_set_1();

    @Attribute
    public abstract String getControl_set_2();

    @Attribute
    public abstract String getControl_set_3();

    @Attribute
    public abstract String getControl_set_4();

    @Attribute
    public abstract String getControl_set_5();

    @Attribute(defaultValue = "Flavor.flavor_1")
    public abstract Flavor getFlavor();

    @Attribute
    public abstract Integer getHeight();

    @Attribute
    public abstract Integer getInterval();

    @Attribute(defaultValue = "hover_previous_button,hover_next_button")
    public abstract String getNo_control_set();

    @Attribute
    public abstract Integer getSlideHeight();

    @Attribute
    public abstract Integer getSlideWidth();

    @Attribute
    public abstract String getStyle();

    @Attribute
    public abstract String getStyleClass();

    @Attribute
    public abstract Integer getTransitionTime();

    @Attribute(defaultValue = "TransitionType.slide")
    public abstract TransitionType getTransitionType();

    @Attribute(defaultValue = "1")
    public abstract int getVisibleSlides();


    @Attribute
    public abstract Integer getWidth();

    @Attribute(defaultValue = "false")
    public abstract boolean isContinuousScrolling();

// -------------------------- ENUMERATIONS --------------------------

    public static enum Flavor {
        flavor_1,
        flavor_2,
        flavor_3,
    }

    public static enum TransitionType {
        slide,
        fade
    }
}

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

package org.richfaces.renderkit.html;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractCarousel;
import org.richfaces.javascript.JSObject;
import org.richfaces.model.DataVisitResult;
import org.richfaces.model.DataVisitor;
import org.richfaces.renderkit.RendererBase;
import org.richfaces.ui.common.HtmlConstants;

@JsfRenderer(family = AbstractCarousel.COMPONENT_FAMILY, type = CarouselRenderer.RENDERER_TYPE)
@ResourceDependencies({@ResourceDependency(library = "javax.faces", name = "jsf.js"), @ResourceDependency(name = "jquery.js", target = "head"),
        @ResourceDependency(name = "richfaces.js", target = "head"),
        @ResourceDependency(name = "richfaces-base-component.js", target = "head"),
        @ResourceDependency(name = "agile_carousel.alpha.js", target = "head"),
        @ResourceDependency(name = "richfaces.carousel.js", target = "head"), @ResourceDependency(name = "agile_carousel.css", target = "head"),
        @ResourceDependency(name = "agile_carousel_flavors.css", target = "head")})
public class CarouselRenderer extends RendererBase {
// ------------------------------ FIELDS ------------------------------

    public static final String RENDERER_TYPE = "org.richfaces.CarouselRenderer";

    private static final Map<String, Object> DEFAULTS;

// -------------------------- STATIC METHODS --------------------------

    static {
        Map<String, Object> defaults = new HashMap<String, Object>();
        defaults.put("continuousScrolling", false);
        defaults.put("visibleSlides", 1);
        defaults.put("interval", 4000);
        defaults.put("transitionTime", 300);
        defaults.put("transitionType", "slide");
        DEFAULTS = Collections.unmodifiableMap(defaults);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        if (!(component instanceof AbstractCarousel)) {
            return;
        }
        String clientId = component.getClientId(context);
        writer.startElement(HtmlConstants.SCRIPT_ELEM, null);
        writer.writeAttribute(HtmlConstants.TYPE_ATTR, "text/javascript", "type");
        final Map<String, Object> options = getOptions((AbstractCarousel) component);
        writer.writeText(new JSObject("RichFaces.ui.Carousel", clientId, options), null);
        writer.writeText(";", null);
        writer.endElement(HtmlConstants.SCRIPT_ELEM);
        writer.endElement(HtmlConstants.DIV_ELEM);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    protected void addOptionIfSetAndNotDefault(String optionName, Object value, Map<String, Object> options) {
        if (value != null && !"".equals(value) && !value.equals(DEFAULTS.get(
                optionName)) && !(value instanceof Collection && ((Collection) value).size() == 0) && !(value instanceof Map && ((Map) value).size() == 0)) {
            options.put(optionName, value);
        }
    }

    @Override
    protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        if (!(component instanceof AbstractCarousel)) {
            return;
        }
        AbstractCarousel carousel = (AbstractCarousel) component;
        writer.startElement(HtmlConstants.DIV_ELEM, null);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, getUtils().clientId(context, component), "type");
        String styleClass = "rf-crl" + (carousel.getFlavor() != null ? " " + carousel.getFlavor().name() : "");
        styleClass += carousel.getStyleClass() != null ? " " + carousel.getStyleClass() : "";
        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, styleClass, "class");
        getUtils().encodeAttributesFromArray(context, component, new String[]{"style"});
    }

    @Override
    protected void doEncodeChildren(final ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        final AbstractCarousel repeater = (AbstractCarousel) component;
        if (repeater.getValue() != null) {
            try {
                DataVisitor visitor = new DataVisitor() {
                    public DataVisitResult process(FacesContext context, Object rowKey, Object argument) {
                        repeater.setRowKey(context, rowKey);

                        if (repeater.isRowAvailable()) {
                            if (repeater.getChildCount() > 0) {
                                try {
                                    writer.startElement(HtmlConstants.DIV_ELEM, null);
                                    writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-crl-sl", "class");
                                    for (UIComponent child : repeater.getChildren()) {
                                        child.encodeAll(context);
                                    }
                                    writer.endElement(HtmlConstants.DIV_ELEM);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        return DataVisitResult.CONTINUE;
                    }
                };

                repeater.walk(context, visitor, null);
            } finally {
                repeater.setRowKey(context, null);
            }
        } else {
            for (UIComponent child : component.getChildren()) {
                writer.startElement(HtmlConstants.DIV_ELEM, null);
                writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-crl-sl", "class");
                child.encodeAll(context);
                writer.endElement(HtmlConstants.DIV_ELEM);
            }
        }
    }

    protected Map<String, Object> getOptions(AbstractCarousel carousel) throws IOException {
        /**
         * Include only attributes that are actually set.
         */
        Map<String, Object> options = new HashMap<String, Object>();
        addOptionIfSetAndNotDefault("width", carousel.getWidth(), options);
        addOptionIfSetAndNotDefault("height", carousel.getHeight(), options);
        addOptionIfSetAndNotDefault("slideWidth", carousel.getSlideWidth(), options);
        addOptionIfSetAndNotDefault("slideHeight", carousel.getSlideHeight(), options);
        addOptionIfSetAndNotDefault("continuousScrolling", carousel.isContinuousScrolling(), options);
        addOptionIfSetAndNotDefault("changeOnHover", carousel.getChangeOnHover(), options);
        addOptionIfSetAndNotDefault("control_set_1", carousel.getControl_set_1(), options);
        addOptionIfSetAndNotDefault("control_set_2", carousel.getControl_set_2(), options);
        addOptionIfSetAndNotDefault("control_set_3", carousel.getControl_set_3(), options);
        addOptionIfSetAndNotDefault("control_set_4", carousel.getControl_set_4(), options);
        addOptionIfSetAndNotDefault("control_set_5", carousel.getControl_set_5(), options);
        addOptionIfSetAndNotDefault("no_control_set", carousel.getNo_control_set(), options);
        addOptionIfSetAndNotDefault("visibleSlides", carousel.getVisibleSlides(), options);
        addOptionIfSetAndNotDefault("transitionType", carousel.getTransitionType(), options);
        addOptionIfSetAndNotDefault("transitionTime", carousel.getTransitionTime(), options);
        addOptionIfSetAndNotDefault("interval", carousel.getInterval(), options);
        return options;
    }
}

/*
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
 */

package org.richfaces.renderkit;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.DateTimeConverter;

import org.richfaces.component.AbstractCalendar;
import org.richfaces.component.util.SelectUtils;
import org.richfaces.component.util.ViewUtil;

/**
 * @author amarkhel
 * 
 */

@ResourceDependencies({ @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(name = "jquery.position.js"),
        @ResourceDependency(name = "richfaces-event.js"),
        @ResourceDependency(library = "org.richfaces", name = "json-dom.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.effects.core.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.effects.highlight.js"),
        @ResourceDependency(library = "org.richfaces", name = "JQuerySpinBtn.js.js"),
        @ResourceDependency(library = "org.richfaces", name = "calendar-utils.js"),
        @ResourceDependency(library = "org.richfaces", name = "calendar.js"),
        @ResourceDependency(library = "org.richfaces", name = "calendar.ecss") })
public class CalendarRendererBase extends InputRendererBase {
    
    protected static final Map<String, ComponentAttribute> CALENDAR_INPUT_HANDLER_ATTRIBUTES = Collections.unmodifiableMap(ComponentAttribute.createMap(
            new ComponentAttribute(HtmlConstants.ONCLICK_ATTRIBUTE)
                    .setEventNames("inputclick")
                    .setComponentAttributeName("oninputclick"),
            new ComponentAttribute(HtmlConstants.ONDBLCLICK_ATTRIBUTE)
                    .setEventNames("inputdblclick")
                    .setComponentAttributeName("oninputdblclick"),
            new ComponentAttribute(HtmlConstants.ONMOUSEDOWN_ATTRIBUTE)
                    .setEventNames("inputmousedown")
                    .setComponentAttributeName("oninputmousedown"),
            new ComponentAttribute(HtmlConstants.ONMOUSEUP_ATTRIBUTE)
                    .setEventNames("inputmouseup")
                    .setComponentAttributeName("oninputmouseup"),
            new ComponentAttribute(HtmlConstants.ONMOUSEOVER_ATTRIBUTE)
                    .setEventNames("inputmouseover")
                    .setComponentAttributeName("oninputmouseover"),
            new ComponentAttribute(HtmlConstants.ONMOUSEMOVE_ATTRIBUTE)
                    .setEventNames("inputmousemove")
                    .setComponentAttributeName("oninputmousemove"),
            new ComponentAttribute(HtmlConstants.ONMOUSEOUT_ATTRIBUTE)
                    .setEventNames("inputmouseout")
                    .setComponentAttributeName("oninputmouseout"),
            new ComponentAttribute(HtmlConstants.ONKEYPRESS_ATTRIBUTE)
                    .setEventNames("inputkeypress")
                    .setComponentAttributeName("oninputkeypress"),
            new ComponentAttribute(HtmlConstants.ONKEYDOWN_ATTRIBUTE)
                    .setEventNames("inputkeydown")
                    .setComponentAttributeName("oninputkeydown"),
            new ComponentAttribute(HtmlConstants.ONKEYUP_ATTRIBUTE)
                    .setEventNames("inputkeyup")
                    .setComponentAttributeName("oninputkeyup"),
            new ComponentAttribute(HtmlConstants.ONBLUR_ATTRIBUTE)
                    .setEventNames("inputblur")
                    .setComponentAttributeName("oninputblur"),
            new ComponentAttribute(HtmlConstants.ONFOCUS_ATTRIBUTE)
                    .setEventNames("inputfocus")
                    .setComponentAttributeName("oninputfocus"),
            new ComponentAttribute(HtmlConstants.ONCHANGE_ATTRIBUTE)
                    .setEventNames("inputchange")
                    .setComponentAttributeName("oninputchange"),
            new ComponentAttribute(HtmlConstants.ONSELECT_ATTRIBUTE)
                    .setEventNames("inputselect")
                    .setComponentAttributeName("oninputselect")));


    public void renderInputHandlers(FacesContext facesContext, UIComponent component) throws IOException {
        RenderKitUtils.renderPassThroughAttributesOptimized(facesContext, component, CALENDAR_INPUT_HANDLER_ATTRIBUTES);
    }

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }

        // skip conversion of already converted date
        if (submittedValue instanceof Date) {
            return (Date) submittedValue;
        }

        // Store submitted value in the local variable as a string
        String newValue = (String) submittedValue;
        // if we have no local value, try to get the valueExpression.
        AbstractCalendar calendar = (AbstractCalendar) component;
        Converter converter = SelectUtils.getConverterForProperty(context, calendar, "value"); 

        // in case the converter hasn't been set, try to use default 
        // DateTimeConverter
        if (converter == null) {
            converter = createDefaultConverter();
        }
        setupDefaultConverter(converter, calendar);

        return converter.getAsObject(context, component, newValue);
    }
    
    public String getButtonIcon(FacesContext facesContext, UIComponent component) {
        boolean disable  = (Boolean)component.getAttributes().get("disable");
        String buttonIcon = (String)component.getAttributes().get("buttonIcon");
        if(disable) {
            buttonIcon = (String)component.getAttributes().get("buttonIconDisabled");
        } 
        //TODO:  add default icon 
        return (buttonIcon != null && !"".equals(buttonIcon)) ? getResourcePath(facesContext, buttonIcon) : "";
    }
    
    protected String getResourcePath(FacesContext facesContext, String source) {
        return (source != null && !"".equals(source)) ? ViewUtil.getResourceURL(source, facesContext) :"" ;         
    }
    
    public boolean isUseIcons(FacesContext facesContext, UIComponent component) {
        Object label = component.getAttributes().get("buttonLabel");
        return (label == null || ((String)label).trim().length() == 0);        
    }
    
    protected static Converter createDefaultConverter() {
        return new DateTimeConverter();
    }
    
    protected static Converter setupDefaultConverter(Converter converter, AbstractCalendar calendar) {
        // skip id converter is null
        if(converter == null) {
            return null;
        }
        
        if(converter instanceof DateTimeConverter) {
            DateTimeConverter defaultConverter = (DateTimeConverter) converter;
            defaultConverter.setPattern(calendar.getDatePattern());
            defaultConverter.setLocale(calendar.getAsLocale(calendar.getLocale()));
            defaultConverter.setTimeZone(calendar.getTimeZone());
        }
        
        return converter;
    }
}
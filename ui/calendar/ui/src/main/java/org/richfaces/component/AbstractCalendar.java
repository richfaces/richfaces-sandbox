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


package org.richfaces.component;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.faces.component.UIInput;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;

/**
 * @author amarkhel
 *
 */

@JsfComponent(
        type = AbstractCalendar.COMPONENT_TYPE,
        family = AbstractCalendar.COMPONENT_FAMILY, 
        generate = "org.richfaces.component.UICalendar",
        renderer = @JsfRenderer(type = "org.richfaces.CalendarRenderer"),
        tag = @Tag(name="calendar")
)
public abstract class AbstractCalendar extends UIInput{

    public static final String COMPONENT_TYPE = "org.richfaces.Calendar";

    public static final String COMPONENT_FAMILY = "org.richfaces.Calendar";
    
    private static final String DEFAULT_TIME_PATTERN = "HH:mm";

    
    @Attribute(defaultValue=DEFAULT_TIME_PATTERN)
    public abstract String getDatePattern();

    @Attribute
    public abstract Object getLocale();
    
    @Attribute
    public abstract TimeZone getTimeZone();
    
    @Attribute
    public abstract boolean isDisabled();
    
    @Attribute
    public abstract boolean isShowInput();
    
    @Attribute
    public abstract boolean isPopup();
    
    @Attribute
    public abstract boolean isEnableManualInput();
    
    @Attribute
    public abstract String getTabindex();
    
    @Attribute
    public abstract String getInputStyle();

    @Attribute
    public abstract String getButtonClass();

    @Attribute
    public abstract String getInputClass();
    
    @Attribute
    public abstract String getButtonLabel();
        
    @Attribute
    public abstract String getInputSize();
    
    @Attribute
    public abstract Object getCurrentDate();
    
    @Attribute
    public abstract String getButtonIcon();
    
    @Attribute
    public abstract String getButtonIconDisabled();
            
    @Attribute(events=@EventName("inputclick"))
    public abstract String getOninputclick();

    @Attribute(events=@EventName("inputdblclick"))
    public abstract String getOninputdblclick();

    @Attribute(events=@EventName("inputchange"))
    public abstract String getOninputchange();
    
    @Attribute(events=@EventName("inputselect"))
    public abstract String getOninputselect();
    
    @Attribute(events=@EventName("inputmousedown"))
    public abstract String getOninputmousedown();
    
    @Attribute(events=@EventName("inputmousemove"))
    public abstract String getOninputmousemove();
    
    @Attribute(events=@EventName("inputmouseout"))
    public abstract String getOninputmouseout();
    
    @Attribute(events=@EventName("inputmouseover"))
    public abstract String getOninputmouseover();
    
    @Attribute(events=@EventName("inputmouseup"))
    public abstract String getOninputmouseup();
    
    @Attribute(events=@EventName("inputkeydown"))
    public abstract String getOninputkeydown();
    
    @Attribute(events=@EventName("inputkeypress"))
    public abstract String getOninputkeypress();
    
    @Attribute(events=@EventName("inputkeyup"))
    public abstract String getOninputkeypup();
    
    @Attribute(events=@EventName("inputfocus"))
    public abstract String getOninputfocus();
  
    @Attribute(events=@EventName("inputblur"))
    public abstract String getOninputblur();
    
    public Date getCurrentDateOrDefault() {
        /*
        Date date = getAsDate(getCurrentDate());

        if (date != null) {
            return date;
        } else {
            Date value = getAsDate(this.getValue());
            if (value != null) {
                return value;
            } else {
                return java.util.Calendar.getInstance(getTimeZone()).getTime();
            }

        }
        */
        return null;
    }

    public Date getAsDate(Object date) {
        /*

        if (date == null) {
            return null;
        } else {
            
                if (date instanceof Date) {
                return (Date) date;
            } else {
                    if (date instanceof String) {
                    DateTimeConverter converter = new DateTimeConverter();
                    converter.setPattern(this.getDatePattern());
                    converter.setLocale(getAsLocale(this.getLocale()));
                    converter.setTimeZone(this.getTimeZone());
                    FacesContext context = FacesContext.getCurrentInstance();
                    return (Date) converter.getAsObject(context, this,
                            (String) date);
                } else {
                    if (date instanceof Calendar) {
                        return ((Calendar) date).getTime();
                    } else {
                            
                            FacesContext context = FacesContext.getCurrentInstance();
                            Converter converter = getConverter();
                            
                            if(converter != null) {
                                return getAsDate(converter.getAsString(context, this, date));
                            }
                            
                        Application application = context.getApplication();
                        converter = application.createConverter(date.getClass());
                        if (null != converter) {
                            return getAsDate(converter.getAsString(context, this, date));
                        } else {
                            throw new FacesException("Wrong attibute type or there is no converter for custom attibute type");
                        }

                    }
                }
            }
        }
        */
        return null;
    }
    
    
    public Locale getAsLocale(Object locale) {
        /*
        if (locale instanceof Locale) {
            return (Locale) locale;

        } else if (locale instanceof String) {

            return parseLocale((String) locale);

        } else {

            FacesContext context = FacesContext.getCurrentInstance();
            Application application = context.getApplication();
            Converter converter = application .createConverter(locale.getClass());
            if (null != converter) {
                return parseLocale(converter.getAsString(context, this, locale));
            } else {
                throw new FacesException(
                        "Wrong locale attibute type or there is no converter for custom attibute type");
            }
        }*/
        return null;
    }

}

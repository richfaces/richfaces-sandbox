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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;

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
    public abstract boolean isDayEnabled();
    
    @Attribute
    public abstract String getDayStyleClass();
            
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
    
    @Attribute
    public abstract Object getDefaultTime();
            
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
    
    public Calendar getCalendar() {
        return Calendar.getInstance(getTimeZone(), getAsLocale(getLocale()));
    }
    
    public Date getCurrentDateOrDefault() {
        Date date = getAsDate(getCurrentDate());
        if (date == null) {
            date = getAsDate(this.getValue());
            
            if (date == null) {
                TimeZone timeZone = getTimeZone();
                Calendar calendar =  timeZone != null ? Calendar.getInstance(timeZone) : Calendar.getInstance();
                date = calendar.getTime();
            }
            
        }
        return date;
    }

    public Date getAsDate(Object date) {
        if(date == null) {
            return null;
        }

        Date value = null;
        FacesContext facesContext = getFacesContext();
        if(date instanceof Date) {
            value = (Date)date;
            
        } else if(date instanceof String) {
            DateTimeConverter converter = new DateTimeConverter();
            converter.setPattern(this.getDatePattern());
            converter.setLocale(getAsLocale(this.getLocale()));
            converter.setTimeZone(this.getTimeZone());
            value = (Date)converter.getAsObject(facesContext, this,(String) date);
            
        } else if(date instanceof Calendar) {
            value = ((Calendar) date).getTime();

        } else {
            Converter converter = getConverter();
            if(converter != null) {
                return getAsDate(converter.getAsString(facesContext, this, date));
            }
            
            Application application = facesContext.getApplication();
            converter = application.createConverter(date.getClass());
            if (null != converter) {
                value = getAsDate(converter.getAsString(facesContext, this, date));
            } else {
                throw new FacesException("Wrong attibute type or there is no converter for custom attibute type");
            }
            
        }
        
        return value;
    }
    
    
    public Locale getAsLocale(Object locale) {
        Locale localeValue = null; 
        if (locale instanceof Locale) {
            localeValue = (Locale)locale;

        } else if (locale instanceof String) {
            localeValue = parseLocale((String) locale);

        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            Application application = context.getApplication();
            Converter converter = application .createConverter(locale.getClass());
            
            if (null != converter) {
                localeValue = parseLocale(converter.getAsString(context, this, locale));
            } else {
                throw new FacesException("Wrong locale attibute type or there is no converter for custom attibute type");
            }
        }
        return localeValue;
    }
    
    public Locale parseLocale(String localeStr){
        if(null==localeStr || localeStr.trim().length() < 2) {
            return Locale.getDefault();
        }
        
        //Lookup index of first '_' in string locale representation.
        int index1 = localeStr.indexOf("_");
        //Get first charters (if exist) from string
        String language = null; 
        if(index1!=-1){
            language = localeStr.substring(0, index1);
        }else{
            return new Locale(localeStr);
        }
        
        //Lookup index of second '_' in string locale representation.
        int index2 = localeStr.indexOf("_", index1+1);
        String country = null;
        if(index2!=-1){
            country = localeStr.substring(index1+1, index2);
            String variant = localeStr.substring(index2+1);
            return new Locale(language, country, variant);
        }else{
            country = localeStr.substring(index1+1);
            return new Locale(language, country);
        }       
    }
    
    public Date getFormattedDefaultTime() {
        Object defaultTime = getDefaultTime();

        if(defaultTime == null) {
            return null;
        }
        
        Date result = null;
        if (defaultTime instanceof Calendar) {
            result = ((Calendar) defaultTime).getTime();
            
        } else if (defaultTime instanceof Date) {
            result = (Date) defaultTime;
            
        } else {
            String defaultTimeString = defaultTime.toString();
            String datePattern = getDatePattern();
            
            String timePattern = "\\s*[hHkKma]+[\\W&&\\S]+[hHkKma]+\\s*";
            Pattern pattern = Pattern.compile(timePattern);
            Matcher matcher = pattern.matcher(datePattern);
            
            String subTimePattern = DEFAULT_TIME_PATTERN;
            if(matcher.find()) {
                subTimePattern = matcher.group().trim();
            }
            
            DateFormat format = new SimpleDateFormat(subTimePattern);
            try {
                result = format.parse(defaultTimeString);
            } catch (ParseException parseException) {
                // log??
                result = null;
            }
        }
        
        return result;
    }
}

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
import javax.faces.component.UIViewRoot;
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
    
    public static final String SUB_TIME_PATTERN = "\\s*[hHkKma]+[\\W&&\\S]+[hHkKma]+\\s*";
    
    public static final String TIME_PATTERN = "HH:mm";
    
    public static final String DEFAULT_DATE_PATTERN = "MMM d, YYYY";
    

    @Attribute(defaultValue="MMM d, YYYY")
    public abstract String getDatePattern();

    @Attribute(defaultValue="getDefaultLocale()")
    public abstract Object getLocale();
    
    @Attribute(defaultValue="TimeZone.getDefault()")
    public abstract TimeZone getTimeZone();
    
    @Attribute(defaultValue="Integer.MIN_VALUE")
    public abstract int getFirstWeekDay();
    
    @Attribute(defaultValue="Integer.MIN_VALUE")
    public abstract int getMinDaysInFirstWeek();
    
    @Attribute(defaultValue="select")
    public abstract String getTodayControlMode();
        
    @Attribute(defaultValue="true")
    public abstract boolean isShowWeekDaysBar();
    
    @Attribute(defaultValue="true")
    public abstract boolean isShowWeeksBar();
    
    @Attribute(defaultValue="true")
    public abstract boolean isShowFooter();
    
    @Attribute(defaultValue="true")
    public abstract boolean isShowHeader();
    
    @Attribute(defaultValue="true")
    public abstract boolean isShowInput();
    
    @Attribute(defaultValue="true")
    public abstract boolean isPopup();
    
    @Attribute(defaultValue="true")
    public abstract String getHidePopupOnScroll();
    
    @Attribute(defaultValue="false")
    public abstract boolean isDisable();
        
    @Attribute(defaultValue="false")
    public abstract boolean isEnableManualInput();
    
    @Attribute(defaultValue="false")
    public abstract boolean isDayEnabled();
    
    @Attribute(defaultValue="false") 
    public abstract boolean isShowApplyButton();
    
    @Attribute(defaultValue="false") 
    public abstract boolean isResetTimeOnDateSelect();
    
    @Attribute(defaultValue="bottom-left")
    public abstract String getJointPoint();
    
    @Attribute(defaultValue="bottom-right")
    public abstract String getDirection();
    
    @Attribute(defaultValue="inactive")
    public abstract String getBoundaryDatesMode();
    
    @Attribute(defaultValue="0")
    public abstract String getHorizontalOffset();
    
    @Attribute(defaultValue="0")
    public abstract String getVerticalOffsetOffset();
    
    @Attribute(defaultValue="3")
    public abstract int getZindex();
    
    @Attribute
    public abstract String getStyle();
    
    @Attribute
    public abstract Object getMonthLabels();
    
    @Attribute
    public abstract Object getMonthLabelsShort();
    
    @Attribute
    public abstract Object getWeekDayLabelsShort();
    
    @Attribute
    public abstract Object getWeekDayLabels();

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
        return Calendar.getInstance(getTimeZone(), getAsLocale());
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
            converter.setLocale(getAsLocale());
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
    
    public Locale getAsLocale() {
        Object locale = getLocale();
        return getAsLocale(locale);
    }
    
    protected Locale getDefaultLocale() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            UIViewRoot viewRoot = facesContext.getViewRoot();
            if (viewRoot != null) {
                Locale locale = viewRoot.getLocale();
                if (locale != null) {
                    return locale;
                }
            }
        }
        return Locale.US;
    }

    public Locale getAsLocale(Object locale) {
        
        if(locale == null) {
            return null;
        }
        
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
            
            String timePattern = TIME_PATTERN;
            Pattern pattern = Pattern.compile(timePattern);
            Matcher matcher = pattern.matcher(datePattern);
            
            String subTimePattern = SUB_TIME_PATTERN;
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

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

package org.richfaces.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;

import org.richfaces.component.AbstractCalendar;

/**
 * @author amarkhel
 * 
 */
public final class CalendarHelper {
    
    protected CalendarHelper(){
    }

    public static Date getAsDate(FacesContext facesContext, AbstractCalendar calendar, Object date) {
        if (date == null) {
            return null;
        }

        Date value = null;

        if (date instanceof Date) {
            value = (Date) date;
        } else if (date instanceof String) {
            value = convertStringToDate(facesContext, calendar, (String) date);
        } else if (date instanceof Calendar) {
            value = ((Calendar) date).getTime();
        } else {
            Converter converter = calendar.getConverter();
            if (converter == null) {
                Application application = facesContext.getApplication();
                converter = application.createConverter(date.getClass());
                if (converter == null) {
                    throw new FacesException("Wrong attibute type or there is no converter for custom attibute type");
                }
            }
            value = convertStringToDate(facesContext, calendar, converter.getAsString(facesContext, calendar, date));
        }

        return value;
    }

    public static Object getDefaultValueOfDefaultTime(FacesContext facesContext, AbstractCalendar calendarComponent) {
        if (calendarComponent == null) {
            return null;
        }

        Calendar calendar = getCalendar(facesContext, calendarComponent);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTime();
    }

    public static Date getFormattedDefaultTime(AbstractCalendar calendar) {
        if (calendar == null || calendar.getDefaultTime() == null) {
            return null;

        }
        Object defaultTime = calendar.getDefaultTime();
        Date result = null;

        if (defaultTime instanceof Calendar) {
            result = ((Calendar) defaultTime).getTime();

        } else if (defaultTime instanceof Date) {
            result = (Date) defaultTime;

        } else {
            String defaultTimeString = defaultTime.toString();
            String datePattern = calendar.getDatePattern();

            String timePattern = AbstractCalendar.TIME_PATTERN;
            Pattern pattern = Pattern.compile(timePattern);
            Matcher matcher = pattern.matcher(datePattern);

            String subTimePattern = AbstractCalendar.SUB_TIME_PATTERN;
            if (matcher.find()) {
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

    public static Locale getAsLocale(FacesContext facesContext, AbstractCalendar calendar) {
        if (calendar == null || facesContext == null) {
            return null;
        }

        Object locale = calendar.getLocale();
        return getAsLocale(facesContext, calendar, locale);
    }

    public static Locale getAsLocale(FacesContext facesContext, AbstractCalendar calendar, Object locale) {
        if (calendar == null || facesContext == null) {
            return null;
        }

        Locale localeValue = null;
        if (locale instanceof Locale) {
            localeValue = (Locale) locale;

        } else if (locale instanceof String) {
            localeValue = parseLocale((String) locale);

        } else if (locale != null) {
            Application application = facesContext.getApplication();
            Converter converter = application.createConverter(locale.getClass());
            if (null != converter) {
                localeValue = parseLocale(converter.getAsString(facesContext, calendar, locale));
            } else {
                throw new FacesException("Wrong locale attibute type or there is no converter for custom attibute type");
            }
        }
        return localeValue;
    }

    public static Locale parseLocale(String localeStr) {
        if (null == localeStr || localeStr.trim().length() < 2) {
            return Locale.getDefault();
        }

        // Lookup index of first '_' in string locale representation.
        int index1 = localeStr.indexOf("_");
        // Get first charters (if exist) from string
        String language = null;
        if (index1 != -1) {
            language = localeStr.substring(0, index1);
        } else {
            return new Locale(localeStr);
        }

        // Lookup index of second '_' in string locale representation.
        int index2 = localeStr.indexOf("_", index1 + 1);
        String country = null;
        if (index2 != -1) {
            country = localeStr.substring(index1 + 1, index2);
            String variant = localeStr.substring(index2 + 1);
            return new Locale(language, country, variant);
        } else {
            country = localeStr.substring(index1 + 1);
            return new Locale(language, country);
        }
    }

    public static Date convertCurrentDate(String currentDateString) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.DATE, 1);
        int idx = currentDateString.indexOf('/');

        Date date = null;
        if (idx != -1) {
            calendar.set(Calendar.MONTH, Integer.parseInt(currentDateString.substring(0, idx)) - 1);
            calendar.set(Calendar.YEAR, Integer.parseInt(currentDateString.substring(idx + 1)));
            date = calendar.getTime();
        }
        return date;
    }

    public static Calendar getCalendar(FacesContext facesContext, AbstractCalendar calendar) {
        if (calendar == null || facesContext == null) {
            return Calendar.getInstance();
        }
        return Calendar.getInstance(calendar.getTimeZone(), getAsLocale(facesContext, calendar));
    }

    public static Date convertStringToDate(FacesContext facesContext, AbstractCalendar calendar, String date) {
        DateTimeConverter converter = new DateTimeConverter();
        converter.setPattern(calendar.getDatePattern());
        converter.setLocale(getAsLocale(facesContext, calendar));
        converter.setTimeZone(calendar.getTimeZone());
        return (Date) converter.getAsObject(facesContext, calendar, date);
    }

    public static Date getCurrentDateOrDefault(FacesContext facesContext, AbstractCalendar calendar) {
        if (calendar == null || facesContext == null) {
            return null;
        }

        Date date = getAsDate(facesContext, calendar, calendar.getCurrentDate());
        if (date == null) {
            date = getAsDate(facesContext, calendar, calendar.getValue());
        }

        if (date == null) {
            date = getCalendar(facesContext, calendar).getTime();
        }
        return date;
    }
}

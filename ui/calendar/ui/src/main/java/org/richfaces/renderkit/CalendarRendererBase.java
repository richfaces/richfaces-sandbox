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
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.DateTimeConverter;

import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.javascript.JSReference;
import org.richfaces.component.AbstractCalendar;
import org.richfaces.component.util.HtmlUtil;
import org.richfaces.component.util.MessageUtil;
import org.richfaces.component.util.SelectUtils;
import org.richfaces.component.util.ViewUtil;

/**
 * @author amarkhel
 * 
 */

@ResourceDependencies({ @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(name = "jquery.js"),
        @ResourceDependency(name = "jquery.position.js"),
        @ResourceDependency(name = "richfaces.js"),
        @ResourceDependency(name = "richfaces-base-component.js"),
        @ResourceDependency(name = "richfaces-event.js"),
        @ResourceDependency(library = "org.richfaces", name = "json-dom.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.effects.core.js"),
        @ResourceDependency(library = "org.richfaces", name = "jquery.effects.highlight.js"),
        @ResourceDependency(library = "org.richfaces", name = "JQuerySpinBtn.js.js"),
        @ResourceDependency(library = "org.richfaces", name = "calendar-utils.js"),
        @ResourceDependency(library = "org.richfaces", name = "calendar.js"),
        @ResourceDependency(library = "org.richfaces", name = "calendar.ecss") })
public class CalendarRendererBase extends InputRendererBase {

    public static final String CALENDAR_BUNDLE = "org.richfaces.renderkit.calendar";

    public static final String OPTION_ENABLE_MANUAL_INPUT = "enableManualInput";
    
    public static final String OPTION_DISABLED = "disabled";
    
    public static final String OPTION_READONLY = "readonly";
    
    public static final String OPTION_RESET_TIME_ON_DATE_SELECT = "resetTimeOnDateSelect";
    
    public static final String OPTION_SHOW_APPLY_BUTTON = "showApplyButton";
    
    public static final String OPTION_MIN_DAYS_IN_FIRST_WEEK = "minDaysInFirstWeek";
    
    public static final String OPTION_POPUP = "popup";
    
    public static final String OPTION_SHOW_INPUT = "showInput";
    
    public static final String OPTION_SHOW_HEADER = "showHeader";
    
    public static final String OPTION_SHOW_FOOTER = "showFooter";
    
    public static final String OPTION_SHOW_WEEKS_BAR = "showWeeksBar";
    
    public static final String OPTION_TODAY_CONTROL_MODE = "todayControlMode";
    
    public static final String OPTION_DATE_PATTERN = "datePattern";
    
    public static final String OPTION_JOINT_POINT = "jointPoint";
    
    public static final String OPTION_DIRECTION = "direction";
    
    public static final String OPTION_BOUNDARY_DATES_MODE = "boundaryDatesMode";
    
    public static final String OPTION_HORIZONTAL_OFFSET = "horizontalOffset";

    public static final String OPTION_VERTICAL_OFFSET = "verticalOffset";

    public static final String OPTION_CURRENT_DATE = "currentDate";

    public static final String OPTION_SELECTED_DATE = "selectedDate";

    public static final String OPTION_SUBMIT_FUNCTION = "submitFunction";

    public static final String OPTION_DAY_CELL_CLASS = "dayCellClass";

    public static final String OPTION_DAY_STYLE_CLASS = "dayStyleClass";
    
    public static final String OPTION_LABELS = "labels";
    
    public static final String OPTION_DEFAULT_TIME = "defaultTime";
    
    public static final String MONTH_LABELS_SHORT = "monthLabelsShort";

    public static final String MONTH_LABELS = "monthLabels";

    public static final String WEEK_DAY_LABELS_SHORT = "weekDayLabelsShort";

    public static final String WEEK_DAY_LABELS = "weekDayLabels";
    
    public static final String FIRST_DAY_WEEK = "firstWeekDay";
    
    public static final String MIN_DAYS_IN_FIRST_WEEK = "minDaysInFirstWeek";

    
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

    private static final String HOURS_VALUE = "hours";
    
    private static final String MINUTES_VALUE = "minutes";

    
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
    
    public Object getSelectedDate(FacesContext facesContext, AbstractCalendar calendar) throws IOException {
        Object returnValue = null;
        if(calendar.isValid()) {
            Date date;
            Object value = calendar.getValue();
            date = calendar.getAsDate(value);
            if(date != null) {
                returnValue = formatSelectedDate(calendar.getTimeZone(), date);  
            }
        }
        return returnValue;    
    }
    
    public static Object formatSelectedDate(TimeZone timeZone, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(timeZone);
        calendar.setTime(date);
      
        JSFunction result = new JSFunction("new Date");
        result.addParameter(Integer.valueOf(calendar.get(Calendar.YEAR)));
        result.addParameter(Integer.valueOf(calendar.get(Calendar.MONTH)));
        result.addParameter(Integer.valueOf(calendar.get(Calendar.DATE)));
        result.addParameter(Integer.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
        result.addParameter(Integer.valueOf(calendar.get(Calendar.MINUTE)));
        result.addParameter(new Integer(0));
        
        return result;
    }

    public Object getCurrentDate(FacesContext facesContext, AbstractCalendar calendar) throws IOException {
        Date date = calendar.getCurrentDateOrDefault();
        return formatDate(date);
    }
    
    public static Object formatDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        JSFunction result = new JSFunction("new Date");
        result.addParameter(Integer.valueOf(calendar.get(Calendar.YEAR)));
        result.addParameter(Integer.valueOf(calendar.get(Calendar.MONTH)));
        result.addParameter(Integer.valueOf(calendar.get(Calendar.DATE)));

        return result;
    }
    
    public String getDayCellClass(FacesContext facesContext, AbstractCalendar calendar) {
        //TODO: refactor this
        /*
        String cellwidth = (String) component.getAttributes().get("cellWidth");
        String cellheight = (String) component.getAttributes().get("cellHeight");
        if (cellwidth != null && cellwidth.length() != 0 || cellheight != null
                && cellheight.length() != 0) {
            String clientId = component.getClientId(context);
            String value = clientId.replace(':', '_') + "DayCell";
            return value;
        }
        */
        return null;
    }
    
    public JSReference getIsDayEnabled(FacesContext facesContext, AbstractCalendar calendar) {
        return calendar.isDayEnabled() ? JSReference.TRUE : JSReference.FALSE;
    }
    
    public JSReference getDayStyleClass(FacesContext context, AbstractCalendar calendar) {
        String dayStyleClass = calendar.getDayStyleClass();
        return ((dayStyleClass != null && dayStyleClass.trim().length() != 0)) ? new JSReference(dayStyleClass) : null; 
    }

    public Map<String, Object> getLabels(FacesContext facesContext, AbstractCalendar calendar) {
        ResourceBundle bundle1 = null;
        ResourceBundle bundle2 = null;

        Object locale = calendar.getAsLocale();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        String messageBundle = facesContext.getApplication().getMessageBundle();
        if (null != messageBundle) {
            bundle1 = ResourceBundle.getBundle(messageBundle, calendar.getAsLocale(locale), loader);
        }
        
        try {
            bundle2 = ResourceBundle.getBundle(CALENDAR_BUNDLE, calendar.getAsLocale(locale), loader);
        } catch (MissingResourceException e) {
                //No external bundle was found, ignore this exception.              
        }
        
        ResourceBundle [] bundles = {bundle1, bundle2};
        String [] test = new String []{};
        String[] names = {"apply", "today", "clean", "cancel", "ok", "close"};
        return getCollectedLabels(bundles, names); 
    }
    
    protected Map<String, Object> getCollectedLabels(ResourceBundle [] bundles , String[] names) {
        Map<String, Object> labels = new HashMap<String, Object>();
        if(bundles != null && names != null) {
            for (String name: names) {
                String label = null;
                String bundleKey = "RICH_CALENDAR_" + name.toUpperCase() + "_LABEL";
                for(ResourceBundle bundle: bundles) {
                    if (bundle != null) {
                        try {
                            label = bundle.getString(bundleKey);
                        } catch (MissingResourceException mre) {
                        // Current key was not found, ignore this exception;
                        }
                    }
                    if(label != null) {
                        break;
                    }
                }
                RenderKitUtils.addToScriptHash(labels, name, label);             
            }
        }
        return labels;
    }

    public Object getSubmitFunction(FacesContext context, AbstractCalendar calendar) throws IOException {
        /*
        if (!UICalendar.AJAX_MODE.equals(calendar.getAttributes().get("mode")))
            return null;

        JSFunction ajaxFunction = AjaxRendererUtils.buildAjaxFunction(calendar, context,
                AjaxRendererUtils.AJAX_FUNCTION_NAME);
        ajaxFunction.addParameter(JSReference.NULL);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(calendar.getClientId(context) + CURRENT_DATE_PRELOAD, Boolean.TRUE);

        Map<String, Object> options = AjaxRendererUtils.buildEventOptions(context, calendar, params, true);
        options.put("calendar", JSReference.THIS);

        String oncomplete = AjaxRendererUtils.getAjaxOncomplete(calendar);
        JSFunctionDefinition oncompleteDefinition = new JSFunctionDefinition();
        oncompleteDefinition.addParameter("request");
        oncompleteDefinition.addParameter("event");
        oncompleteDefinition.addParameter("data");
        oncompleteDefinition.addToBody("this.calendar.load(data, true);");
        if (oncomplete != null) {
            oncompleteDefinition.addToBody(oncomplete);
        }

        options.put("oncomplete", oncompleteDefinition);
        JSReference requestValue = new JSReference("requestValue");
        ajaxFunction.addParameter(options);
        JSFunctionDefinition definition = new JSFunctionDefinition();
        definition.addParameter(requestValue);
        definition.addToBody(ajaxFunction);
        return definition;
        */
        
        return null;
    }
    
    public Map<String, Object> getPreparedDefaultTime(AbstractCalendar abstractCalendar) {
        Date date = abstractCalendar.getFormattedDefaultTime();
        Map<String, Object> result = new HashMap<String, Object>();
        if (date != null) {
            Calendar calendar = abstractCalendar.getCalendar();
            calendar.setTime(date);
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            
            if (hours != 12 || minutes != 0) {
                result.put(HOURS_VALUE, hours);
                result.put(MINUTES_VALUE, minutes);
            }
        }
        return result;   
    } 

    private static String[] shiftDates(int minimum, int maximum, String[] labels) {
        if (minimum == 0 && (maximum - minimum == labels.length - 1)) {
            return labels;
        }

        String[] shiftedLabels = new String[maximum - minimum + 1];
        System.arraycopy(labels, minimum, shiftedLabels, 0, maximum - minimum
                + 1);

        return shiftedLabels;
    }
    
    protected Map<String, Object> getLocaleOptions(FacesContext facesContext, AbstractCalendar calendarComponent) {
        Map<String, Object> map = new HashMap<String, Object>();
        
        Locale locale = calendarComponent.getAsLocale();
        DateFormatSymbols dateFormat = new DateFormatSymbols(locale);
        
        Calendar calendar = calendarComponent.getCalendar();
        int maximum = calendar.getActualMaximum(Calendar.DAY_OF_WEEK);
        int minimum = calendar.getActualMinimum(Calendar.DAY_OF_WEEK);

        int monthMax = calendar.getActualMaximum(Calendar.MONTH);
        int monthMin = calendar.getActualMinimum(Calendar.MONTH);

        String [] weekDayLabels = RenderKitUtils.asArray(calendarComponent.getWeekDayLabels());
        if(weekDayLabels == null) {
            weekDayLabels = dateFormat.getWeekdays();
            weekDayLabels = shiftDates(minimum, maximum, weekDayLabels);
        }
        RenderKitUtils.addToScriptHash(map, WEEK_DAY_LABELS, weekDayLabels);

        String [] weekDayLabelsShort = RenderKitUtils.asArray(calendarComponent.getWeekDayLabelsShort());
        if(weekDayLabelsShort == null) {
            weekDayLabelsShort = dateFormat.getShortWeekdays();
            weekDayLabelsShort = shiftDates(minimum, maximum, weekDayLabelsShort);
        }
        RenderKitUtils.addToScriptHash(map, WEEK_DAY_LABELS_SHORT, weekDayLabelsShort);

        String [] monthLabels = RenderKitUtils.asArray(calendarComponent.getMonthLabels());
        if(monthLabels == null) {
            monthLabels = dateFormat.getMonths();
            monthLabels = shiftDates(monthMin, monthMax, monthLabels);
        }
        RenderKitUtils.addToScriptHash(map, MONTH_LABELS, monthLabels);

        
        String [] monthLabelsShort = RenderKitUtils.asArray(calendarComponent.getMonthLabelsShort());
        if(monthLabelsShort == null) {
            monthLabelsShort = dateFormat.getShortMonths();
            monthLabelsShort = shiftDates(monthMin, monthMax, monthLabelsShort);
        }
        RenderKitUtils.addToScriptHash(map, MONTH_LABELS_SHORT, monthLabelsShort);
        
        int minDaysInFirstWeek = calendarComponent.getMinDaysInFirstWeek();
        if(minDaysInFirstWeek == Integer.MIN_VALUE) {
            minDaysInFirstWeek = calendar.getMinimalDaysInFirstWeek();
        }

        if (0 <= minDaysInFirstWeek && minDaysInFirstWeek <= 7) {
            RenderKitUtils.addToScriptHash(map, MIN_DAYS_IN_FIRST_WEEK, minDaysInFirstWeek);
        }
        
        int day = calendarComponent.getFirstWeekDay();
        if(day == Integer.MIN_VALUE) {
            day = calendar.getFirstDayOfWeek();
            day = calendar.getFirstDayOfWeek() - calendar.getActualMinimum(Calendar.DAY_OF_WEEK);
        }
        
        if (0 <= day && day <= 6) {
            RenderKitUtils.addToScriptHash(map, FIRST_DAY_WEEK, day);
        } else if (day != Integer.MIN_VALUE) {
            facesContext.getExternalContext().log(day + " value of firstWeekDay attribute is not a legal one for component: " + MessageUtil.getLabel(facesContext, calendarComponent) + ". Default value was applied.");
        }
        
        return map;
    }

    public ScriptOptions createCalendarScriptOption(FacesContext facesContext, UIComponent component) throws IOException {
        AbstractCalendar calendar = (AbstractCalendar)component;

        ScriptOptions scriptOptions = new ScriptOptions(component);
        
        scriptOptions.addOption(OPTION_ENABLE_MANUAL_INPUT);
        scriptOptions.addOption("disabled");
        scriptOptions.addOption("readonly");
        scriptOptions.addOption("resetTimeOnDateSelect");
        scriptOptions.addOption("showApplyButton");
        scriptOptions.addOption("styleClass");
        scriptOptions.addOption("popup"); 
        scriptOptions.addOption("showInput");
        scriptOptions.addOption("showHeader");
        scriptOptions.addOption("showFooter");
        scriptOptions.addOption("showWeeksBar");
        scriptOptions.addOption("showWeekDaysBar");
        scriptOptions.addOption("todayControlMode");
        scriptOptions.addOption("datePattern");
        scriptOptions.addOption("jointPoint");
        scriptOptions.addOption("direction");
        scriptOptions.addOption("boundaryDatesMode");
        scriptOptions.addOption("horizontalOffset");
        scriptOptions.addOption("verticalOffset");
        scriptOptions.addOption("hidePopupOnScroll");
        
        scriptOptions.addOption("currentDate", getCurrentDate(facesContext, calendar));
        scriptOptions.addOption("selectedDate", getSelectedDate(facesContext, calendar));
        scriptOptions.addOption("style", HtmlUtil.concatStyles("z-index: " + calendar.getZindex(), calendar.getStyle()));
        scriptOptions.addOption("submitFunction", getSubmitFunction(facesContext, calendar));
        scriptOptions.addOption("dayCellClass", getDayCellClass(facesContext, calendar));
        scriptOptions.addOption("dayStyleClass", getDayStyleClass(facesContext, calendar));
        /*
         *add to script option 
         *<cdk:scriptOption attributes="ondateselected, ondateselect, ontimeselect, ontimeselected, onchanged, ondatemouseover, ondatemouseout, onexpand, oncollapse, oncurrentdateselect, oncurrentdateselected" wrapper="eventHandler" />
         * */
        scriptOptions.addOption("labels", getLabels(facesContext, calendar));
        scriptOptions.addOption("defaultTime", getPreparedDefaultTime(calendar));
       
        return scriptOptions;
    }
    
    public void buildLocaleScript(ResponseWriter writer, FacesContext facesContext, UIComponent component) throws IOException {
        AbstractCalendar calendar = (AbstractCalendar)component;
        JSFunction function = new JSFunction("RichFaces.ui.Calendar.addLocale", calendar.getAsLocale(), getLocaleOptions(facesContext, calendar));
        writer.write(function.toScript());
        writer.write(";");
    }
    
    public void buildScript(ResponseWriter writer, FacesContext facesContext, UIComponent component) throws IOException {
        AbstractCalendar calendar = (AbstractCalendar)component;

        ScriptOptions scriptOptions = createCalendarScriptOption(facesContext, calendar);
        JSFunction function = new JSFunction("new RichFaces.ui.Calendar", calendar.getClientId(facesContext), calendar.getAsLocale(), scriptOptions, "");
        StringBuffer scriptBuffer = new StringBuffer(); 
        scriptBuffer.append(function.toScript()).append(".load();");
        writer.write(scriptBuffer.toString());
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
            defaultConverter.setLocale(calendar.getAsLocale());
            defaultConverter.setTimeZone(calendar.getTimeZone());
        }
        
        return converter;
    }
    
}
package org.richfaces.renderkit;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.richfaces.component.AbstractSchedule;
import org.richfaces.component.AbstractScheduleAgendaDayView;
import org.richfaces.component.AbstractScheduleAgendaWeekView;
import org.richfaces.component.AbstractScheduleBasicDayView;
import org.richfaces.component.AbstractScheduleBasicWeekView;
import org.richfaces.component.AbstractScheduleMonthView;
import org.richfaces.component.ScheduleCommonViewAttributes;
import org.richfaces.component.event.ScheduleDateRangeChangeEvent;
import org.richfaces.component.event.ScheduleDateRangeSelectEvent;
import org.richfaces.component.event.ScheduleDateSelectEvent;
import org.richfaces.component.event.ScheduleItemMoveEvent;
import org.richfaces.component.event.ScheduleItemResizeEvent;
import org.richfaces.component.event.ScheduleItemSelectEvent;
import org.richfaces.component.event.ScheduleViewChangeEvent;
import org.richfaces.javascript.JSFunction;
import org.richfaces.javascript.JSFunctionDefinition;
import org.richfaces.javascript.JSObject;
import org.richfaces.javascript.JSReference;
import org.richfaces.javascript.ScriptString;
import org.richfaces.ui.common.AjaxFunction;
import org.richfaces.ui.common.SwitchType;
import org.richfaces.util.AjaxRendererUtils;
import org.richfaces.util.RendererUtils;

@ResourceDependencies({
    @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
    @ResourceDependency(name = "richfaces-event.js"),
    @ResourceDependency(library = "com.jqueryui", name = "jquery.ui.core.js"),
    @ResourceDependency(library = "com.jqueryui", name = "jquery.ui.widget.js"),
    @ResourceDependency(library = "com.jqueryui", name = "jquery.ui.mouse.js"),
    @ResourceDependency(library = "com.jqueryui", name = "jquery.ui.draggable.js"),
    @ResourceDependency(library = "com.jqueryui", name = "jquery.ui.droppable.js"),
    @ResourceDependency(library = "com.jqueryui", name = "jquery.ui.resizable.js"),
    @ResourceDependency(library = "com.arshaw/fullcalendar", name = "fullcalendar.js"),
    @ResourceDependency(library = "org.richfaces", name = "richfaces.schedule.js"),
    @ResourceDependency(library = "org.richfaces", name = "richfaces.schedule.css"),
    @ResourceDependency(name = "org.richfaces.renderkit.html.scripts.ScheduleMessages"),
    @ResourceDependency(library = "com.arshaw/fullcalendar", name = "fullcalendar.css")})
public abstract class ScheduleRendererBase extends RendererBase {
// ------------------------------ FIELDS ------------------------------

    public static final String DATE_RANGE_CHANGED_EVENT = "dateRangeChange";

    public static final String DATE_RANGE_SELECTED_EVENT = "dateRangeSelect";

    public static final String DATE_SELECTED_EVENT = "dateSelect";

    public static final String ITEM_MOVE_EVENT = "itemMove";

    public static final String ITEM_RESIZE_EVENT = "itemResize";

    public static final String ITEM_SELECTED_EVENT = "itemSelect";

    public static final String RENDERER_TYPE = "org.richfaces.ScheduleRenderer";

    public static final String VIEW_CHANGED_EVENT = "viewChange";

    private static final String ALL_DAY_PARAM = "allDay";

    private static final String CALLBACK = "callback";

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final String DAY_DELTA_PARAM = "dayDelta";

    private static final Map<String, Object> DEFAULTS;

    private static final String END_DATE_PARAM = "endDate";

    private static final String EVENT_TYPE_PARAM = "eventType";

    private static final String ITEM_ID_PARAM = "itemId";

    private static final String MINUTE_DELTA_PARAM = "minuteDelta";

    private static final String START_DATE_PARAM = "startDate";

    private static final String VIEW_PARAM = "view";

// -------------------------- STATIC METHODS --------------------------

    /**
     * Following defaults are be used by addOptionIfSetAndNotDefault
     */
    static {
        Map<String, Object> defaults = new HashMap<String, Object>();
        defaults.put("eventColor", "");
        defaults.put("eventBackgroundColor", "");
        defaults.put("eventBorderColor", "");
        defaults.put("eventTextColor", "");
        defaults.put("styleClass", "");
        defaults.put("defaultView", AbstractSchedule.DEFAULT_VIEW);
        defaults.put("firstDay", AbstractSchedule.DEFAULT_FIRST_DAY - 1);
        defaults.put("isRTL", AbstractSchedule.DEFAULT_RTL);
        defaults.put("weekends", AbstractSchedule.DEFAULT_SHOW_WEEKENDS);
        defaults.put("weekMode", AbstractSchedule.DEFAULT_WEEK_MODE);
        defaults.put("aspectRatio", AbstractSchedule.DEFAULT_ASPECT_RATIO);
        defaults.put("allDaySlot", AbstractSchedule.DEFAULT_ALL_DAY_SLOT);
        defaults.put("axisFormat", AbstractSchedule.DEFAULT_AXIS_FORMAT);
        defaults.put("slotMinutes", AbstractSchedule.DEFAULT_SLOT_MINUTES);
        defaults.put("defaultEventMinutes", AbstractSchedule.DEFAULT_EVENT_MINUTES);
        defaults.put("firstHour", AbstractSchedule.DEFAULT_FIRST_HOUR);
        defaults.put("minTime", AbstractSchedule.DEFAULT_MIN_TIME);
        defaults.put("maxTime", AbstractSchedule.DEFAULT_MAX_TIME);
        defaults.put("editable", AbstractSchedule.DEFAULT_EDITABLE);
        defaults.put("selectable", AbstractSchedule.DEFAULT_SELECTABLE);
        defaults.put("selectHelper", AbstractSchedule.DEFAULT_SELECT_HELPER);
        defaults.put("unselectAuto", AbstractSchedule.DEFAULT_UNSELECT_AUTO);
        defaults.put("unselectCancel", AbstractSchedule.DEFAULT_UNSELECT_CANCEL);
        defaults.put("disableDragging", AbstractSchedule.DEFAULT_DISABLE_DRAGGING);
        defaults.put("disableResizing", AbstractSchedule.DEFAULT_DISABLE_RESIZING);
        defaults.put("dragRevertDuration", AbstractSchedule.DEFAULT_DRAG_REVERT_DURATION);
        defaults.put("allDayDefault", AbstractSchedule.DEFAULT_ALL_DAY_DEFAULT);
        defaults.put("autoRefreshOnDateRangeSelect", AbstractSchedule.DEFAULT_AUTO_REFRESH_ON_DATE_RANGE_SELECT);
        defaults.put("onbeforeitemselect", "");
        defaults.put("onitemselect", "");
        defaults.put("onbeforeitemdrop", "");
        defaults.put("onitemdrop", "");
        defaults.put("onbeforeitemresize", "");
        defaults.put("onitemresize", "");
        defaults.put("onitemresizestart", "");
        defaults.put("onitemresizestop", "");
        defaults.put("onitemdragstart", "");
        defaults.put("onitemdragstop", "");
        defaults.put("onitemmouseover", "");
        defaults.put("onitemmouseout", "");
        defaults.put("onviewchange", "");
        defaults.put("onviewdisplay", "");
        defaults.put("onbeforedateselect", "");
        defaults.put("ondateselect", "");
        defaults.put("onbeforedaterangeselect", "");
        defaults.put("ondaterangeselect", "");
        defaults.put("ondaterangechange", "");
        defaults.put("showCurrentTimeline", false);
        DEFAULTS = Collections.unmodifiableMap(defaults);
    }

    private static void copyAttribute(String attribute, String suffix, UIComponent source, UIComponent target) {
        Object value = source.getAttributes().get(attribute);
        if (value != null) {
            target.getAttributes().put(attribute + suffix, value);
        }
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public void decode(FacesContext context, UIComponent component) {
        if (!component.isRendered()) {
            return;
        }
        Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        if (requestParameterMap.get(component.getClientId(context)) != null) {
            String startDateParam = requestParameterMap.get(getFieldId(context, (AbstractSchedule) component,
                START_DATE_PARAM));
            String endDateParam = requestParameterMap.get(getFieldId(context, (AbstractSchedule) component,
                END_DATE_PARAM));
            String itemIdParam = requestParameterMap.get(getFieldId(context, (AbstractSchedule) component,
                ITEM_ID_PARAM));
            String dayDeltaParam = requestParameterMap.get(getFieldId(context, (AbstractSchedule) component,
                DAY_DELTA_PARAM));
            String minuteDeltaParam = requestParameterMap.get(getFieldId(context, (AbstractSchedule) component,
                MINUTE_DELTA_PARAM));
            String allDayParam = requestParameterMap.get(getFieldId(context, (AbstractSchedule) component,
                ALL_DAY_PARAM));
            String eventTypeParam = requestParameterMap.get(getFieldId(context, (AbstractSchedule) component,
                EVENT_TYPE_PARAM));
            String viewParam = requestParameterMap.get(getFieldId(context, (AbstractSchedule) component,
                VIEW_PARAM));

            try {
                if (DATE_RANGE_CHANGED_EVENT.equals(eventTypeParam)) {
                    Date startDate = DATE_FORMAT.parse(startDateParam);
                    Date endDate = DATE_FORMAT.parse(endDateParam);
                    new ScheduleDateRangeChangeEvent(component, startDate, endDate).queue();
                } else if (ITEM_MOVE_EVENT.equals(eventTypeParam)) {
                    int dayDelta = Integer.parseInt(dayDeltaParam);
                    int minuteDelta = Integer.parseInt(minuteDeltaParam);
                    boolean allDay = Boolean.parseBoolean(allDayParam);
                    new ScheduleItemMoveEvent(component, itemIdParam, dayDelta, minuteDelta, allDay).queue();
                } else if (ITEM_RESIZE_EVENT.equals(eventTypeParam)) {
                    int dayDelta = Integer.parseInt(dayDeltaParam);
                    int minuteDelta = Integer.parseInt(minuteDeltaParam);
                    new ScheduleItemResizeEvent(component, itemIdParam, dayDelta, minuteDelta).queue();
                } else if (ITEM_SELECTED_EVENT.equals(eventTypeParam)) {
                    new ScheduleItemSelectEvent(component, itemIdParam).queue();
                } else if (VIEW_CHANGED_EVENT.equals(eventTypeParam)) {
                    new ScheduleViewChangeEvent(component, viewParam).queue();
                } else if (DATE_SELECTED_EVENT.equals(eventTypeParam)) {
                    Date startDate = DATE_FORMAT.parse(startDateParam);
                    boolean allDay = Boolean.parseBoolean(allDayParam);
                    new ScheduleDateSelectEvent(component, startDate, allDay).queue();
                } else if (DATE_RANGE_SELECTED_EVENT.equals(eventTypeParam)) {
                    Date startDate = DATE_FORMAT.parse(startDateParam);
                    Date endDate = DATE_FORMAT.parse(endDateParam);
                    boolean allDay = Boolean.parseBoolean(allDayParam);
                    new ScheduleDateRangeSelectEvent(component, startDate, endDate, allDay).queue();
                }
            } catch (ParseException ex) {
                throw new FacesException("Cannot convert request parmeters", ex);
            }
        }
    }

    private void addOptionHash(String attribute, UIComponent source, Map<String, Object> options) {
        Map<String, Object> hash = new HashMap<String, Object>(3);
        Map<String, Object> attributes = source.getAttributes();
        addOptionIfSetAndNotDefault("month", attributes.get(attribute + "Month"), hash);
        addOptionIfSetAndNotDefault("basicWeek", attributes.get(attribute + "BasicWeek"), hash);
        addOptionIfSetAndNotDefault("agendaWeek", attributes.get(attribute + "AgendaWeek"), hash);
        addOptionIfSetAndNotDefault("basicDay", attributes.get(attribute + "BasicDay"), hash);
        addOptionIfSetAndNotDefault("agendaDay", attributes.get(attribute + "AgendaDay"), hash);
        addOptionIfSetAndNotDefault("", attributes.get(attribute), hash);
        if (hash.size() > 0) {
            options.put(attribute, hash);
        }
    }

    protected void addOptionIfSetAndNotDefault(String optionName, Object value, Map<String, Object> options) {
        if (value != null && !"".equals(value) && !value.equals(DEFAULTS.get(optionName))) {
            options.put(optionName, value);
        }
    }

    protected Object createSubmitEventFunction(FacesContext context, AbstractSchedule component) {
        ScriptString jsFunction;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(getFieldId(context, component, START_DATE_PARAM), new JSReference(START_DATE_PARAM));
        params.put(getFieldId(context, component, END_DATE_PARAM), new JSReference(END_DATE_PARAM));
        params.put(getFieldId(context, component, ITEM_ID_PARAM), new JSReference(ITEM_ID_PARAM));
        params.put(getFieldId(context, component, DAY_DELTA_PARAM), new JSReference(DAY_DELTA_PARAM));
        params.put(getFieldId(context, component, MINUTE_DELTA_PARAM), new JSReference(MINUTE_DELTA_PARAM));
        params.put(getFieldId(context, component, ALL_DAY_PARAM), new JSReference(ALL_DAY_PARAM));
        params.put(getFieldId(context, component, EVENT_TYPE_PARAM), new JSReference(EVENT_TYPE_PARAM));
        params.put(getFieldId(context, component, VIEW_PARAM), new JSReference(VIEW_PARAM));
        String clientId = component.getClientId();
        params.put(clientId, clientId);
        if (isAjaxMode(component)) {
            AjaxFunction ajaxFunction = AjaxRendererUtils.buildAjaxFunction(context, component);
            ajaxFunction.getOptions().getParameters().putAll(params);
            ajaxFunction.getOptions().set("complete", new JSReference(CALLBACK));
            jsFunction = ajaxFunction;
        } else if (SwitchType.server.equals(component.getSwitchType())) {
            jsFunction = new JSFunction("RichFaces.submitForm", "#"
                + RendererUtils.getInstance().getNestingForm(component).getClientId(context),
                params,
                "");
        } else {
            return null;
        }
        return new JSFunctionDefinition("event", VIEW_PARAM, EVENT_TYPE_PARAM, ITEM_ID_PARAM, START_DATE_PARAM,
            END_DATE_PARAM, DAY_DELTA_PARAM, MINUTE_DELTA_PARAM, ALL_DAY_PARAM, CALLBACK).addToBody(jsFunction);
    }

    protected String getFieldId(FacesContext context, AbstractSchedule component, String attribute) {
        return RendererUtils.getInstance().clientId(context, component) + UINamingContainer.getSeparatorChar(context)
            + attribute;
    }

    protected Map<String, Object> getOptions(AbstractSchedule schedule) throws IOException {
        /**
         * Copy attributes from child view components
         */
        for (UIComponent child : schedule.getChildren()) {
            if (!child.isRendered()) {
                continue;
            }
            String suffix = "";
            if (child instanceof AbstractScheduleMonthView) {
                copyAttribute("weekMode", "", child, schedule);
                suffix = "Month";
            } else if (child instanceof AbstractScheduleAgendaDayView) {
                suffix = "AgendaDay";
            } else if (child instanceof AbstractScheduleAgendaWeekView) {
                suffix = "AgendaWeek";
            } else if (child instanceof AbstractScheduleBasicDayView) {
                suffix = "BasicDay";
            } else if (child instanceof AbstractScheduleBasicWeekView) {
                suffix = "BasicWeek";
            }
            if (child instanceof ScheduleCommonViewAttributes) {
                copyAttribute("timeFormat", suffix, child, schedule);
                copyAttribute("columnFormat", suffix, child, schedule);
                copyAttribute("titleFormat", suffix, child, schedule);
                copyAttribute("dragOpacity", suffix, child, schedule);
            }
        }
        /**
         * Include only attributes that are actually set.
         */
        Map<String, Object> options = new HashMap<String, Object>();
        addOptionIfSetAndNotDefault("defaultView", schedule.getView(), options);
        /**
         * firstDayOfWeek numeration in Calendar (sunday=1,monday=2,etc.) and in widget(sunday=0,monday=1,etc.)
         */
        Integer firstDay = schedule.getFirstDay();
        if (firstDay != null) {
            addOptionIfSetAndNotDefault("firstDay", firstDay - 1, options);
        }
        addOptionIfSetAndNotDefault("isRTL", schedule.getRTL(), options);
        addOptionIfSetAndNotDefault("weekends", schedule.getShowWeekends(), options);
        addOptionIfSetAndNotDefault("weekMode", schedule.getWeekMode(), options);
        addOptionIfSetAndNotDefault("height", schedule.getHeight(), options);
        addOptionIfSetAndNotDefault("contentHeight", schedule.getContentHeight(), options);
        addOptionIfSetAndNotDefault("aspectRatio", schedule.getAspectRatio(), options);
        addOptionIfSetAndNotDefault("allDaySlot", schedule.getAllDaySlot(), options);
        addOptionIfSetAndNotDefault("allDayText", schedule.getAllDayText(), options);
        addOptionIfSetAndNotDefault("axisFormat", schedule.getAxisFormat(), options);
        addOptionIfSetAndNotDefault("slotMinutes", schedule.getSlotMinutes(), options);
        addOptionIfSetAndNotDefault("defaultEventMinutes", schedule.getDefaultEventMinutes(), options);
        addOptionIfSetAndNotDefault("firstHour", schedule.getFirstHour(), options);
        addOptionIfSetAndNotDefault("minTime", schedule.getMinTime(), options);
        addOptionIfSetAndNotDefault("maxTime", schedule.getMaxTime(), options);
        addOptionIfSetAndNotDefault("editable", schedule.getEditable(), options);
        addOptionIfSetAndNotDefault("selectable", schedule.getSelectable(), options);
        addOptionIfSetAndNotDefault("selectHelper", schedule.getSelectHelper(), options);
        addOptionIfSetAndNotDefault("unselectAuto", schedule.getUnselectAuto(), options);
        addOptionIfSetAndNotDefault("unselectCancel", schedule.getUnselectCancel(), options);
        addOptionIfSetAndNotDefault("disableDragging", schedule.getDisableDragging(), options);
        addOptionIfSetAndNotDefault("disableResizing", schedule.getDisableResizing(), options);
        addOptionIfSetAndNotDefault("dragRevertDuration", schedule.getDragRevertDuration(), options);
        addOptionIfSetAndNotDefault("eventColor", schedule.getEventColor(), options);
        addOptionIfSetAndNotDefault("eventBackgroundColor", schedule.getEventBackgroundColor(), options);
        addOptionIfSetAndNotDefault("eventBorderColor", schedule.getEventBorderColor(), options);
        addOptionIfSetAndNotDefault("eventTextColor", schedule.getEventTextColor(), options);
        addOptionIfSetAndNotDefault("showCurrentTimeline", schedule.isShowCurrentTimeline(), options);
        addOptionHash("dragOpacity", schedule, options);
        addOptionHash("titleFormat", schedule, options);
        addOptionHash("timeFormat", schedule, options);
        addOptionHash("columnFormat", schedule, options);
        Map<String, Object> headerOptions = new HashMap<String, Object>(3);
        addOptionIfSetAndNotDefault("left", schedule.getHeaderLeft(), headerOptions);
        addOptionIfSetAndNotDefault("center", schedule.getHeaderCenter(), headerOptions);
        addOptionIfSetAndNotDefault("right", schedule.getHeaderRight(), headerOptions);
        if (headerOptions.size() > 0) {
            options.put("header", headerOptions);
        }
        addOptionIfSetAndNotDefault("allDayDefault", schedule.getAllDayByDefault(), options);
        addOptionIfSetAndNotDefault("autoRefreshOnDateRangeSelect", schedule.isAutoRefreshOnDateRangeSelect(), options);

        addOptionIfSetAndNotDefault("onbeforeitemselect", schedule.getOnbeforeitemselect(), options);
        addOptionIfSetAndNotDefault("onitemselect", schedule.getOnitemselect(), options);
        addOptionIfSetAndNotDefault("onbeforeitemdrop", schedule.getOnbeforeitemdrop(), options);
        addOptionIfSetAndNotDefault("onitemdrop", schedule.getOnitemdrop(), options);
        addOptionIfSetAndNotDefault("onbeforeitemresize", schedule.getOnbeforeitemresize(), options);
        addOptionIfSetAndNotDefault("onitemresize", schedule.getOnitemresize(), options);
        addOptionIfSetAndNotDefault("onitemresizestart", schedule.getOnitemresizestart(), options);
        addOptionIfSetAndNotDefault("onitemresizestop", schedule.getOnitemresizestop(), options);
        addOptionIfSetAndNotDefault("onitemdragstart", schedule.getOnitemdragstart(), options);
        addOptionIfSetAndNotDefault("onitemdragstop", schedule.getOnitemdragstop(), options);
        addOptionIfSetAndNotDefault("onitemmouseover", schedule.getOnitemmouseover(), options);
        addOptionIfSetAndNotDefault("onitemmouseout", schedule.getOnitemmouseout(), options);
        addOptionIfSetAndNotDefault("onviewchange", schedule.getOnviewchange(), options);
        addOptionIfSetAndNotDefault("onviewdisplay", schedule.getOnviewdisplay(), options);
        addOptionIfSetAndNotDefault("onbeforedateselect", schedule.getOnbeforedateselect(), options);
        addOptionIfSetAndNotDefault("ondateselect", schedule.getOndateselect(), options);
        addOptionIfSetAndNotDefault("onbeforedaterangeselect", schedule.getOnbeforedaterangeselect(), options);
        addOptionIfSetAndNotDefault("ondaterangeselect", schedule.getOndaterangeselect(), options);
        addOptionIfSetAndNotDefault("ondaterangechange", schedule.getOndaterangechange(), options);
        if (schedule.getDate() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(schedule.getDate());
            options.put("year", calendar.get(Calendar.YEAR));
            options.put("month", calendar.get(Calendar.MONTH));
            options.put("date", calendar.get(Calendar.DATE));
        }
        if (!isClientMode(schedule)) {
            Map<String, Object> initialItems = new HashMap<String, Object>();
            Date startDate = AbstractSchedule.getFirstDisplayedDay(schedule);
            Date endDate = AbstractSchedule.getLastDisplayedDate(schedule);
            initialItems.put("items", schedule.getScheduleData(startDate, endDate));
            Map<String, Object> date = new HashMap<String, Object>();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            date.put("year", calendar.get(Calendar.YEAR));
            date.put("month", calendar.get(Calendar.MONTH));
            date.put("date", calendar.get(Calendar.DATE));
            initialItems.put("startDate", date);
            date = new HashMap<String, Object>();
            calendar.setTime(endDate);
            date.put("year", calendar.get(Calendar.YEAR));
            date.put("month", calendar.get(Calendar.MONTH));
            date.put("date", calendar.get(Calendar.DATE));
            initialItems.put("endDate", date);
            options.put("initialItems", initialItems);
        } else {
            options.put("events", schedule.getScheduleData(null, null));
        }
        return options;
    }

    protected boolean isAjaxMode(AbstractSchedule component) {
        SwitchType mode = component.getSwitchType();
        return SwitchType.ajax.equals(mode) || null == mode;
    }

    protected boolean isClientMode(AbstractSchedule component) {
        SwitchType mode = component.getSwitchType();
        return SwitchType.client.equals(mode);
    }

    protected void writeInitFunction(FacesContext context, UIComponent component) throws IOException {
        AbstractSchedule schedule = (AbstractSchedule) component;
        ResponseWriter writer = context.getResponseWriter();
        String clientId = schedule.getClientId(context);
        Locale locale = context.getViewRoot().getLocale();
        String widgetVar = schedule.getWidgetVar();
        if (widgetVar != null) {
            writer.writeText("var " + widgetVar + " = ", null);
        }
        final Map<String, Object> options = getOptions(schedule);
        options.put("locale", locale.toString());
        options.put("submitEventFunction", createSubmitEventFunction(context, schedule));
        writer.writeText(new JSObject("RichFaces.ui.Schedule", clientId, options).toScript(), null);
    }
}

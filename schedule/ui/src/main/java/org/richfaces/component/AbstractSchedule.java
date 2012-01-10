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

import org.ajax4jsf.model.DataVisitResult;
import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Description;
import org.richfaces.cdk.annotations.Event;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Signature;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.event.ScheduleDateRangeChangeEvent;
import org.richfaces.component.event.ScheduleDateRangeChangeListener;
import org.richfaces.component.event.ScheduleDateRangeSelectEvent;
import org.richfaces.component.event.ScheduleDateRangeSelectListener;
import org.richfaces.component.event.ScheduleDateSelectEvent;
import org.richfaces.component.event.ScheduleDateSelectListener;
import org.richfaces.component.event.ScheduleItemMoveEvent;
import org.richfaces.component.event.ScheduleItemMoveListener;
import org.richfaces.component.event.ScheduleItemResizeEvent;
import org.richfaces.component.event.ScheduleItemResizeListener;
import org.richfaces.component.event.ScheduleItemSelectEvent;
import org.richfaces.component.event.ScheduleItemSelectListener;
import org.richfaces.component.event.ScheduleViewChangeEvent;
import org.richfaces.component.event.ScheduleViewChangeListener;
import org.richfaces.component.model.DateRange;
import org.richfaces.context.ExtendedPartialViewContext;
import org.richfaces.renderkit.ScheduleRendererBase;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.ResultDataModel;
import javax.faces.model.ResultSetDataModel;
import javax.faces.model.ScalarDataModel;
import javax.servlet.jsp.jstl.sql.Result;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Base class for generation of UISchedule component.
 *
 * @author Bernard Labno
 */
@JsfComponent(tag = @Tag(name = "schedule",
    handler = "org.richfaces.view.facelets.html.ScheduleTagHandler",
    generate = true,
    type = TagType.Facelets),
    renderer = @JsfRenderer(family = AbstractSchedule.COMPONENT_FAMILY, type = ScheduleRendererBase.RENDERER_TYPE),
    fires = {
        @Event(value = ScheduleDateRangeChangeEvent.class, listener = ScheduleDateRangeChangeListener.class),
        @Event(value = ScheduleDateRangeSelectEvent.class, listener = ScheduleDateRangeSelectListener.class),
        @Event(value = ScheduleDateSelectEvent.class, listener = ScheduleDateSelectListener.class),
        @Event(value = ScheduleItemMoveEvent.class, listener = ScheduleItemMoveListener.class),
        @Event(value = ScheduleItemResizeEvent.class, listener = ScheduleItemResizeListener.class),
        @Event(value = ScheduleItemSelectEvent.class, listener = ScheduleItemSelectListener.class),
        @Event(value = ScheduleViewChangeEvent.class, listener = ScheduleViewChangeListener.class)
    }
)
public abstract class AbstractSchedule extends UIComponentBase implements ScheduleCommonViewAttributes {
// ------------------------------ FIELDS ------------------------------

    public static final String COMPONENT_FAMILY = "org.richfaces.Schedule";

    public static final String COMPONENT_TYPE = "org.richfaces.Schedule";

    /**
     * Values of switchType attribute
     */
    public static final String SWITCH_TYPE_AJAX = "ajax";

    public static final String SWITCH_TYPE_CLIENT = "client";

    public static final String SWITCH_TYPE_SERVER = "server";

    public static final String VIEW_AGENDA_DAY = "agendaDay";

    public static final String VIEW_AGENDA_WEEK = "agendaWeek";

    public static final String VIEW_BASIC_DAY = "basicDay";

    public static final String VIEW_BASIC_WEEK = "basicWeek";

    /**
     * Values of view attribute.
     */
    public static final String VIEW_MONTH = "month";

    /**
     * Values of weekMode attribute.
     */
    public static final String WEEK_MODE_FIXED = "fixed";

    public static final String WEEK_MODE_LIQUID = "liquid";

    public static final String WEEK_MODE_VARIABLE = "variable";

    public static final boolean DEFAULT_ALL_DAY_DEFAULT = true;

    public static final boolean DEFAULT_ALL_DAY_SLOT = true;

    public static final double DEFAULT_ASPECT_RATIO = 1.35;

    public static final boolean DEFAULT_AUTO_REFRESH_ON_DATE_RANGE_SELECT = true;

    public static final String DEFAULT_AXIS_FORMAT = "h(:mm)tt";

    public static final boolean DEFAULT_DISABLE_DRAGGING = false;

    public static final boolean DEFAULT_DISABLE_RESIZING = false;

    public static final double DEFAULT_DRAG_OPACITY = .3;

    public static final int DEFAULT_DRAG_REVERT_DURATION = 500;

    public static final boolean DEFAULT_EDITABLE = false;

    public static final int DEFAULT_EVENT_MINUTES = 120;

    public static final int DEFAULT_FIRST_DAY = Calendar.SUNDAY;

    public static final int DEFAULT_FIRST_HOUR = 6;

    public static final String DEFAULT_MAX_TIME = "24";

    public static final String DEFAULT_MIN_TIME = "0";

    public static final boolean DEFAULT_RTL = false;

    public static final boolean DEFAULT_SELECTABLE = false;

    public static final boolean DEFAULT_SELECT_HELPER = false;

    public static final boolean DEFAULT_SHOW_WEEKENDS = true;

    public static final int DEFAULT_SLOT_MINUTES = 30;

    public static final String DEFAULT_SWITCH_TYPE = SWITCH_TYPE_AJAX;

    public static final boolean DEFAULT_UNSELECT_AUTO = true;

    public static final String DEFAULT_UNSELECT_CANCEL = "";

    public static final String DEFAULT_VIEW = VIEW_MONTH;

    public static final String DEFAULT_WEEK_MODE = WEEK_MODE_FIXED;

    private DataModel model;

// -------------------------- STATIC METHODS --------------------------

    /**
     * Tells value of firstDay. If it is not set it returns
     * default value.
     *
     * @param schedule inspected schedule
     * @return value of firstDay
     */
    public static int getFirstDay(AbstractSchedule schedule) {
        Integer firstDay = schedule.getFirstDay();
        return firstDay == null ? AbstractSchedule.DEFAULT_FIRST_DAY : firstDay;
    }

    /**
     * Gets first displayed day on schedule. This is used mainly when schedule
     * is in ajax or server mode to calculate initial events daterange.
     * Initial events are sent during first render in order to avoid extra
     * ajax request right after first render.
     *
     * @param schedule schedule configuration for which date is calculated
     * @return first day displayed on schedule
     */
    public static Date getFirstDisplayedDay(AbstractSchedule schedule) {
        Calendar calendar = Calendar.getInstance();
        Date date = schedule.getDate();
        if (date != null) {
            calendar.setTime(date);
        }
        int firstDayOfWeek = getFirstDay(schedule);
        calendar.setFirstDayOfWeek(firstDayOfWeek);
        String view = getView(schedule);
        boolean showWeekends = isShowWeekends(schedule);
        if (VIEW_MONTH.equals(view)) {
            calendar.set(Calendar.DATE, 1);
            if (!showWeekends) {
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek == Calendar.SUNDAY) {
                    calendar.add(Calendar.DATE, 1);
                } else if (dayOfWeek == Calendar.SATURDAY) {
                    calendar.add(Calendar.DATE, 2);
                }
            }
            if (!showWeekends && firstDayOfWeek == Calendar.SUNDAY) {
                firstDayOfWeek++;
            }
            /**
             * Following 1 line is a fix to what i believe is a bug of java.util.Calendar
             */
            calendar.get(Calendar.DAY_OF_WEEK);
            calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
            return calendar.getTime();
        } else if (VIEW_AGENDA_WEEK.equals(view) || VIEW_BASIC_WEEK.equals(view)) {
            calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
            if (!showWeekends) {
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek == Calendar.SUNDAY) {
                    calendar.add(Calendar.DATE, 1);
                } else if (dayOfWeek == Calendar.SATURDAY) {
                    calendar.add(Calendar.DATE, 2);
                }
            }
            return calendar.getTime();
        } else if (VIEW_AGENDA_DAY.equals(view) || VIEW_BASIC_DAY.equals(view)) {
            return calendar.getTime();
        } else {
            throw new IllegalStateException("Invalid view attribute: " + view);
        }
    }

    /**
     * Gets last displayed day on schedule.
     *
     * @param schedule schedule configuration for which date is calculated
     * @return last day displayed on schedule
     * @see AbstractSchedule#getFirstDisplayedDay(org.richfaces.component.AbstractSchedule)
     */
    public static Date getLastDisplayedDate(AbstractSchedule schedule) {
        Calendar calendar = Calendar.getInstance();
        int firstDayOfWeek = getFirstDay(schedule);
        String view = getView(schedule);
        boolean showWeekends = isShowWeekends(schedule);
        if (VIEW_MONTH.equals(view)) {
            if (WEEK_MODE_FIXED.equals(getWeekMode(schedule))) {
                calendar.setTime(getFirstDisplayedDay(schedule));
                calendar.add(Calendar.DAY_OF_YEAR, 42);
            } else {
                Date date = schedule.getDate();
                if (date != null) {
                    calendar.setTime(date);
                }

                if (!showWeekends && firstDayOfWeek == Calendar.SUNDAY) {
                    firstDayOfWeek++;
                }
                calendar.setFirstDayOfWeek(firstDayOfWeek);
                calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
                int dayOfWeek = firstDayOfWeek + 6;
                if (dayOfWeek > Calendar.SATURDAY) {
                    dayOfWeek -= 7;
                }
                /**
                 * Following 1 line is a fix to what i believe is a bug of java.util.Calendar
                 */
                calendar.get(Calendar.DAY_OF_WEEK);
                calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                calendar.add(Calendar.DATE, 1);
            }
            return calendar.getTime();
        } else if (VIEW_AGENDA_WEEK.equals(view) || VIEW_BASIC_WEEK.equals(view)) {
            calendar.setFirstDayOfWeek(firstDayOfWeek);
            calendar.setTime(getFirstDisplayedDay(schedule));
            calendar.add(Calendar.DATE, 7);
            if (!showWeekends) {
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek - 1 == Calendar.SUNDAY) {
                    calendar.add(Calendar.DATE, -2);
                } else if (dayOfWeek - 1 == Calendar.SATURDAY) {
                    calendar.add(Calendar.DATE, -1);
                }
            }
            return calendar.getTime();
        } else if (VIEW_AGENDA_DAY.equals(view) || VIEW_BASIC_DAY.equals(view)) {
            calendar.setTime(getFirstDisplayedDay(schedule));
            calendar.add(Calendar.DATE, 1);
            return calendar.getTime();
        } else {
            throw new IllegalStateException("Invalid view attribute: " + view);
        }
    }

    /**
     * Tells value of view. If it is not set it returns
     * default value.
     *
     * @param schedule inspected schedule
     * @return value of view
     */
    public static String getView(AbstractSchedule schedule) {
        String view = schedule.getView();
        return view == null ? AbstractSchedule.DEFAULT_VIEW : view;
    }

    /**
     * Tells value of weekMode. If it is not set it returns
     * default value.
     *
     * @param schedule inspected schedule
     * @return value of weekMode
     */
    public static String getWeekMode(AbstractSchedule schedule) {
        String weekMode = schedule.getWeekMode();
        return weekMode == null ? AbstractSchedule.DEFAULT_WEEK_MODE : weekMode;
    }

    /**
     * Tells if showWeekends is true or false. If it is not set it returns
     * default value.
     *
     * @param schedule inspected schedule
     * @return value of showWeekends
     */
    public static boolean isShowWeekends(AbstractSchedule schedule) {
        Boolean showWeekends = schedule.isShowWeekends();
        return showWeekends == null ? AbstractSchedule.DEFAULT_SHOW_WEEKENDS : schedule.isShowWeekends();
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface ScheduleCommonViewAttributes ---------------------

    @Attribute(defaultValue = "" + DEFAULT_DRAG_OPACITY)
    public abstract Double getDragOpacity();

// -------------------------- OTHER METHODS --------------------------

    /**
     * React on various events.
     * Vetoable events are first broadcasted to listeners bound via EL to
     * component attribtues and then if no veto is raised then to the rest of
     * listeners.
     * In case of non vetoable events the order of broadcast is reverse.
     * Vetoable events are: ScheduleItemMoveEvent, ScheduleItemResizeEvent
     * Non-vetoable events: ScheduleDateRangeChangeEvent, ScheduleItemSelectEvent,
     * ScheduleViewChangeEvent, ScheduleDateSelectEvent,
     * ScheduleDateRangeSelectEvent
     * In case of ScheduleDateRangeChangeEvent new items are returned
     * via response data map of ajaxContext.
     * In case of ScheduleItemMoveEvent and ScheduleItemResizeEvent, the
     * decision if veto was raised is sent back to client via response data map
     * of ajaxContext as boolean.
     *
     * @param event broadcasted event
     * @throws AbortProcessingException if broadcasting of particular event
     *                                  should stop
     */
    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        if (event instanceof ScheduleDateRangeChangeEvent) {
            super.broadcast(event);
            ScheduleDateRangeChangeEvent calendarAjaxEvent = (ScheduleDateRangeChangeEvent) event;
            FacesContext facesContext = getFacesContext();
            MethodExpression expression = getDateRangeChangeListener();
            if (expression != null) {
                expression.invoke(facesContext.getELContext(), new Object[]{event});
            }
            setResponseData(getScheduleData(calendarAjaxEvent.getStartDate(), calendarAjaxEvent.getEndDate()));
        } else if (event instanceof ScheduleItemMoveEvent) {
            FacesContext facesContext = getFacesContext();
            MethodExpression expression = getItemMoveListener();
            boolean allow = true;
            if (expression != null) {
                Object result = expression.invoke(facesContext.getELContext(), new Object[]{event});
                allow = (Boolean) result;
            }
            setResponseData(allow);
            super.broadcast(event);
        } else if (event instanceof ScheduleItemResizeEvent) {
            FacesContext facesContext = getFacesContext();
            MethodExpression expression = getItemResizeListener();
            boolean allow = true;
            if (expression != null) {
                Object result = expression.invoke(facesContext.getELContext(), new Object[]{event});
                allow = ((Boolean) result);
            }
            setResponseData(allow);
            super.broadcast(event);
        } else if (event instanceof ScheduleItemSelectEvent) {
            super.broadcast(event);
            FacesContext facesContext = getFacesContext();
            MethodExpression expression = getItemSelectListener();
            if (expression != null) {
                expression.invoke(facesContext.getELContext(), new Object[]{event});
            }
        } else if (event instanceof ScheduleViewChangeEvent) {
            super.broadcast(event);
            FacesContext facesContext = getFacesContext();
            MethodExpression expression = getViewChangeListener();
            if (expression != null) {
                expression.invoke(facesContext.getELContext(), new Object[]{event});
            }
        } else if (event instanceof ScheduleDateSelectEvent) {
            super.broadcast(event);
            FacesContext facesContext = getFacesContext();
            MethodExpression expression = getDateSelectListener();
            if (expression != null) {
                expression.invoke(facesContext.getELContext(), new Object[]{event});
            }
        } else if (event instanceof ScheduleDateRangeSelectEvent) {
            super.broadcast(event);
            FacesContext facesContext = getFacesContext();
            MethodExpression expression = getDateRangeSelectListener();
            if (expression != null) {
                expression.invoke(facesContext.getELContext(), new Object[]{event});
            }
        } else {
            super.broadcast(event);
        }
    }

    @Attribute
    public abstract String getAllDayText();

    @Attribute(defaultValue = "" + DEFAULT_ASPECT_RATIO)
    public abstract Double getAspectRatio();

    @Attribute(defaultValue = DEFAULT_AXIS_FORMAT)
    public abstract String getAxisFormat();

    @Attribute
    public abstract Integer getContentHeight();

    @Attribute
    public abstract Date getDate();

    @Attribute(signature = @Signature(parameters = ScheduleDateRangeChangeEvent.class))
    public abstract MethodExpression getDateRangeChangeListener();

    @Attribute(signature = @Signature(parameters = ScheduleDateRangeSelectEvent.class))
    public abstract MethodExpression getDateRangeSelectListener();

    @Attribute(signature = @Signature(parameters = ScheduleDateSelectEvent.class))
    public abstract MethodExpression getDateSelectListener();

    @Attribute(defaultValue = "" + DEFAULT_EVENT_MINUTES)
    public abstract Integer getDefaultEventMinutes();

    @Attribute(defaultValue = "" + DEFAULT_DRAG_REVERT_DURATION)
    public abstract Integer getDragRevertDuration();

    @Attribute(defaultValue = "" + DEFAULT_FIRST_DAY,
        description = @Description("First day of week. 1 - sunday, 2 - monday,..,7 - saturday."))
    public abstract Integer getFirstDay();

    @Attribute(defaultValue = "" + DEFAULT_FIRST_HOUR)
    public abstract Integer getFirstHour();

    @Attribute
    public abstract String getHeaderCenter();

    @Attribute
    public abstract String getHeaderLeft();

    @Attribute
    public abstract String getHeaderRight();

    @Attribute
    public abstract Integer getHeight();

    @Attribute(signature = @Signature(parameters = ScheduleItemMoveEvent.class, returnType = Boolean.class))
    public abstract MethodExpression getItemMoveListener();

    @Attribute(signature = @Signature(parameters = ScheduleItemResizeEvent.class, returnType = Boolean.class))
    public abstract MethodExpression getItemResizeListener();

    @Attribute(signature = @Signature(parameters = ScheduleItemSelectEvent.class))
    public abstract MethodExpression getItemSelectListener();

    @Attribute(defaultValue = DEFAULT_MAX_TIME)
    public abstract String getMaxTime();

    @Attribute(defaultValue = DEFAULT_MIN_TIME)
    public abstract String getMinTime();

    @Attribute(events = @EventName("beforedaterangeselect"))
    public abstract String getOnbeforedaterangeselect();

    @Attribute(events = @EventName("beforedateselect"))
    public abstract String getOnbeforedateselect();

    @Attribute(events = @EventName("beforeitemdrop"))
    public abstract String getOnbeforeitemdrop();

    @Attribute(events = @EventName("beforeitemresize"))
    public abstract String getOnbeforeitemresize();

    @Attribute(events = @EventName("beforeitemselect"))
    public abstract String getOnbeforeitemselect();

    @Attribute(events = @EventName("daterangechange"))
    public abstract String getOndaterangechange();

    @Attribute(events = @EventName("daterangeselect"))
    public abstract String getOndaterangeselect();

    @Attribute(events = @EventName(value = "dateselect", defaultEvent = true))
    public abstract String getOndateselect();

    @Attribute(events = @EventName("itemdragstart"))
    public abstract String getOnitemdragstart();

    @Attribute(events = @EventName("itemdragstop"))
    public abstract String getOnitemdragstop();

    @Attribute(events = @EventName("itemdrop"))
    public abstract String getOnitemdrop();

    @Attribute(events = @EventName("itemmouseout"))
    public abstract String getOnitemmouseout();

    @Attribute(events = @EventName("itemmouseover"))
    public abstract String getOnitemmouseover();

    @Attribute(events = @EventName("itemresize"))
    public abstract String getOnitemresize();

    @Attribute(events = @EventName("itemresizestart"))
    public abstract String getOnitemresizestart();

    @Attribute(events = @EventName("itemresizestop"))
    public abstract String getOnitemresizestop();

    @Attribute(events = @EventName("itemselect"))
    public abstract String getOnitemselect();

    @Attribute(events = @EventName("viewchange"))
    public abstract String getOnviewchange();

    @Attribute(events = @EventName("viewdisplay"))
    public abstract String getOnviewdisplay();

    /**
     * Gets data from provided data model within given range.
     * Range is [startDate;endDate), which means that start date is inclusive
     * and end date is exclusive.
     * Data are in form of list of maps, which is ready to serialize to JSON.
     *
     * @param startDate date of earliest item
     * @param endDate   date of lates item
     * @return list of items as map of their properties
     */
    public List<Map<String, Object>> getScheduleData(Date startDate, Date endDate) {
        /**
         * Locale must be US because this is the format the javascript widget supports
         */
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        format.setLenient(false);
        DataModel dataModel = getDataModel();
        if (dataModel instanceof ExtendedDataModel) {
            DataVisitor visitor = new DataVisitor() {
                //TODO Is this fine? or should we stack rowKeys and not use dataModel later on. I'don't know business rules of extendedDataModel, just used it once to do pagination with underlying EntityQuery from Seam

                public DataVisitResult process(FacesContext context, Object rowKey, Object argument) {
                    return null;
                }
            };
            ((ExtendedDataModel) dataModel).walk(getFacesContext(), visitor, new DateRange(startDate, endDate), null);
        }
        ELContext elContext = (ELContext) getFacesContext().getELContext();
        ValueExpression valueExpression = getFacesContext().getApplication().getExpressionFactory()
            .createValueExpression(elContext, "#{" + getVar() + "}", Object.class);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < dataModel.getRowCount(); i++) {
            dataModel.setRowIndex(i);
            valueExpression.setValue(elContext, dataModel.getRowData());
            Map<String, Object> firstDataElement = new HashMap<String, Object>();
// TODO shouldn't we check earlier if there is at most one renderable UIScheduleItem child?
            for (UIComponent child : getChildren()) {
                if (child instanceof AbstractScheduleItem) {
                    AbstractScheduleItem item = (AbstractScheduleItem) child;
                    if (!item.isRendered()) {
                        continue;
                    }
                    firstDataElement.put("id", item.getEventId());
                    firstDataElement.put("title", item.getTitle());
                    if (item.isAllDay() != null) {
                        firstDataElement.put("allDay", item.isAllDay());
                    }
                    firstDataElement.put("start", format.format(item.getStartDate()));
                    if (item.getEndDate() != null) {
                        firstDataElement.put("end", format.format(item.getEndDate()));
                    }
                    if (item.getUrl() != null) {
                        firstDataElement.put("url", item.getUrl());
                    }
                    if (item.getStyleClass() != null) {
                        firstDataElement.put("className", item.getStyleClass());
                    }
                    if (item.isEditable() != null) {
                        firstDataElement.put("editable", item.isEditable());
                    }
                    if (item.getData() != null) {
                        firstDataElement.put("data", item.getData());
                    }
                    if (item.getColor() != null) {
                        firstDataElement.put("color", item.getColor());
                    }
                    if (item.getBackgroundColor() != null) {
                        firstDataElement.put("backgroundColor", item.getBackgroundColor());
                    }
                    if (item.getBorderColor() != null) {
                        firstDataElement.put("borderColor", item.getBorderColor());
                    }
                    if (item.getTextColor() != null) {
                        firstDataElement.put("textColor", item.getTextColor());
                    }
                    data.add(firstDataElement);
                }
            }
        }
        valueExpression.setValue(elContext, null);
        return data;
    }

    @Attribute(defaultValue = "" + DEFAULT_SLOT_MINUTES)
    public abstract Integer getSlotMinutes();

    @Attribute
    public abstract String getStyleClass();

    @Attribute(defaultValue = "SwitchType." + DEFAULT_SWITCH_TYPE,
        suggestedValue = SWITCH_TYPE_AJAX + "," + SWITCH_TYPE_SERVER + "," + SWITCH_TYPE_CLIENT)
    public abstract SwitchType getSwitchType();

    @Attribute(defaultValue = DEFAULT_UNSELECT_CANCEL)
    public abstract String getUnselectCancel();

    @Attribute(required = true)
    public abstract Object getValue();

    @Attribute(required = true)
    public abstract String getVar();

    @Attribute(defaultValue = DEFAULT_VIEW,
        suggestedValue = VIEW_MONTH
            + "," + VIEW_AGENDA_DAY
            + "," + VIEW_AGENDA_WEEK
            + "," + VIEW_BASIC_DAY + "," + VIEW_BASIC_WEEK)
    public abstract String getView();

    @Attribute(signature = @Signature(parameters = ScheduleViewChangeEvent.class))
    public abstract MethodExpression getViewChangeListener();

    @Attribute(defaultValue = DEFAULT_WEEK_MODE,
        suggestedValue = WEEK_MODE_FIXED + "," + WEEK_MODE_LIQUID + "," + WEEK_MODE_VARIABLE)
    public abstract String getWeekMode();

    @Attribute
    public abstract String getWidgetVar();

    @Attribute(defaultValue = "" + DEFAULT_ALL_DAY_DEFAULT)
    public abstract Boolean isAllDayByDefault();

    @Attribute(defaultValue = "" + DEFAULT_ALL_DAY_SLOT)
    public abstract Boolean isAllDaySlot();

    /**
     * Tells if schedule should be automatically refreshed when date range is selected.
     *
     * @return true if schedule should be refreshed automaticaly; flase otherwise.
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isAutoRefreshOnDateRangeSelect();

    @Attribute(defaultValue = "" + DEFAULT_DISABLE_DRAGGING)
    public abstract Boolean isDisableDragging();

    @Attribute(defaultValue = "" + DEFAULT_DISABLE_RESIZING)
    public abstract Boolean isDisableResizing();

    @Attribute(defaultValue = "" + DEFAULT_EDITABLE)
    public abstract Boolean isEditable();

    @Attribute(defaultValue = "" + DEFAULT_RTL)
    public abstract Boolean isRTL();

    @Attribute(defaultValue = "" + DEFAULT_SELECT_HELPER)
    public abstract Boolean isSelectHelper();

    @Attribute(defaultValue = "" + DEFAULT_SELECTABLE)
    public abstract Boolean isSelectable();

    @Attribute(defaultValue = "" + DEFAULT_SHOW_WEEKENDS)
    public abstract Boolean isShowWeekends();

    @Attribute(defaultValue = "" + DEFAULT_UNSELECT_AUTO)
    public abstract Boolean isUnselectAuto();

    @Attribute
    public abstract String getEventColor();

    @Attribute
    public abstract String getEventBackgroundColor();

    @Attribute
    public abstract String getEventBorderColor();

    @Attribute
    public abstract String getEventTextColor();

    public void setDataModel(DataModel model) {
        this.model = model;
    }

    public abstract void setDate(Date date);

    public abstract void setFirstDay(Integer firstDay);

    public abstract void setShowWeekends(Boolean showWeekends);

    public abstract void setView(String view);

    @SuppressWarnings("unchecked")
    protected DataModel getDataModel() {
        // Return any previously cached DataModel instance
        if (this.model != null) {
            return model;
        }

        // Synthesize a DataModel around our current value if possible
        Object current = getValue();
        if (current == null) {
            setDataModel(new ListDataModel(Collections.EMPTY_LIST));
        } else if (current instanceof DataModel) {
            setDataModel((DataModel) current);
        } else if (current instanceof List) {
            setDataModel(new ListDataModel((List) current));
        } else if (Object[].class.isAssignableFrom(current.getClass())) {
            setDataModel(new ArrayDataModel((Object[]) current));
        } else if (current instanceof ResultSet) {
            setDataModel(new ResultSetDataModel((ResultSet) current));
        } else if (current instanceof Result) {
            setDataModel(new ResultDataModel((Result) current));
        } else {
            setDataModel(new ScalarDataModel(current));
        }
        return model;
    }

    private void setResponseData(Object data) {
        ExtendedPartialViewContext.getInstance(getFacesContext()).getResponseComponentDataMap().put(getClientId(getFacesContext()), data);
    }
}

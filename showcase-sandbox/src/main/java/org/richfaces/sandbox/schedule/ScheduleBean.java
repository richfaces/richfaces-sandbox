package org.richfaces.sandbox.schedule;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.Range;
import org.richfaces.component.AbstractSchedule;
import org.richfaces.component.event.*;
import org.richfaces.component.model.DateRange;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import java.io.Serializable;
import java.util.*;

@SessionScoped
@ManagedBean
public class ScheduleBean {
// ------------------------------ FIELDS ------------------------------

    private CustomScheduleListener additionalListener = new CustomScheduleListener();

    private boolean allDayByDefault;

    private boolean allDaySlot = true;

    private String allDayText = "All day";

    private List<ScheduleTask> allTasks = new ArrayList<ScheduleTask>();

    private boolean allowTaskMoving;

    private boolean allowTaskResizing;

    private Double aspectRatio = 1.;

    private String axisFormat = "h(:mm)tt";

    private String columnFormat = null;

    private Integer contentHeight = 400;

    private Integer defaultEventMinutes = 90;

    private Double dragOpacity = .2;

    private Integer dragRevertDuration = 2000;

    private Boolean editable = true;

    private int firstDay = Calendar.SUNDAY;

    private Integer firstHour = 8;

    private String headerCenter = "title";

    private String headerLeft = "prevYear,nextYear";

    private String headerRight = "basicDay,basicWeek agendaDay,agendaWeek month today prev,next";

    private Integer height = 400;

    private Date initialDate;

    private boolean isRTL;

    private ExtendedDataModel lazyDataModel = new MyDataModel();

    private String locale;

    private Integer maxTime = 17;

    private Integer minTime = 8;

    private Boolean selectHelper = true;

    private Boolean selectable = true;

    private String selectedEventId;

    private boolean showWeekends;

    private Integer slotMinutes = 30;

    private String switchType = "ajax";

    private int taskIdSequence = 1;

    private String text;

    private String timeFormat = null;

    private String titleFormat = null;

    private Boolean unselectAuto = true;

    private String unselectCancel = "";

    private String view = AbstractSchedule.VIEW_MONTH;

    private String weekMode = AbstractSchedule.WEEK_MODE_FIXED;

// --------------------------- CONSTRUCTORS ---------------------------

    public ScheduleBean() {
        Calendar instance = Calendar.getInstance();
        instance.setTime(getInitialDate());
        final String[] colors = new String[]{"#00ff00", "#ff0000", "#0000ff"};
        Random random = new Random();
        for (int i = -30; i < 60; i++) {
            instance.set(Calendar.HOUR, minTime + random.nextInt(maxTime - minTime));
            instance.set(Calendar.MINUTE, random.nextInt(59));
            instance.add(Calendar.DAY_OF_YEAR, 1);
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("category", "category-" + (i % 3));
            int taskId = taskIdSequence++;

            allTasks.add(new ScheduleTask("" + taskId, "Title " + taskId, instance.getTime(), instance.getTime(), data, colors[random.nextInt(3)]));
        }
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public CustomScheduleListener getAdditionalListener() {
        return additionalListener;
    }

    public boolean getAllDayByDefault() {
        return allDayByDefault;
    }

    public void setAllDayByDefault(boolean allDayByDefault) {
        this.allDayByDefault = allDayByDefault;
    }

    public boolean getAllDaySlot() {
        return allDaySlot;
    }

    public void setAllDaySlot(boolean allDaySlot) {
        this.allDaySlot = allDaySlot;
    }

    public String getAllDayText() {
        return allDayText;
    }

    public void setAllDayText(String allDayText) {
        this.allDayText = allDayText;
    }

    public Double getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(Double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public String getAxisFormat() {
        return axisFormat;
    }

    public void setAxisFormat(String axisFormat) {
        this.axisFormat = axisFormat;
    }

    public String getColumnFormat() {
        return columnFormat;
    }

    public void setColumnFormat(String columnFormat) {
        this.columnFormat = columnFormat;
    }

    public Integer getContentHeight() {
        return contentHeight;
    }

    public void setContentHeight(Integer contentHeight) {
        this.contentHeight = contentHeight;
    }

    public Integer getDefaultEventMinutes() {
        return defaultEventMinutes;
    }

    public void setDefaultEventMinutes(Integer defaultEventMinutes) {
        this.defaultEventMinutes = defaultEventMinutes;
    }

    public Double getDragOpacity() {
        return dragOpacity;
    }

    public void setDragOpacity(Double dragOpacity) {
        this.dragOpacity = dragOpacity;
    }

    public Integer getDragRevertDuration() {
        return dragRevertDuration;
    }

    public void setDragRevertDuration(Integer dragRevertDuration) {
        this.dragRevertDuration = dragRevertDuration;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public int getFirstDay() {
        return firstDay;
    }

    public void setFirstDay(int firstDay) {
        this.firstDay = firstDay;
    }

    public Integer getFirstHour() {
        return firstHour;
    }

    public void setFirstHour(Integer firstHour) {
        this.firstHour = firstHour;
    }

    public String getHeaderCenter() {
        return headerCenter;
    }

    public void setHeaderCenter(String headerCenter) {
        this.headerCenter = headerCenter;
    }

    public String getHeaderLeft() {
        return headerLeft;
    }

    public void setHeaderLeft(String headerLeft) {
        this.headerLeft = headerLeft;
    }

    public String getHeaderRight() {
        return headerRight;
    }

    public void setHeaderRight(String headerRight) {
        this.headerRight = headerRight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Date getInitialDate() {
        if (initialDate == null) {
            Calendar instance = Calendar.getInstance();
            instance.set(Calendar.YEAR, 2012);
            instance.set(Calendar.MONTH, 7);
            instance.set(Calendar.DATE, 22);
            initialDate = instance.getTime();
        }
        return initialDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    public Boolean getIsRTL() {
        return isRTL;
    }

    public void setIsRTL(Boolean isRTL) {
        this.isRTL = isRTL;
    }

    public ExtendedDataModel getLazyDataModel() {
        return lazyDataModel;
    }

    public String getLocale() {
        if (locale == null) {
            locale = FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage();
        }
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Integer getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Integer maxTime) {
        this.maxTime = maxTime;
    }

    public Integer getMinTime() {
        return minTime;
    }

    public void setMinTime(Integer minTime) {
        this.minTime = minTime;
    }

    public Boolean getSelectHelper() {
        return selectHelper;
    }

    public void setSelectHelper(Boolean selectHelper) {
        this.selectHelper = selectHelper;
    }

    public Boolean getSelectable() {
        return selectable;
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }

    public String getSelectedEventId() {
        return selectedEventId;
    }

    public Integer getSlotMinutes() {
        return slotMinutes;
    }

    public void setSlotMinutes(Integer slotMinutes) {
        this.slotMinutes = slotMinutes;
    }

    public String getSwitchType() {
        return switchType;
    }

    public void setSwitchType(String switchType) {
        this.switchType = switchType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

    public String getTitleFormat() {
        return titleFormat;
    }

    public void setTitleFormat(String titleFormat) {
        this.titleFormat = titleFormat;
    }

    public Boolean getUnselectAuto() {
        return unselectAuto;
    }

    public void setUnselectAuto(Boolean unselectAuto) {
        this.unselectAuto = unselectAuto;
    }

    public String getUnselectCancel() {
        return unselectCancel;
    }

    public void setUnselectCancel(String unselectCancel) {
        this.unselectCancel = unselectCancel;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getWeekMode() {
        return weekMode;
    }

    public void setWeekMode(String weekMode) {
        this.weekMode = weekMode;
    }

    public boolean isAllowTaskMoving() {
        return allowTaskMoving;
    }

    public void setAllowTaskMoving(boolean allowTaskMoving) {
        this.allowTaskMoving = allowTaskMoving;
    }

    public boolean isAllowTaskResizing() {
        return allowTaskResizing;
    }

    public void setAllowTaskResizing(boolean allowTaskResizing) {
        this.allowTaskResizing = allowTaskResizing;
    }

    public boolean isShowWeekends() {
        return showWeekends;
    }

    public void setShowWeekends(boolean showWeekends) {
        this.showWeekends = showWeekends;
    }

// -------------------------- OTHER METHODS --------------------------

    public void dateRangeChanged(ScheduleDateRangeChangeEvent event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Date range changed", event.toString()));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(event.getStartDate());
        if (AbstractSchedule.VIEW_MONTH.equals(getView())) {
            calendar.add(Calendar.DATE, 15);
        }
        setInitialDate(calendar.getTime());
    }

    public void dateRangeSelected(ScheduleDateRangeSelectEvent event) {
        if (editable) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Date range selected", event.toString()));
            int taskId = taskIdSequence++;
            allTasks.add(new ScheduleTask("" + taskId, "Title-" + taskId, event.getStartDate(), event.getEndDate(), event.isAllDay()));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Date range selected", "Cannot create item. Not in edit mode."));
        }
    }

    public void dateSelected(ScheduleDateSelectEvent event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Date selected", event.toString()));
        setInitialDate(event.getDate());
    }

    public List<ScheduleTask> getAllEvents() {
        return allTasks;
    }

    public ScheduleTask getSelectedTask() {
        return getTask(getSelectedEventId());
    }

    public Boolean taskMoved(ScheduleItemMoveEvent event) {
        System.out.println("taskMoved invoked " + event + " : " + isAllowTaskMoving());
        if (isAllowTaskMoving()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Item moved", event.toString()));
            ScheduleTask task = getTask(event.getEventId());
            selectedEventId = event.getEventId();
            if (task != null) {
                boolean endDateEqualsStartDate = task.getStartDate().equals(task.getEndDate());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(task.getStartDate());
                calendar.add(Calendar.DAY_OF_MONTH, event.getDayDelta());
                calendar.add(Calendar.MINUTE, event.getMinuteDelta());
                task.setStartDate(calendar.getTime());
                if (!event.isAllDay() && endDateEqualsStartDate) {
                    calendar.setTime(task.getStartDate());
                    calendar.add(Calendar.MINUTE, getDefaultEventMinutes());
                } else {
                    calendar.setTime(task.getEndDate());
                    calendar.add(Calendar.DAY_OF_MONTH, event.getDayDelta());
                    calendar.add(Calendar.MINUTE, event.getMinuteDelta());
                }
                task.setEndDate(calendar.getTime());
                task.setAllDay(event.isAllDay());
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "It is not allowed to move this item", event.toString()));
        }
        return isAllowTaskMoving();
    }

    public Boolean taskResized(ScheduleItemResizeEvent event) {
        System.out.println("taskResized invoked " + event + " : " + isAllowTaskResizing());
        if (isAllowTaskResizing()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Item resized", event.toString()));
            ScheduleTask task = getTask(event.getEventId());
            selectedEventId = event.getEventId();
            if (task != null) {
                Calendar calendar = Calendar.getInstance();
                Date date = task.getEndDate() == null ? task.getStartDate() : task.getEndDate();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, event.getDayDelta());
                calendar.add(Calendar.MINUTE, event.getMinuteDelta());
                task.setEndDate(calendar.getTime());
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "It is not allowed to resize this item", event.toString()));
        }
        return isAllowTaskResizing();
    }

    public void taskSelected(ScheduleItemSelectEvent event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Task selected", event.toString()));
        selectedEventId = event.getEventId();
    }

    public void viewChanged(ScheduleViewChangeEvent event) {
        System.out.println("viewChanged invoked " + event);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("View changed", event.toString()));
        setView(event.getView());
    }

    protected ScheduleTask getTask(String id) {
        if (id == null) {
            return null;
        }
        for (ScheduleTask task : allTasks) {
            if (id.equals(task.getId())) {
                return task;
            }
        }
        return null;
    }

// -------------------------- INNER CLASSES --------------------------

    public static class CustomScheduleListener implements ScheduleDateRangeChangeListener, ScheduleDateSelectListener, ScheduleItemMoveListener, ScheduleItemResizeListener, ScheduleItemSelectListener, ScheduleViewChangeListener, ScheduleDateRangeSelectListener, Serializable {
// ------------------------------ FIELDS ------------------------------

        private static FacesEvent recentlyProcessedEvent;

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface ScheduleDateRangeChangeListener ---------------------

        public void dateRangeChanged(ScheduleDateRangeChangeEvent event) {
            if (event != recentlyProcessedEvent) {
                recentlyProcessedEvent = event;
                addMessage(event.toString(), FacesMessage.SEVERITY_INFO);
            }
        }

// --------------------- Interface ScheduleDateRangeSelectListener ---------------------

        public void dateRangeSelected(ScheduleDateRangeSelectEvent event) {
            if (event != recentlyProcessedEvent) {
                recentlyProcessedEvent = event;
                addMessage(event.toString(), FacesMessage.SEVERITY_INFO);
            }
        }

// --------------------- Interface ScheduleDateSelectListener ---------------------

        public void dateSelected(ScheduleDateSelectEvent event) {
            if (event != recentlyProcessedEvent) {
                recentlyProcessedEvent = event;
                addMessage(event.toString(), FacesMessage.SEVERITY_INFO);
            }
        }

// --------------------- Interface ScheduleItemMoveListener ---------------------

        public void itemMove(ScheduleItemMoveEvent event) {
            if (event != recentlyProcessedEvent) {
                recentlyProcessedEvent = event;
                addMessage("I'd like to veto moving, but nobody cares!", FacesMessage.SEVERITY_WARN);
            }
        }

// --------------------- Interface ScheduleItemResizeListener ---------------------

        public void itemResize(ScheduleItemResizeEvent event) {
            if (event != recentlyProcessedEvent) {
                recentlyProcessedEvent = event;
                addMessage("I'd like to veto resizing, but nobody cares!", FacesMessage.SEVERITY_WARN);
            }
        }

// --------------------- Interface ScheduleItemSelectListener ---------------------

        public void itemSelected(ScheduleItemSelectEvent event) {
            if (event != recentlyProcessedEvent) {
                recentlyProcessedEvent = event;
                addMessage(event.toString(), FacesMessage.SEVERITY_INFO);
            }
        }

// --------------------- Interface ScheduleViewChangeListener ---------------------

        public void viewChanged(ScheduleViewChangeEvent event) {
            if (event != recentlyProcessedEvent) {
                recentlyProcessedEvent = event;
                addMessage(event.toString(), FacesMessage.SEVERITY_INFO);
            }
        }

        private void addMessage(String text, Severity severity) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Additional listener", text));
        }
    }

    private class MyDataModel extends ExtendedDataModel implements Serializable {
// ------------------------------ FIELDS ------------------------------

        java.util.Map indexToRowKey = new HashMap();

        int rowCount = -1;

        int rowIndex = -1;

        Object rowKey;

        java.util.Map rowKeyToIndex = new HashMap();

        java.util.Map wrappedDataMap = new HashMap();

// --------------------- GETTER / SETTER METHODS ---------------------

        @Override
        public int getRowCount() {
            if (rowCount == -1) {
                rowCount = wrappedDataMap.size();
            }
            return rowCount;
        }

        @Override
        public int getRowIndex() {
            return rowIndex;
        }

        @Override
        public Object getRowKey() {
            return rowKey;
        }

// -------------------------- OTHER METHODS --------------------------

        @Override
        public Object getRowData() {
            if (getRowKey() == null) {
                return null;
            } else {
                return wrappedDataMap.get(getRowKey());
            }
        }

        @Override
        public Object getWrappedData() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isRowAvailable() {
            if (getRowKey() == null) {
                return false;
            } else {
                return null != wrappedDataMap.get(getRowKey());
            }
        }

        @Override
        public void setRowIndex(int rowIndex) {
            this.rowIndex = rowIndex;
            Object key = indexToRowKey.get(rowIndex);
            if ((key != null && !key.equals(getRowKey())) || (key == null && getRowKey() != null)) {
                setRowKey(key);
            }
        }

        @Override
        public void setRowKey(Object key) {
            this.rowKey = key;
            Integer index = (Integer) rowKeyToIndex.get(key);
            if (index == null) {
                index = -1;
            }
            if (index != getRowIndex()) {
                setRowIndex(rowIndex);
            }
        }

        @Override
        public void setWrappedData(Object data) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void walk(FacesContext context, DataVisitor visitor, Range range, Object argument) {
            Date startDate = ((DateRange) range).getStartDate();
            Date endDate = ((DateRange) range).getEndDate();
            wrappedDataMap.clear();
            indexToRowKey.clear();
            rowKeyToIndex.clear();
            int i = 0;
            for (ScheduleTask task : allTasks) {
                if ((startDate == null || task.getStartDate().compareTo(startDate) >= 0) && (endDate == null || task.getStartDate().compareTo(endDate) < 0)) {
                    wrappedDataMap.put(task.getId(), task);
                    int index = i++;
                    indexToRowKey.put(index, task.getId());
                    rowKeyToIndex.put(task.getId(), index);
                    visitor.process(context, task.getId(), argument);
                }
            }
            rowCount = -1;
        }
    }
}

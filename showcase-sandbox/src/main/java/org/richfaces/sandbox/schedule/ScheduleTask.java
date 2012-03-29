package org.richfaces.sandbox.schedule;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

//TODO base this example on classroom schedule
public class ScheduleTask implements Serializable {
// ------------------------------ FIELDS ------------------------------

    private Boolean allDay;
    private String color;
    private Map<String, Object> data;
    private String details;
    private Boolean editable;
    private Date endDate;
    //    TODO shouldn't ID be an object?

    private String id;
    private Date startDate;
    private String title;
    private String url;

// --------------------------- CONSTRUCTORS ---------------------------

    public ScheduleTask() {
    }

    public ScheduleTask(String id, String title, Date start, Date end) {
        this.id = id;
        this.title = title;
        this.startDate = start;
        this.endDate = end;
    }

    public ScheduleTask(String id, String title, Date start, Date end, boolean allDay) {
        this.id = id;
        this.title = title;
        this.startDate = start;
        this.endDate = end;
        this.allDay = allDay;
    }

    public ScheduleTask(String id, String title, Date start, Date end, Map<String, Object> data, String color) {
        this.id = id;
        this.title = title;
        this.startDate = start;
        this.endDate = end;
        this.data = data;
        this.color = color;
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public Boolean getAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ScheduleTask)) {
            return false;
        }

        ScheduleTask toCompare = (ScheduleTask) obj;
        return this.id != null && this.id.equals(toCompare.id);
    }

    @Override
    public int hashCode() {
        int hash = 1;
        if (this.id != null) {
            hash = hash * 31 + this.id.hashCode();
        }
        return hash;
    }

// -------------------------- OTHER METHODS --------------------------

    public String getURL() {
        return url;
    }

    public void setURL(String url) {
        this.url = url;
    }
}

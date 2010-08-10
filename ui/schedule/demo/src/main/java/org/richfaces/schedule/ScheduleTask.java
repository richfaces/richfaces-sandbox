package org.richfaces.schedule;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

//TODO base this example on classroom schedule
public class ScheduleTask implements Serializable {
    //    TODO shouldn't ID be an object?

    private String id;
    private String title;
    private Date startDate;
    private Date endDate;
    private Boolean allDay;
    private Map<String, Object> data;
    private Boolean editable;
    private String url;
    private String details;

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

    public ScheduleTask(String id, String title, Date start, Date end, Map<String, Object> data) {
        this.id = id;
        this.title = title;
        this.startDate = start;
        this.endDate = end;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String url) {
        this.url = url;
    }

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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}

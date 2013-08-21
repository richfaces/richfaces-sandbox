package org.richfaces.component.event;

import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class ScheduleDateRangeSelectEvent extends FacesEvent {

    private Date startDate;
    private Date endDate;
    private boolean allDay;

    public ScheduleDateRangeSelectEvent(UIComponent component, Date startDate, Date endDate, boolean allDay) {
        super(component);
        this.startDate = startDate;
        this.endDate = endDate;
        this.allDay = allDay;
    }

    public boolean isAppropriateListener(FacesListener facesListener) {
        return facesListener instanceof ScheduleDateSelectListener;
    }

    public void processListener(FacesListener facesListener) {
        ((ScheduleDateRangeSelectListener) facesListener).dateRangeSelected(this);
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public boolean isAllDay() {
        return allDay;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[startDate=" + startDate + ";endDate=" + endDate
            + ";allDay=" + allDay + "]";
    }
}

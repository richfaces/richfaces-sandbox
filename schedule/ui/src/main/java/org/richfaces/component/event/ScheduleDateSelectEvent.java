package org.richfaces.component.event;

import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class ScheduleDateSelectEvent extends FacesEvent {

    private Date date;
    private boolean allDay;

    public ScheduleDateSelectEvent(UIComponent component, Date date, boolean allDay) {
        super(component);
        this.date = date;
        this.allDay = allDay;
    }

    public boolean isAppropriateListener(FacesListener facesListener) {
        return facesListener instanceof ScheduleDateSelectListener;
    }

    public void processListener(FacesListener facesListener) {
        ((ScheduleDateSelectListener) facesListener).dateSelected(this);
    }

    public Date getDate() {
        return date;
    }

    public boolean isAllDay() {
        return allDay;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[date=" + date + ";allDay=" + allDay + "]";
    }
}

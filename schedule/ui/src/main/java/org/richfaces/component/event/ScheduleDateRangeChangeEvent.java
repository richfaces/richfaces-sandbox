package org.richfaces.component.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import java.util.Date;

public class ScheduleDateRangeChangeEvent extends FacesEvent {

    private Date startDate;
    private Date endDate;

    public ScheduleDateRangeChangeEvent(UIComponent component, Date startDate, Date endDate) {
        super(component);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[startDate=" + startDate + ";endDate=" + endDate + "]";
    }

    @Override
    public boolean isAppropriateListener(FacesListener listener) {
        return listener instanceof ScheduleDateRangeChangeListener;
    }

    @Override
    public void processListener(FacesListener listener) {
        ((ScheduleDateRangeChangeListener) listener).dateRangeChanged(this);
    }
}

package org.richfaces.component.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class ScheduleItemSelectEvent extends FacesEvent {

    private String eventId;

    public ScheduleItemSelectEvent(UIComponent component, String eventId) {
        super(component);
        this.eventId = eventId;
    }

    public boolean isAppropriateListener(FacesListener facesListener) {
        return facesListener instanceof ScheduleItemSelectListener;
    }

    public void processListener(FacesListener facesListener) {
        ((ScheduleItemSelectListener) facesListener).itemSelected(this);
    }

    public String getEventId() {
        return eventId;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[eventId=" + eventId + "]";
    }
}

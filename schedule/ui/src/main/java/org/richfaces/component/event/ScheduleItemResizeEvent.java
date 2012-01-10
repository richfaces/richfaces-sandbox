package org.richfaces.component.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class ScheduleItemResizeEvent extends FacesEvent {

    private String eventId;
    private int dayDelta;
    private int minuteDelta;

    public ScheduleItemResizeEvent(UIComponent component, String eventId, int dayDelta, int minuteDelta) {
        super(component);
        this.eventId = eventId;
        this.dayDelta = dayDelta;
        this.minuteDelta = minuteDelta;
    }

    public boolean isAppropriateListener(FacesListener facesListener) {
        return facesListener instanceof ScheduleItemResizeListener;
    }

    public void processListener(FacesListener facesListener) {
        ((ScheduleItemResizeListener) facesListener).itemResize(this);
    }

    public String getEventId() {
        return eventId;
    }

    public int getDayDelta() {
        return dayDelta;
    }

    public int getMinuteDelta() {
        return minuteDelta;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[eventId=" + eventId + ";dayDelta=" + dayDelta
            + ";minuteDelta=" + minuteDelta + "]";
    }
}

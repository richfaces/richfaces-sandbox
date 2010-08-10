package org.richfaces.component.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class ScheduleItemMoveEvent extends FacesEvent {

    private String eventId;
    private int dayDelta;
    private int minuteDelta;
    private boolean allDay;

    public ScheduleItemMoveEvent(UIComponent component, String eventId, int dayDelta, int minuteDelta, boolean allDay) {
        super(component);
        this.eventId = eventId;
        this.dayDelta = dayDelta;
        this.minuteDelta = minuteDelta;
        this.allDay = allDay;
    }

    public boolean isAppropriateListener(FacesListener facesListener) {
        return facesListener instanceof ScheduleItemMoveListener;
    }

    public void processListener(FacesListener facesListener) {
        ((ScheduleItemMoveListener) facesListener).itemMove(this);
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

    public boolean isAllDay() {
        return allDay;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[eventId=" + eventId + ";dayDelta=" + dayDelta
            + ";minuteDelta=" + minuteDelta + ";allDay=" + allDay + "]";
    }
}

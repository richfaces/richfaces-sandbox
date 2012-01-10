package org.richfaces.component.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class ScheduleViewChangeEvent extends FacesEvent {

    private String view;

    public ScheduleViewChangeEvent(UIComponent component, String view) {
        super(component);
        this.view = view;
    }

    public boolean isAppropriateListener(FacesListener facesListener) {
        return facesListener instanceof ScheduleViewChangeListener;
    }

    public void processListener(FacesListener facesListener) {
        ((ScheduleViewChangeListener) facesListener).viewChanged(this);
    }

    public String getView() {
        return view;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[view=" + view + "]";
    }
}

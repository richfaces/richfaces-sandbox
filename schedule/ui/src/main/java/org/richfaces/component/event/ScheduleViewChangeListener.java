package org.richfaces.component.event;

import javax.faces.event.FacesListener;

public interface ScheduleViewChangeListener extends FacesListener {

    void viewChanged(ScheduleViewChangeEvent event);
}

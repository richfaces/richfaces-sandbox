package org.richfaces.component.event;

import javax.faces.event.FacesListener;

public interface ScheduleItemSelectListener extends FacesListener {

    public void itemSelected(ScheduleItemSelectEvent event);
}

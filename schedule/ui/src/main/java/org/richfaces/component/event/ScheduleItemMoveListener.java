package org.richfaces.component.event;

import javax.faces.event.FacesListener;

public interface ScheduleItemMoveListener extends FacesListener {

    void itemMove(ScheduleItemMoveEvent event);
}

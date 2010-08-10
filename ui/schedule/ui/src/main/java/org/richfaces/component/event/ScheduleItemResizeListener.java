package org.richfaces.component.event;

import javax.faces.event.FacesListener;

public interface ScheduleItemResizeListener extends FacesListener {

    void itemResize(ScheduleItemResizeEvent event);
}

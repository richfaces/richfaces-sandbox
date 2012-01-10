package org.richfaces.component.event;

import javax.faces.event.FacesListener;

public interface ScheduleDateRangeSelectListener extends FacesListener {

    void dateRangeSelected(ScheduleDateRangeSelectEvent event);
}

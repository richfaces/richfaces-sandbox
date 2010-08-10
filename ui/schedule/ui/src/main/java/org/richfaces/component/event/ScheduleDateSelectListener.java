package org.richfaces.component.event;

import javax.faces.event.FacesListener;

public interface ScheduleDateSelectListener extends FacesListener {

    void dateSelected(ScheduleDateSelectEvent event);
}

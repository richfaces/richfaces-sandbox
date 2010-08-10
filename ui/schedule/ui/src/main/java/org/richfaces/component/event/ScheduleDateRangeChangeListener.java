package org.richfaces.component.event;

import javax.faces.event.FacesListener;

public interface ScheduleDateRangeChangeListener extends FacesListener {

    void dateRangeChanged(ScheduleDateRangeChangeEvent event);
}

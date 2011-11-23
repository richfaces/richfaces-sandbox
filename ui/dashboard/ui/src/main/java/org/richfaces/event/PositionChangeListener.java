package org.richfaces.event;

import javax.faces.event.FacesListener;

public interface PositionChangeListener extends FacesListener {
// -------------------------- OTHER METHODS --------------------------

    void positionChange(PositionChangeEvent event);
}

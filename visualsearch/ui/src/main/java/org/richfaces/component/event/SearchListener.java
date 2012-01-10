package org.richfaces.component.event;

import javax.faces.event.FacesListener;

public interface SearchListener extends FacesListener {
// -------------------------- OTHER METHODS --------------------------

    void search(SearchEvent searchEvent);
}

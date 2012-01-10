package org.richfaces.component.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class SearchEvent extends FacesEvent {
// --------------------------- CONSTRUCTORS ---------------------------

    public SearchEvent(UIComponent component)
    {
        super(component);
    }

// -------------------------- OTHER METHODS --------------------------

    public boolean isAppropriateListener(FacesListener facesListener)
    {
        return facesListener instanceof FacetSuggestionListener;
    }

    public void processListener(FacesListener facesListener)
    {
        ((SearchListener) facesListener).search(this);
    }
}

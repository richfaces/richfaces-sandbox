package org.richfaces.component.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class FacetSuggestionEvent extends FacesEvent {
// ------------------------------ FIELDS ------------------------------

    private String searchTerm;

// --------------------------- CONSTRUCTORS ---------------------------

    public FacetSuggestionEvent(UIComponent component, String searchTerm)
    {
        super(component);
        this.searchTerm = searchTerm;
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public String getSearchTerm()
    {
        return searchTerm;
    }

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "[text=" + searchTerm + "]";
    }

// -------------------------- OTHER METHODS --------------------------

    public boolean isAppropriateListener(FacesListener facesListener)
    {
        return facesListener instanceof FacetSuggestionListener;
    }

    public void processListener(FacesListener facesListener)
    {
        ((FacetSuggestionListener) facesListener).suggest(this);
    }
}

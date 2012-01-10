package org.richfaces.component.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class ValueSuggestionEvent extends FacesEvent {
// ------------------------------ FIELDS ------------------------------

    private String facet;

    private String searchTerm;

// --------------------------- CONSTRUCTORS ---------------------------

    public ValueSuggestionEvent(UIComponent component, String searchTerm, String facet)
    {
        super(component);
        this.searchTerm = searchTerm;
        this.facet = facet;
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public String getFacet()
    {
        return facet;
    }

    public String getSearchTerm()
    {
        return searchTerm;
    }

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    public String toString()
    {
        return "ValueSuggestionEvent{" + "facet='" + facet + '\'' + ", text='" + searchTerm + '\'' + '}';
    }

// -------------------------- OTHER METHODS --------------------------

    public boolean isAppropriateListener(FacesListener facesListener)
    {
        return facesListener instanceof FacetSuggestionListener;
    }

    public void processListener(FacesListener facesListener)
    {
        ((ValueSuggestionListener) facesListener).suggest(this);
    }
}

package org.richfaces.component.event;

import javax.faces.event.FacesListener;

public interface FacetSuggestionListener extends FacesListener {
// -------------------------- OTHER METHODS --------------------------

    void suggest(FacetSuggestionEvent suggestionEvent);
}

package org.richfaces.component.event;

import javax.faces.event.FacesListener;

public interface ValueSuggestionListener extends FacesListener {
// -------------------------- OTHER METHODS --------------------------

    void suggest(ValueSuggestionEvent suggestionEvent);
}

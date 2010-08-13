package org.richfaces.renderkit;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class AbstractAutocompleteLayoutStrategy {
	public String getContainerElementId(FacesContext facesContext, UIComponent component) {
        return component.getClientId(facesContext) + "Items";
    }
}

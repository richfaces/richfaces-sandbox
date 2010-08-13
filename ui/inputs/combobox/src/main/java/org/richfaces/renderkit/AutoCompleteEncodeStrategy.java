package org.richfaces.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.component.AbstractAutoComplete;


public interface AutoCompleteEncodeStrategy {
	void encodeItemsContainerBegin(FacesContext facesContext, UIComponent component) throws IOException ;

    void encodeItemsContainerEnd(FacesContext facesContext, UIComponent component) throws IOException ;
    
    void encodeFakeItem(FacesContext facesContext, UIComponent component) throws IOException ;

	void encodeItem(FacesContext facesContext, AbstractAutoComplete comboBox,
			Object nextItem) throws IOException;
	
	public String getContainerElementId(FacesContext facesContext, UIComponent component);
}

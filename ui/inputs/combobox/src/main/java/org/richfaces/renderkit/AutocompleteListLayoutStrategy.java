package org.richfaces.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.richfaces.component.AbstractAutoComplete;

public class AutocompleteListLayoutStrategy extends AbstractAutocompleteLayoutStrategy implements
		AutoCompleteEncodeStrategy {

	public void encodeFakeItem(FacesContext facesContext, UIComponent component)
			throws IOException {
		ResponseWriter responseWriter = facesContext.getResponseWriter();
        responseWriter.startElement(HTML.LI_ELEMENT, component);
        responseWriter.writeAttribute(HTML.STYLE_ATTRIBUTE, "display:none", null);
        responseWriter.endElement(HTML.LI_ELEMENT);

	}

	public void encodeItemsContainerBegin(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        responseWriter.startElement(HTML.UL_ELEMENT, component);
        responseWriter.writeAttribute(HTML.ID_ATTRIBUTE, getContainerElementId(facesContext, component), null);
        responseWriter.writeAttribute(HTML.CLASS_ATTRIBUTE, "cb_list_ul", null);
    }

    public void encodeItemsContainerEnd(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        responseWriter.endElement(HTML.UL_ELEMENT);
    }

	public void encodeItem(FacesContext facesContext,
			AbstractAutoComplete comboBox, Object item) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.LI_ELEMENT, comboBox);
        writer.writeAttribute(HTML.CLASS_ATTRIBUTE, "cb_option cb_font rf-ac-i", null);

        if (comboBox.getChildCount() > 0) {
            for (UIComponent child: comboBox.getChildren()) {
                child.encodeAll(facesContext);
            }
        } else {
            if (item != null) {
                //TODO nick - use converter
            	String value = null;
            	if (comboBox.getItemConverter() != null) {
            		value = comboBox.getItemConverter().getAsString(facesContext, comboBox, item);
            	}
            	if (value != null) {
            		writer.writeText(value, null);
            	}
            	writer.writeText(item, null);
            }
        }

        writer.endElement(HTML.LI_ELEMENT);
		
	}

}

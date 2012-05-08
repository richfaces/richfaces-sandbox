package org.richfaces.renderkit.html;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlSelectOneRadio;
import javax.faces.context.FacesContext;

import org.richfaces.renderkit.InputRendererBase;

public class SelectOneRadioRenderer extends InputRendererBase {
// ------------------------------ FIELDS ------------------------------

    public static final String RENDERER_TYPE = "org.richfaces.renderkit.html.SelectOneRadioRenderer";

// -------------------------- OTHER METHODS --------------------------

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
    }

    @Override
    protected void doDecode(FacesContext context, UIComponent component) {
        HtmlSelectOneRadio radio = (HtmlSelectOneRadio) component;
        if (radio.isReadonly() || radio.isDisabled() || !component.isRendered()) {
            return;
        }
        super.doDecode(context, component);
    }
}

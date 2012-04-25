package org.richfaces.bootstrap.renderkit;

import org.richfaces.bootstrap.component.AbstractButton;
import org.richfaces.component.AbstractMenuItem;
import org.richfaces.component.Mode;
import org.richfaces.renderkit.AjaxCommandRendererBase;
import org.richfaces.renderkit.util.HandlersChain;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

@ResourceDependencies({
        @ResourceDependency(library = "org.richfaces", name = "ajax.reslib"),
        @ResourceDependency(library = "bootstrap/css", name = "bootstrap.css"),
        @ResourceDependency(library = "bootstrap/js", name = "bootstrap.js")})
public class ButtonRendererBase extends AjaxCommandRendererBase {
    public static final String RENDERER_TYPE = "org.richfaces.bootstrap.ButtonRenderer";

    // A workaround for RF-11668
    public AbstractButton castComponent(UIComponent component) {
        return (AbstractButton) component;
    }

    @Override
    public void doDecode(FacesContext context, UIComponent component) {
        AbstractButton button = (AbstractButton) component;
        if (button != null) {
            if (!Mode.client.equals(button.getMode())) {
                super.doDecode(context, component);
            }
        }
    }

    @Override
    public String getOnClick(FacesContext context, UIComponent component) {
        AbstractButton button = (AbstractButton) component;
        Mode submitMode = button.getMode();
        StringBuffer onClick = new StringBuffer();

        if (!button.isDisabled()) {
            HandlersChain handlersChain = new HandlersChain(context, component);

            handlersChain.addInlineHandlerFromAttribute("onclick");
            handlersChain.addBehaviors("click", "action");

            switch (submitMode) {
                case ajax:
                    handlersChain.addAjaxSubmitFunction();
                    break;
                case server:
                    handlersChain.addInlineHandlerAsValue("RichFaces.submitForm(event.form, event.itemId)");
            }

            String handlerScript = handlersChain.toScript();

            if (handlerScript != null) {
                onClick.append(handlerScript);
            }

            if (!"reset".equals(component.getAttributes().get("type"))) {
                onClick.append(";return false;");
            }
        } else {
            onClick.append("return false;");
        }

        return onClick.toString();
    }
}

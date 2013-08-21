package org.richfaces.bootstrap.function;

import java.text.MessageFormat;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.bootstrap.javascript.BootstrapJSPlugin;
import org.richfaces.ui.misc.RichFunction;

/**
 * Bootstrap components client-side API operation.
 * 
 * @author Lukas Fryc
 */
class BootstrapOperation {

    private static String CALL = "jQuery('{0}').{1}('{2}')";
    private static String ESCAPED_CALL = CALL.replace("'", "''");

    private FacesContext facesContext;
    private UIComponent component;
    private String operation;

    public BootstrapOperation(FacesContext facesContext, UIComponent component, String operation) {
        this.facesContext = facesContext;
        this.component = component;
        this.operation = operation;
    }

    public String getClientSideCall() {
        String pluginName = getPluginName();
        String escapedClientId = RichFunction.jQuerySelector(facesContext, component);
        return MessageFormat.format(ESCAPED_CALL, escapedClientId, pluginName, operation);
    }

    public void verifyComponent(Class<?> type) {
        if (component == null) {
            throw new IllegalArgumentException("no such target component with identified was found");
        }

        if (component.getClass().isInstance(type)) {
            // TODO unified API for reporting meaningful exceptions (componentID, etc.)
            throw new IllegalArgumentException("the target component " + component + " is not " + type.getName());
        }
    }

    private String getPluginName() {
        BootstrapJSPlugin bootstrapPlugin = component.getClass().getSuperclass().getAnnotation(BootstrapJSPlugin.class);
        if (bootstrapPlugin == null) {
            throw new IllegalArgumentException("given component does not have Bootstrap JavaScript plugin binding defined");
        }
        return bootstrapPlugin.name();
    }
}
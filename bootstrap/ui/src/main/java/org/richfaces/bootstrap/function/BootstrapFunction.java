package org.richfaces.bootstrap.function;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.bootstrap.javascript.api.Hideable;
import org.richfaces.bootstrap.javascript.api.Showable;
import org.richfaces.bootstrap.javascript.api.Toggleable;
import org.richfaces.cdk.annotations.Function;
import org.richfaces.function.RichFunction;

/**
 * Bootstrap specific functions to operate with client-side component API.
 *
 * @author Lukas Fryc
 */
public final class BootstrapFunction {

    @Function
    public static String toggle(String target) {
        return bootstrapCall(target, Toggleable.class, "toggle");
    }

    @Function
    public static String hide(String target) {
        return bootstrapCall(target, Hideable.class, "hide");
    }

    @Function
    public static String show(String target) {
        return bootstrapCall(target, Showable.class, "show");
    }

    private static String bootstrapCall(String target, Class<?> type, String operationName) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIComponent component = RichFunction.findComponent(facesContext, target);
        BootstrapOperation operation = new BootstrapOperation(facesContext, component, operationName);

        operation.verifyComponent(type);
        String call = operation.getClientSideCall();
        return call;
    }
}

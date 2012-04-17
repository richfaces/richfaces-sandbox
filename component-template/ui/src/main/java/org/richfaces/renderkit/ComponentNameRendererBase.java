package org.richfaces.renderkit;

import org.richfaces.component.ComponentName;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;

@ResourceDependencies({
        @ResourceDependency(library = "org.richfaces", name = "ajax.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "componentName.js"),
        @ResourceDependency(library = "org.richfaces", name = "componentName.css")})
public abstract class ComponentNameRendererBase extends RendererBase {
    public static final String RENDERER_TYPE = "org.richfaces.ComponentNameRenderer";

    // A workaround for RF-11668
    public ComponentName castComponent(UIComponent component) {
        return (ComponentName) component;
    }
}

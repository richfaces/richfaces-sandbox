package org.richfaces.bootstrap.renderkit;

import org.richfaces.bootstrap.component.HeroUnit;
import org.richfaces.renderkit.RendererBase;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;

@ResourceDependencies({
        @ResourceDependency(library = "org.richfaces", name = "ajax.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
        @ResourceDependency(library = "bootstrap/css", name = "bootstrap.css")})
public abstract class HeroUnitRendererBase extends RendererBase {
    public static final String RENDERER_TYPE = "org.richfaces.bootstrap.HeroUnitRenderer";

    // A workaround for RF-11668
    public HeroUnit castComponent(UIComponent component) {
        return (HeroUnit) component;
    }
}

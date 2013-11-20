package org.richfaces.bootstrap.ui.navlist;

import org.richfaces.bootstrap.semantic.AbstractSeparatorFacet;
import org.richfaces.renderkit.RendererBase;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Base class for the Navlist renderer
 *
 * @author Lukas Eichler
 */
@ResourceDependencies({
        @ResourceDependency(library = "org.richfaces", name = "ajax.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "bootstrap-css.reslib")})
public abstract class NavlistRendererBase extends RendererBase {
    public static final String RENDERER_TYPE = "org.richfaces.bootstrap.NavlistRenderer";

    public void renderSeparator(FacesContext facesContext, UIComponent component) throws IOException {
        if (component instanceof AbstractSeparatorFacet) {
            ResponseWriter writer = facesContext.getResponseWriter();
            writer.startElement("li", component);
            writer.writeAttribute("class", "divider", null);
            writer.endElement("li");

        } else {
            throw new IllegalArgumentException("needs to be instance of AbstractSeperatorFacet");
        }
    }
}


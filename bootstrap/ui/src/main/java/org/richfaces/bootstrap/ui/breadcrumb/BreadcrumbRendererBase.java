package org.richfaces.bootstrap.ui.breadcrumb;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.google.common.collect.Collections2;
import org.richfaces.bootstrap.semantic.AbstractMenuFacet;
import org.richfaces.bootstrap.semantic.AbstractPositionFacet;
import org.richfaces.bootstrap.ui.navbar.AbstractNavbar;
import org.richfaces.renderkit.RendererBase;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;

/**
 * Base class for the breadcrumb renderer
 *
 * @author damien.gouyette@gmail.com
 */
@ResourceDependencies({@ResourceDependency(library = "org.richfaces", name = "ajax.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "bootstrap-css.reslib")})
public class BreadcrumbRendererBase extends RendererBase {
    public static final String RENDERER_TYPE = "org.richfaces.bootstrap.BreadcrumbRenderer";


    @Override
    protected void doEncodeChildren(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        AbstractBreadcrumb breadcrumb = (AbstractBreadcrumb) component;
        writer.startElement("ul", component);
        writer.writeAttribute("class", "breadcrumb", null);

        int count = 0;
        for (UIComponent child : breadcrumb.getChildren()) {
            writer.startElement("li", breadcrumb);
            child.encodeAll(context);
            writer.startElement("span", breadcrumb);
            writer.writeAttribute("class", "divider", null);

            if (count < breadcrumb.getChildCount()-1)
                writer.append("&gt;");

            writer.endElement("span");
            writer.endElement("li");
            count++;
        }
        writer.endElement("ul");

    }
}

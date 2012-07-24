/**
 * JBoss, Home of Professional Open Source
 * Copyright , Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.bootstrap.renderkit;

import java.io.IOException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.richfaces.bootstrap.component.AbstractMenuFacet;
import org.richfaces.bootstrap.component.AbstractNavbar;
import org.richfaces.bootstrap.component.AbstractPositionFacet;
import org.richfaces.renderkit.RendererBase;

/**
 * Base class for the navbar renderer
 * 
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@ResourceDependencies({ @ResourceDependency(library = "org.richfaces", name = "ajax.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "bootstrap-css.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "bootstrap-responsive.reslib")
})
public abstract class NavbarRendererBase extends RendererBase {
    public static final String RENDERER_TYPE = "org.richfaces.bootstrap.NavbarRenderer";
    
    @Override
    protected void doEncodeChildren(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        AbstractNavbar navbar = (AbstractNavbar) component;
        boolean hasPositionFacet = hasPositionFacet(navbar);
        
        if (navbar.isCollapsible()) {
            writer.startElement("div", component);
            writer.writeAttribute("id", component.getId() + "_collapse", null);
            writer.writeAttribute("class", "nav-collapse", null);
        }
        
        if(!hasPositionFacet) {
            writer.startElement("ul", component);
            writer.writeAttribute("class", "nav", null);
        }
        
        for(UIComponent child : navbar.getChildren()) {
            if(child instanceof AbstractMenuFacet || child instanceof AbstractPositionFacet) {
                child.encodeAll(context);
            } else {
                writer.startElement("li", navbar);
                child.encodeAll(context);
                writer.endElement("li");
            }
        }
        
        if(!hasPositionFacet) {
            writer.endElement("ul");
        }
        
        if (navbar.isCollapsible()) {
            writer.endElement("div");
        }
    }

    private boolean hasPositionFacet(UIComponent component) {
        for(UIComponent child : component.getChildren()) {
            if(child instanceof AbstractPositionFacet) {
                return true;
            }
        }
        
        return false;
    }
}

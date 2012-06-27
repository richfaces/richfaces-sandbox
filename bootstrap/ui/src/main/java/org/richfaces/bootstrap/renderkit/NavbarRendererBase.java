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
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.richfaces.bootstrap.component.AbstractMenuGroup;
import org.richfaces.bootstrap.component.AbstractPositionGroup;
import org.richfaces.bootstrap.component.AbstractNavbar;
import org.richfaces.bootstrap.component.HorizontalPosition;
import org.richfaces.renderkit.RendererBase;

/**
 * Base class for the navbar renderer
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@ResourceDependencies({
        @ResourceDependency(library = "org.richfaces", name = "ajax.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
        @ResourceDependency(library = "bootstrap/css", name = "bootstrap.css"),
        @ResourceDependency(library = "bootstrap/css", name = "bootstrap-responsive.css"),
        @ResourceDependency(library = "bootstrap/js", name = "bootstrap.js")})
public abstract class NavbarRendererBase extends RendererBase {
    public static final String RENDERER_TYPE = "org.richfaces.bootstrap.NavbarRenderer";

    public void renderFacet(String facetName, FacesContext context, UIComponent component) throws IOException {
        UIComponent facet = component.getFacet(facetName);
        facet.encodeAll(context);
    }
    
    public void renderBrandFacet(FacesContext context, UIComponent component) throws IOException {
        renderFacet("brand", context, component);
    }
    
    public void renderCollapsedMenuFacet(FacesContext context, UIComponent component) throws IOException {
        renderFacet("collapsedMenu", context, component);
    }
    
    @Override
    protected void doEncodeChildren(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        AbstractNavbar navbar = (AbstractNavbar) component;
        
        if(navbar.isCollapsible()) {
            writer.startElement("div", component);
            writer.writeAttribute("id", component.getId()+"_collapse", null);
            writer.writeAttribute("class", "nav-collapse", null);
        }
        
        List<UIComponent> defaultChildren = new ArrayList<UIComponent>();
        List<AbstractPositionGroup> leftGroups = new ArrayList<AbstractPositionGroup>();
        List<AbstractPositionGroup> rightGroups = new ArrayList<AbstractPositionGroup>();
        
        for(UIComponent child : component.getChildren()) {
            if(child.isRendered()) {
                if(child instanceof AbstractPositionGroup) {
                    AbstractPositionGroup group = (AbstractPositionGroup) child;
                    
                    if(HorizontalPosition.left.equals(group.getHorizontal())) {
                        leftGroups.add(group);
                    } else if(HorizontalPosition.right.equals(group.getHorizontal())) {
                        rightGroups.add(group);
                    } else {
                        for(UIComponent groupChild : group.getChildren()) {
                            defaultChildren.add(groupChild);
                        }
                    }
                } else {
                    defaultChildren.add(child);
                }
            }
        }
        
        if(!leftGroups.isEmpty()) {
            for(AbstractPositionGroup group : leftGroups) {
                writer.startElement("ul", component);
                writer.writeAttribute("class", "nav pull-left", null);
                
                for(UIComponent child : group.getChildren()) {
                    encodeChild(writer, context, child);
                }
                
                writer.endElement("ul");
            }
        }
        
        if(!defaultChildren.isEmpty()) {
            writer.startElement("ul", component);
            writer.writeAttribute("class", "nav", null);
            
            for(UIComponent child : defaultChildren) {
                encodeChild(writer, context, child);
            }
            
            writer.endElement("ul");
        }
        
        if(!rightGroups.isEmpty()) {
            for(AbstractPositionGroup group : rightGroups) {
                writer.startElement("ul", component);
                writer.writeAttribute("class", "nav pull-right", null);
                
                for(UIComponent child : group.getChildren()) {
                    encodeChild(writer, context, child);
                }
                
                writer.endElement("ul");
            }
        }
        
        if(navbar.isCollapsible()) {
            writer.endElement("div");
        }
    }
    
    protected void encodeChild(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        encodeChild(writer, context, component, 0);
    }
    
    protected void encodeChild(ResponseWriter writer, FacesContext context, UIComponent component, int level) throws IOException {
        if(component.isRendered()) {
            if(component instanceof AbstractMenuGroup) {
                AbstractMenuGroup group = (AbstractMenuGroup) component;
                encodeGroupMenu(writer, context, group, level);
            } else {
                writer.startElement("li", component);
                component.encodeAll(context);
                writer.endElement("li");
            }
        }
    }
    
    protected void encodeGroupMenu(ResponseWriter writer, FacesContext context, AbstractMenuGroup groupMenu, int level) throws IOException {
        ResponseWriter response = context.getResponseWriter();
        response.startElement("li" , groupMenu);
        response.writeAttribute("class", "dropdown", null);
        
        response.startElement("a", groupMenu);
        response.writeAttribute("href", "#", null);
        response.writeAttribute("class", "dropdown-toggle", null);
        response.writeAttribute("data-toggle", "dropdown", null);
        response.write(groupMenu.getLabel()+" ");
        response.startElement("b", groupMenu);
        response.writeAttribute("class", "caret", null);
        response.endElement("b");
        response.endElement("a");
        
        response.startElement("ul", groupMenu);
        response.writeAttribute("class", level > 0 ? "subdropdown-menu" : "dropdown-menu", null);
        
        for(UIComponent child : groupMenu.getChildren()) {
            encodeChild(writer, context, child, level+1);
        }
        
        response.endElement("ul");
        
        response.endElement("li");
    }
}

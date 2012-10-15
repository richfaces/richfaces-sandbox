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
package org.richfaces.bootstrap.ui.navbar;

import javax.faces.component.UIPanel;

import org.richfaces.bootstrap.semantic.RenderMenuFacetCapable;
import org.richfaces.bootstrap.semantic.RenderPositionFacetCapable;
import org.richfaces.bootstrap.semantic.RenderSeparatorFacetCapable;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;

/**
 * Base class for the navbar component
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 * @author <a href="http://community.jboss.org/people/electrobolt">Adrian Chia</a>
 */
@JsfComponent(
        type = AbstractNavbar.COMPONENT_TYPE,
        family = AbstractNavbar.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = NavbarRendererBase.RENDERER_TYPE),
        tag = @Tag(name="navbar"))
public abstract class AbstractNavbar extends UIPanel implements RenderMenuFacetCapable,
    RenderPositionFacetCapable, RenderSeparatorFacetCapable {
    public static final String COMPONENT_FAMILY = "org.richfaces.bootstrap.Navbar";
    public static final String COMPONENT_TYPE = "org.richfaces.bootstrap.Navbar";

    /**
     * A simple text / link that display your project or brand name.
     * */
    @Attribute
    public abstract String getBrand();
    
	/**
	* If specified, fix the navbar component to the top or bottom of the viewport.
	* Possible values are <code>top</code> or <code>bottom</code>
	*/
    @Attribute
    public abstract String getFixed();
    
    /**
     * Implement a collapsing navbar if the browser window is less than 768 pixels width.
     * Default value is <code>true</code>.
     * */
    @Attribute(defaultValue = "true")
    public abstract boolean isCollapsible();
    
    /**
     * Color inversion attribute for the navbar component.
     * If true, add "navbar-inverse" class to the current navbar component.
     * Default value is <code>false</code>.
     * */
    @Attribute(defaultValue = "false")
    public abstract boolean isInverse();
    
    @Override
    public String getMenuFacetRendererType() {
        return "org.richfaces.bootstrap.NavbarMenuFacetRenderer";
    }
    
    @Override
    public String getPositionFacetRendererType() {
        return "org.richfaces.bootstrap.NavbarPositionFacetRenderer";
    }
    
    @Override
    public String getSeparatorFacetRendererType() {
        return "org.richfaces.bootstrap.NavbarSeparatorFacetRenderer";
    }
}

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
package org.richfaces.bootstrap.ui.commandButton;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.bootstrap.component.props.BootstrapScaleProps;
import org.richfaces.bootstrap.component.props.BootstrapSeverityProps;
import org.richfaces.bootstrap.component.props.CardinalPositionProps;
import org.richfaces.bootstrap.semantic.RenderSeparatorFacetCapable;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.component.AbstractActionComponent;
import org.richfaces.component.AjaxContainer;
import org.richfaces.component.MetaComponentResolver;
import org.richfaces.component.Mode;
import org.richfaces.component.attribute.AjaxProps;
import org.richfaces.component.attribute.CommandButtonProps;
import org.richfaces.component.attribute.CoreProps;
import org.richfaces.renderkit.AjaxConstants;

/**
 * Base class for the commandButton component
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@JsfComponent(
        type = AbstractCommandButton.COMPONENT_TYPE, family = AbstractCommandButton.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = CommandButtonRendererBase.RENDERER_TYPE),
        tag = @Tag(name="commandButton"))
public abstract class AbstractCommandButton extends AbstractActionComponent implements AjaxProps, CoreProps,
    CommandButtonProps, BootstrapSeverityProps, BootstrapScaleProps, CardinalPositionProps,
    RenderSeparatorFacetCapable, MetaComponentResolver {
    public static final String COMPONENT_FAMILY = "org.richfaces.bootstrap.CommandButton";
    public static final String COMPONENT_TYPE = "org.richfaces.bootstrap.CommandButton";

    /**
     * <p>Determines how the menu item requests are submitted.  Valid values:</p>
     * <ol>
     *     <li>server, the default setting, submits the form normally and completely refreshes the page.</li>
     *     <li>ajax performs an Ajax form submission, and re-renders elements specified with the render attribute.</li>
     *     <li>
     *         client causes the action and actionListener items to be ignored, and the behavior is fully defined by
     *         the nested components instead of responses from submissions
     *     </li>
     * </ol>
     */
    @Attribute(defaultValue = "Mode.ajax")
    public abstract Mode getMode();

    /**
     * The icon to be displayed with the CommandButton
     */
    @Attribute
    public abstract String getIcon();

    /**
     * HMTL tag used to create the button. Can be either "button" or "input".  If not specified, the default value is
     * "button".
     */
    @Attribute(defaultValue = "button")
    public abstract String getTag();

    @Attribute(hidden = true)
    public abstract Object getValue();
    
    @Attribute
    public abstract String getColor();
    
    public String resolveClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {
        return null;
    }

    public String substituteUnresolvedClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {
        if (AjaxContainer.META_COMPONENT_ID.equals(metaComponentId)) {
            return AjaxConstants.FORM;
        }
        return null;
    }
    
    @Override
    public String getSeparatorFacetRendererType() {
        return "org.richfaces.bootstrap.CommandButtonSeparatorFacetRenderer";
    }
    
    public boolean hasFacet(String facetName) {
        return getFacet(facetName) != null && getFacet(facetName).isRendered();
    }

    public List<UIComponent> getFacetChildren(String facetName) {
        UIComponent facet = getFacet(facetName);
        if(facet != null && facet.isRendered()) {
            if("javax.faces.Panel".equals(facet.getFamily())) {
                return facet.getChildren();
            } else {
                List<UIComponent> children = new ArrayList<UIComponent>();
                children.add(facet);
                return children;
            }
        }
        return null;
    }

    public enum Facets {
        icon,
        split
    }
}
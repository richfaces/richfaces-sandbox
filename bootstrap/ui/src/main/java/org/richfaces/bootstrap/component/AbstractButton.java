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
package org.richfaces.bootstrap.component;

import org.richfaces.bootstrap.renderkit.ButtonRendererBase;
import org.richfaces.cdk.annotations.*;
import org.richfaces.component.AbstractActionComponent;
import org.richfaces.component.Mode;
import org.richfaces.component.attribute.CoreProps;

/**
 * Base class for the button component
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@JsfComponent(
        type = AbstractButton.COMPONENT_TYPE, family = AbstractButton.COMPONENT_FAMILY,
        facets = {@Facet(name = "icon", generate = false)},
        renderer = @JsfRenderer(type = ButtonRendererBase.RENDERER_TYPE),
        tag = @Tag(name="button"),
        attributes = {"events-mouse-props.xml", "core-props.xml", "ajax-props.xml"})
abstract public class AbstractButton extends AbstractActionComponent implements CoreProps {
    public static final String COMPONENT_FAMILY = "org.richfaces.bootstrap.Button";
    public static final String COMPONENT_TYPE = "org.richfaces.bootstrap.Button";

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
     * The icon to be displayed with the button
     */
    @Attribute
    public abstract String getIcon();

    /**
     * Disables the button component, so it will not be clickable
     */
    @Attribute
    public abstract boolean isDisabled();


    @Attribute(hidden = true)
    public abstract Object getValue();

    public enum Facets {
        icon
    }
}
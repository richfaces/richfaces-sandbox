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

import org.richfaces.bootstrap.renderkit.CommandButtonRendererBase;
import org.richfaces.cdk.annotations.*;
import org.richfaces.component.AbstractActionComponent;
import org.richfaces.component.Mode;
import org.richfaces.component.attribute.CoreProps;

/**
 * Base class for the commandButton component
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@JsfComponent(
        type = AbstractCommandButton.COMPONENT_TYPE, family = AbstractCommandButton.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = CommandButtonRendererBase.RENDERER_TYPE),
        tag = @Tag(name="commandButton"),
        attributes = {"ajax-props.xml", "command-button-props.xml", "core-props.xml" })
abstract public class AbstractCommandButton extends AbstractActionComponent implements CoreProps {
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


    /**
     * Disables the CommandButton component, so it will not be clickable
     */
    @Attribute
    public abstract boolean isDisabled();


    @Attribute(hidden = true)
    public abstract Object getValue();

    public enum Facets {
        icon
    }
}
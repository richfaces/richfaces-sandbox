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
package org.richfaces.bootstrap.ui.tooltip;

import javax.faces.component.UIOutput;

import org.richfaces.bootstrap.component.props.TooltipProps;
import org.richfaces.bootstrap.javascript.BootstrapJSPlugin;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.component.attribute.CoreProps;

/**
 * Base class for the tooltip component
 * 
 * @author Lukas Fryc
 */
@BootstrapJSPlugin(name = "tooltip")
@JsfComponent(type = AbstractTooltip.COMPONENT_TYPE, family = AbstractTooltip.COMPONENT_FAMILY, renderer = @JsfRenderer(type = TooltipRendererBase.RENDERER_TYPE), tag = @Tag(name = "tooltip"))
public abstract class AbstractTooltip extends UIOutput implements TooltipProps, CoreProps {
    public static final String COMPONENT_FAMILY = "org.richfaces.bootstrap.Tooltip";
    public static final String COMPONENT_TYPE = "org.richfaces.bootstrap.Tooltip";

}

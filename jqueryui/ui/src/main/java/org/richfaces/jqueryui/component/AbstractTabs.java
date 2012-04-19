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
package org.richfaces.jqueryui.component;

import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.jqueryui.renderkit.TabsRendererBase;

import javax.faces.component.UIPanel;

/**
 * Base class for the tabs component
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@JsfComponent(
        type = AbstractTabs.COMPONENT_TYPE,
        family = AbstractTabs.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = TabsRendererBase.RENDERER_TYPE),
        tag = @Tag(name="tabs"))
abstract public class AbstractTabs extends UIPanel {
    public static final String COMPONENT_FAMILY = "org.richfaces.Tabs";
    public static final String COMPONENT_TYPE = "org.richfaces.jqueryui.Tabs";

}

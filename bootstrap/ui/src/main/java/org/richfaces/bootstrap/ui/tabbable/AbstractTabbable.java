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
package org.richfaces.bootstrap.ui.tabbable;

import javax.faces.component.UIPanel;

import org.richfaces.bootstrap.semantic.RenderMenuFacetCapable;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;

/**
 * Base class for the tabbable component
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@JsfComponent(
        type = AbstractTabbable.COMPONENT_TYPE,
        family = AbstractTabbable.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = TabbableRendererBase.RENDERER_TYPE),
        tag = @Tag(name="tabbable"))
public abstract class AbstractTabbable extends UIPanel implements RenderMenuFacetCapable {
    public static final String COMPONENT_FAMILY = "org.richfaces.bootstrap.Tabs";
    public static final String COMPONENT_TYPE = "org.richfaces.bootstrap.Tabs";
    public static final String ACTIVE_INDEX_DEFAULT = "0";
    public static final String INDEX_SEPARATOR_DEFAULT = ".";

    @Attribute
    public abstract String getActiveName();
    
    @Attribute(defaultValue = ACTIVE_INDEX_DEFAULT)
    public abstract String getActiveIndex();
    
    @Attribute(defaultValue = INDEX_SEPARATOR_DEFAULT)
    public abstract String getIndexSeparator();
    
    @Override
    public String getMenuFacetRendererType() {
        return "org.richfaces.bootstrap.TabbableMenuFacetRenderer";
    }
}

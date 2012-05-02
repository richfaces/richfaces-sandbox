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

import org.richfaces.bootstrap.renderkit.CodeblockRendererBase;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.component.attribute.CoreProps;

import javax.faces.component.UIPanel;

/**
 * Base class for the codeblock component
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@JsfComponent(
        type = AbstractCodeblock.COMPONENT_TYPE,
        family = AbstractCodeblock.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = CodeblockRendererBase.RENDERER_TYPE),
        tag = @Tag(name="codeblock"),
        attributes = "core-props.xml")
public abstract class AbstractCodeblock extends UIPanel implements CoreProps {
    public static final String COMPONENT_FAMILY = "org.richfaces.bootstrap.Codeblock";
    public static final String COMPONENT_TYPE = "org.richfaces.bootstrap.Codeblock";

    @Attribute(defaultValue = "true")
    public abstract boolean isLinenums();
}

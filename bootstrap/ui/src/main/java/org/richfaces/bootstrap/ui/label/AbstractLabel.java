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
package org.richfaces.bootstrap.ui.label;

import javax.faces.component.UIOutput;

import org.richfaces.bootstrap.component.BootstrapSeverity;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.ui.attribute.CoreProps;

/**
 * Base class for the label component
 * 
 * @author <a href="http://pauldijou.fr">Paul Dijou</a>
 *
 */
@JsfComponent(
        type = AbstractLabel.COMPONENT_TYPE,
        family = AbstractLabel.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = LabelRendererBase.RENDERER_TYPE),
        tag = @Tag(name="label"))
public abstract class AbstractLabel extends UIOutput implements CoreProps {
    public static final String COMPONENT_FAMILY = "org.richfaces.bootstrap.Label";
    public static final String COMPONENT_TYPE = "org.richfaces.bootstrap.Label";
    
    @Attribute(suggestedValue = BootstrapSeverity.SUCCESS + ","
            + BootstrapSeverity.INFO + ","
            + BootstrapSeverity.WARNING + ","
            + BootstrapSeverity.IMPORTANT + ","
            + BootstrapSeverity.INVERSE)
    public abstract String getSeverity();
}

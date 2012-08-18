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
package org.richfaces.bootstrap.semantic;

import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.component.attribute.CoreProps;

/**
 * Base class for the body component
 * 
 * @author <a href="http://pauldijou.fr">Paul Dijou</a>
 * 
 */
@JsfComponent(
        type = AbstractBodyFacet.COMPONENT_TYPE,
        family = AbstractBodyFacet.COMPONENT_FAMILY,
        tag = @Tag(name="bodyFacet"))
public abstract class AbstractBodyFacet extends AbstractSemanticComponentBase<RenderBodyFacetCapable> implements CoreProps {
    public static final String COMPONENT_FAMILY = "org.richfaces.bootstrap.BodyFacet";
    public static final String COMPONENT_TYPE = "org.richfaces.bootstrap.BodyFacet";
    
    @Override
    public Class<RenderBodyFacetCapable> getRendererCapability() {
        return RenderBodyFacetCapable.class;
    }
    
    @Override
    public String getRendererType(RenderBodyFacetCapable container) {
        return container.getBodyFacetRendererType();
    }
    
}

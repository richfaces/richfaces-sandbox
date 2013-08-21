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
package org.richfaces.bootstrap.ui.modal;

import javax.faces.component.UIPanel;

import org.richfaces.bootstrap.javascript.BootstrapJSPlugin;
import org.richfaces.bootstrap.semantic.RenderBodyFacetCapable;
import org.richfaces.bootstrap.semantic.RenderFooterFacetCapable;
import org.richfaces.bootstrap.semantic.RenderHeaderFacetCapable;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.ui.attribute.CoreProps;

/**
 * Base class for the modal component
 * 
 * @author <a href="http://pauldijou.fr">Paul Dijou</a>
 * 
 */
@BootstrapJSPlugin(name = "modal")
@JsfComponent(
        type = AbstractModal.COMPONENT_TYPE,
        family = AbstractModal.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = ModalRendererBase.RENDERER_TYPE),
        tag = @Tag(name="modal"))
public abstract class AbstractModal extends UIPanel implements CoreProps, RenderHeaderFacetCapable, RenderBodyFacetCapable,
        RenderFooterFacetCapable {
    public static final String COMPONENT_FAMILY = "org.richfaces.bootstrap.Modal";
    public static final String COMPONENT_TYPE = "org.richfaces.bootstrap.Modal";
    
    public enum Facets {
        header,
        footer
    }
    
    @Attribute
    public abstract String getHeader();

    /**
     * Defines whenever the modal should be closeable (sets whenever the X button should be available).
     * @return
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isCloseable();
    
    @Attribute(defaultValue = "", suggestedValue = "fade")
    public abstract String getEffect();
    
    private boolean custom = false;
    
    public boolean hasFacet(String facetName) {
        return getFacet(facetName) != null && getFacet(facetName).isRendered();
    }
    
    @Override
    public String getHeaderFacetRendererType() {
        setCustom(true);
        return "org.richfaces.bootstrap.ModalHeaderFacetRenderer";
    }
    
    @Override
    public String getBodyFacetRendererType() {
        setCustom(true);
        return "org.richfaces.bootstrap.ModalBodyFacetRenderer";
    }
    
    @Override
    public String getFooterFacetRendererType() {
        setCustom(true);
        return "org.richfaces.bootstrap.ModalFooterFacetRenderer";
    }
    
    public boolean isCustom() {
        return custom;
    }
    
    public void setCustom(boolean custom) {
        this.custom = custom;
    }
}

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

import javax.faces.component.UIPanel;

import org.richfaces.bootstrap.RenderBodyGroupCapable;
import org.richfaces.bootstrap.RenderFooterGroupCapable;
import org.richfaces.bootstrap.RenderHeaderGroupCapable;
import org.richfaces.bootstrap.javascript.BootstrapJSPlugin;
import org.richfaces.bootstrap.renderkit.ModalRendererBase;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.component.attribute.CoreProps;

/**
 * Base class for the modal component
 * 
 * @author <a href="http://www.pauldijou.fr">Paul Dijou</a>
 * 
 */
@BootstrapJSPlugin(name = "modal")
@JsfComponent(
        type = AbstractModal.COMPONENT_TYPE,
        family = AbstractModal.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = ModalRendererBase.RENDERER_TYPE),
        tag = @Tag(name="modal"))
public abstract class AbstractModal extends UIPanel implements CoreProps, RenderHeaderGroupCapable, RenderBodyGroupCapable,
        RenderFooterGroupCapable {
    public static final String COMPONENT_FAMILY = "org.richfaces.bootstrap.Modal";
    public static final String COMPONENT_TYPE = "org.richfaces.bootstrap.Modal";

    @Attribute
    public abstract String getHeader();

    /**
     * Defines whenever the modal should be closeable (sets whenever the X button should be available).
     * @return
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isCloseable();

    @Attribute(defaultValue = "false", hidden = true)
    public abstract boolean isCustom();
    
    public abstract void setCustom(boolean custom);

    @Attribute(defaultValue = "", suggestedValue = "fade")
    public abstract String getEffect();

    @Attribute
    public abstract HorizontalPosition getCancelPosition();

    public boolean hasLeftCancelButton() {
        return HorizontalPosition.left.equals(getCancelPosition());
    }

    public boolean hasRightCancelButton() {
        return HorizontalPosition.right.equals(getCancelPosition());
    }

    public boolean hasFacet(String facetName) {
        return getFacet(facetName) != null && getFacet(facetName).isRendered();
    }

    public enum Facets {
        header,
        footer
    }
    
    @Override
    public String getHeaderGroupRendererType() {
        return "org.richfaces.bootstrap.ModalHeaderGroupRenderer";
    }
    
    @Override
    public String getBodyGroupRendererType() {
        return "org.richfaces.bootstrap.ModalBodyGroupRenderer";
    }
    
    @Override
    public String getFooterGroupRendererType() {
        return "org.richfaces.bootstrap.ModalFooterGroupRenderer";
    }
    
    @Override
    public void setCustomHeaderGroup(boolean custom) {
        setCustom(custom);
    }
    
    @Override
    public void setCustomBodyGroup(boolean custom) {
        setCustom(custom);
        
    }
    
    @Override
    public void setCustomFooterGroup(boolean custom) {
        setCustom(custom);
    }
}

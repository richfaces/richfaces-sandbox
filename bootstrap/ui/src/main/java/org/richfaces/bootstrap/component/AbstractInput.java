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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UIInput;

import org.richfaces.bootstrap.renderkit.InputRendererBase;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.component.attribute.CoreProps;

/**
 * Base class for the label component
 * 
 * @author <a href="http://www.pauldijou.fr">Paul Dijou</a>
 *
 */
@JsfComponent(
        type = AbstractInput.COMPONENT_TYPE,
        family = AbstractInput.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = InputRendererBase.RENDERER_TYPE),
        tag = @Tag(name="input"),
        attributes = "core-props.xml")
abstract public class AbstractInput extends UIInput implements CoreProps {
    public static final String COMPONENT_FAMILY = "org.richfaces.bootstrap.Input";
    public static final String COMPONENT_TYPE = "org.richfaces.bootstrap.Input";
    
    @Attribute
    abstract public String getPlaceholder();
    
    @Attribute
    abstract public boolean isReadonly();
    
    @Attribute
    abstract public boolean isDisabled();
    
    @Attribute
    abstract public BootstrapSize getScale();
    
    @Attribute
    abstract public BootstrapSeverity getSeverity();
    
    public boolean hasFacet(String facetName) {
        return getFacet(facetName) != null && getFacet(facetName).isRendered();
    }
    
    public boolean hasStyleClass(String styleClassName) {
        return containsStyleClass(getAttributes().get("styleClass"), styleClassName);
    }
    
    public boolean containsStyleClass(Object styleClassAttribute, String styleClassName) {
        if(styleClassAttribute == null) {
            return false;
        }
        
        StringBuilder regex = new StringBuilder();
        
        // the styleClass starts with "styleClassName "
        regex.append("^").append(styleClassName).append(" ");
        regex.append("|");
        // the styleClass contains " styleClassName "
        regex.append(" ").append(styleClassName).append(" ");
        regex.append("|");
        // the styleClass ends with " styleClassName"
        regex.append(" ").append(styleClassName).append("$");
        regex.append("|");
        // the styleClass is exactly "styleClassName"
        regex.append("^").append(styleClassName).append("$");
        
        Pattern pattern = Pattern.compile(regex.toString());
        Matcher matcher = pattern.matcher(styleClassAttribute.toString());
        
        return matcher.find();
    }
}

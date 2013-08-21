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
package org.richfaces.bootstrap.ui.alert;

import javax.faces.component.UIPanel;

import org.richfaces.bootstrap.component.BootstrapSeverity;
import org.richfaces.bootstrap.javascript.BootstrapJSPlugin;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.ui.attribute.CoreProps;

/**
 * Base class for the alert component
 * 
 * @author <a href="http://pauldijou.fr">Paul Dijou</a>
 * 
 */
@BootstrapJSPlugin(name = "alert")
@JsfComponent(
        type = AbstractAlert.COMPONENT_TYPE,
        family = AbstractAlert.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = AlertRendererBase.RENDERER_TYPE),
        tag = @Tag(name="alert"))
public abstract class AbstractAlert extends UIPanel implements CoreProps {
    public static final String COMPONENT_FAMILY = "org.richfaces.bootstrap.Alert";
    public static final String COMPONENT_TYPE = "org.richfaces.bootstrap.Alert";
    
    /**
     * Severity of the alert. Possible values: "success", "info", "error" or "danger".
     */
    @Attribute(suggestedValue = BootstrapSeverity.SUCCESS + ","
            + BootstrapSeverity.INFO + ","
            + BootstrapSeverity.ERROR + ","
            + BootstrapSeverity.DANGER)
    public abstract String getSeverity();
    
    /**
     * Equivalent of the CSS 'display' attribute. Supported value: "block".
     */
    @Attribute
    public abstract String getLayout();
    
    /**
     * Header value of the alert.
     */
    @Attribute
    public abstract String getHeader();
    
    /**
     * If set to "true" the alert can be closed with a button. Default: "false."
     */
    @Attribute
    public abstract boolean isClosable();
    
    /**
     * CSS class(es) to be applied to the header.
     */
    @Attribute
    public abstract String getHeaderStyleClass();
    
    /**
     * If set will show an icon in the corner of the alert. Can be set to "default" which will apply a pre-defined icon based on the severity of the alert.
     * Shows no icon by default.
     */
    @Attribute
    public abstract String getIcon();
    
}

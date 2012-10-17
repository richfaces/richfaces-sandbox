package org.richfaces.bootstrap.ui.breadcrumb;

import javax.faces.component.UIOutput;

import org.richfaces.bootstrap.javascript.BootstrapJSPlugin;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;

@BootstrapJSPlugin(name = "breadcrumb")
@JsfComponent(type = AbstractBreadcrumb.COMPONENT_TYPE, family = AbstractBreadcrumb.COMPONENT_FAMILY, renderer = @JsfRenderer(type = BreadcrumbRendererBase.RENDERER_TYPE), tag = @Tag(name = "breadcrumb"))
public abstract class AbstractBreadcrumb extends UIOutput {

    public static final String COMPONENT_FAMILY = "org.richfaces.bootstrap.Breadcrumb";
    public static final String COMPONENT_TYPE = "org.richfaces.bootstrap.Breadcrumb";

    public static final String SEPARATOR_DEFAULT = "/";

    /**
     * The separator is a character between each breadcrumb's component, default value is '/'
     */
    @Attribute(defaultValue = SEPARATOR_DEFAULT, suggestedValue = SEPARATOR_DEFAULT)
    public abstract String getSeparator();

}

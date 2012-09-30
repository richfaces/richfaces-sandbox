package org.richfaces.bootstrap.ui.breadcrumb;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.richfaces.bootstrap.javascript.BootstrapJSPlugin;
import org.richfaces.bootstrap.semantic.AbstractMenuFacet;
import org.richfaces.bootstrap.semantic.AbstractPositionFacet;
import org.richfaces.bootstrap.ui.modal.AbstractModal;
import org.richfaces.bootstrap.ui.modal.ModalRendererBase;
import org.richfaces.bootstrap.ui.navbar.AbstractNavbar;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;

import java.io.IOException;

@BootstrapJSPlugin(name = "breadcrumb")
@JsfComponent(type = AbstractBreadcrumb.COMPONENT_TYPE, family = AbstractBreadcrumb.COMPONENT_FAMILY, renderer = @JsfRenderer(type = BreadcrumbRendererBase.RENDERER_TYPE),
        tag = @Tag(name = "breadcrumb"))
public abstract class AbstractBreadcrumb extends UIOutput {


    public static final String COMPONENT_FAMILY = "org.richfaces.bootstrap.Breadcrumb";
    public static final String COMPONENT_TYPE = "org.richfaces.bootstrap.Breadcrumb";


}

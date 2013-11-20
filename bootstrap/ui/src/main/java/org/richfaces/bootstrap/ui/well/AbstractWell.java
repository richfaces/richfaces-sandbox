package org.richfaces.bootstrap.ui.well;

import org.richfaces.bootstrap.ui.navitem.NavItemRendererBase;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.ui.attribute.CoreProps;

import javax.faces.component.UIOutput;

/**
 * base class for navItem component
 *
 * @author Lukas Eichler
 */
@JsfComponent(
        type = AbstractWell.COMPONENT_TYPE,
        family = AbstractWell.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = NavItemRendererBase.RENDERER_TYPE),
        tag = @Tag(name = "navItem"))
public abstract class AbstractWell extends UIOutput implements CoreProps {
    public static final String COMPONENT_FAMILY = "org.richfaces.bootstrap.Well";
    public static final String COMPONENT_TYPE = "org.richfaces.bootstrap.Well";

}

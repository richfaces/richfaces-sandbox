package org.richfaces.bootstrap.ui.navlist;

import org.richfaces.bootstrap.ui.navitem.AbstractNavItem;
import org.richfaces.bootstrap.ui.pageritem.AbstractPagerItem;
import org.richfaces.cdk.annotations.*;
import org.richfaces.ui.attribute.CoreProps;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.webapp.FacetTag;

/**
 * base class for navlist component
 *
 * @author Lukas Eichler
 */
@JsfComponent(
        type = AbstractNavlist.COMPONENT_TYPE,
        family = AbstractNavlist.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = NavlistRendererBase.RENDERER_TYPE),
        tag = @Tag(name = "navlist"))
public abstract class AbstractNavlist extends UIOutput implements CoreProps {
    public static final String COMPONENT_FAMILY = "org.richfaces.bootstrap.Navlist";
    public static final String COMPONENT_TYPE = "org.richfaces.bootstrap.Navlist";

    public  boolean isNavItem(UIComponent component) {
        return component instanceof AbstractNavItem;
    }
}

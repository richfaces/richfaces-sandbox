package org.richfaces.bootstrap.ui.navitem;

import org.richfaces.cdk.annotations.*;
import org.richfaces.ui.attribute.CoreProps;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

/**
 * base class for navItem component
 *
 * @author Lukas Eichler
 */
@JsfComponent(
        type = AbstractNavItem.COMPONENT_TYPE,
        family = AbstractNavItem.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = NavItemRendererBase.RENDERER_TYPE),
        tag = @Tag(name = "navItem"))
public abstract class AbstractNavItem extends UIOutput implements CoreProps {
    public static final String COMPONENT_FAMILY = "org.richfaces.bootstrap.NavItem";
    public static final String COMPONENT_TYPE = "org.richfaces.bootstrap.NavItem";
    public static final String ACTIVE = "active";
    public static final String HEADER = "header";
    public static final String HEADER_CLASS = "nav-header";

    @Attribute(suggestedValue = ACTIVE + ","
            + HEADER, description = @Description("Type of the item"))
    public abstract String getType();

    public String generateClass() {
        StringBuilder sb = new StringBuilder();
        if(getType() != null && !getType().isEmpty()) {
            if(getType().equals(ACTIVE)) {
                sb.append(ACTIVE);
            } else if(getType().equals(HEADER)) {
                sb.append(HEADER_CLASS);
            }
        }
        if(getStyleClass() != null) {
            sb.append(getStyleClass());
        }

        return sb.toString();
    }
}

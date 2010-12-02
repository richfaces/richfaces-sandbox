package org.richfaces.component;

import javax.faces.component.UIComponentBase;

import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.renderkit.html.MenuSeparatorRendererBase;

@JsfComponent(family = AbstractDropDownMenu.COMPONENT_FAMILY, type = AbstractMenuSeparator.COMPONENT_TYPE, 
    renderer=@JsfRenderer(type = MenuSeparatorRendererBase.RENDERER_TYPE), tag = @Tag(name="menuSeparator"))
public abstract class AbstractMenuSeparator extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.richfaces.MenuSeparator";
}

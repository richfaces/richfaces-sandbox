package org.richfaces.bootstrap.ui.pageritem;

import org.richfaces.bootstrap.ui.pager.PagerRendererBase;
import org.richfaces.cdk.annotations.*;
import org.richfaces.ui.attribute.CoreProps;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;

/**
 * base class for pagerItem component
 *
 * @author Lukas Eichler
 */
@JsfComponent(
        type = AbstractPagerItem.COMPONENT_TYPE,
        family = AbstractPagerItem.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = PagerItemRendererBase.RENDERER_TYPE),
        tag = @Tag(name = "pagerItem"))
public abstract class AbstractPagerItem extends UIInput implements CoreProps {
    public static final String COMPONENT_FAMILY = "org.richfaces.bootstrap.PagerItem";
    public static final String COMPONENT_TYPE = "org.richfaces.bootstrap.PagerItem";

    @Attribute(suggestedValue = "left" + ","
            + "right", description = @Description("Defines the aligment of the button. Default value is center. "))
    public abstract String getAlign();

    @Attribute(description = @Description("Button active"))
    public abstract boolean isDisabled();

}

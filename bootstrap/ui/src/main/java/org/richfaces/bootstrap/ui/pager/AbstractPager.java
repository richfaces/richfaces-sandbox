package org.richfaces.bootstrap.ui.pager;

import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.ui.attribute.CoreProps;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import java.util.ArrayList;
import java.util.List;

/**
 * base class for pager component
 *
 * @author Lukas Eichler
 */
@JsfComponent(
        type = AbstractPager.COMPONENT_TYPE,
        family = AbstractPager.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = PagerRendererBase.RENDERER_TYPE),
        tag = @Tag(name = "pager"))
public abstract class AbstractPager extends UIInput implements CoreProps {
    public static final String COMPONENT_FAMILY = "org.richfaces.bootstrap.Pager";
    public static final String COMPONENT_TYPE = "org.richfaces.bootstrap.Pager";

}

package org.richfaces.component;

import javax.faces.component.UIPanel;

import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.renderkit.html.LayoutRenderer;

@JsfComponent(tag = @Tag(name = "layout", type = TagType.Facelets),
        renderer = @JsfRenderer(family = AbstractLayout.COMPONENT_FAMILY, type = LayoutRenderer.RENDERER_TYPE),
        attributes = {"core-props.xml"}
)
public abstract class AbstractLayout extends UIPanel {
// ------------------------------ FIELDS ------------------------------

    public static final String COMPONENT_FAMILY = "org.richfaces.Layout";

    public static final String COMPONENT_TYPE = "org.richfaces.Layout";
}

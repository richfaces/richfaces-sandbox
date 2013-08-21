package org.richfaces.component;

import javax.faces.component.UIComponentBase;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Description;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.renderkit.html.LayoutPanelRenderer;

@JsfComponent(tag = @Tag(name = "layoutPanel", type = TagType.Facelets),
        renderer = @JsfRenderer(family = AbstractLayoutPanel.COMPONENT_FAMILY, type = LayoutPanelRenderer.RENDERER_TYPE),
        attributes = {"core-props.xml"}
)
public abstract class AbstractLayoutPanel extends UIComponentBase {
// ------------------------------ FIELDS ------------------------------

    public static final String COMPONENT_FAMILY = "org.richfaces.LayoutPanel";

    public static final String COMPONENT_TYPE = "org.richfaces.LayoutPanel";

// -------------------------- OTHER METHODS --------------------------

    /**
     * Get placement position type name
     *
     * @return
     */
    @Attribute(required = true, description = @Description("Positions the component relative to the &lt;rich:layout/&gt; component. Possible values are top, left, right, center, bottom."))
    public abstract LayoutPosition getPosition();


    /**
     * Get Panel width.
     *
     * @return
     */
    @Attribute(description = @Description("Sets the width of the layout area"))
    public abstract String getWidth();
}

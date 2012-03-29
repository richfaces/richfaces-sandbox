package org.richfaces.component;

import org.richfaces.cdk.annotations.*;
import org.richfaces.renderkit.html.ImageSelectToolRenderer;

import javax.faces.component.UIInput;

@JsfComponent(tag = @Tag(name = "imageSelectTool", type = TagType.Facelets),
        renderer = @JsfRenderer(family = AbstractImageSelectTool.COMPONENT_FAMILY, type = ImageSelectToolRenderer.RENDERER_TYPE))
public abstract class AbstractImageSelectTool extends UIInput {
// ------------------------------ FIELDS ------------------------------

    public static final String COMPONENT_FAMILY = "org.richfaces.ImageSelectTool";

    public static final String COMPONENT_TYPE = "org.richfaces.ImageSelectTool";

// -------------------------- OTHER METHODS --------------------------

    @Attribute
    public abstract Double getAspectRatio();

    @Attribute
    public abstract String getBackgroundColor();

    @Attribute
    public abstract Double getBackgroundOpacity();

    @Attribute
    public abstract Integer getMaxHeight();

    @Attribute
    public abstract Integer getMaxWidth();

    @Attribute
    public abstract Integer getMinHeight();

    @Attribute
    public abstract Integer getMinWidth();

    @Attribute(events = @EventName(value = "change"))
    public abstract String getOnchange();

    @Attribute(events = @EventName(value = "select", defaultEvent = true))
    public abstract String getOnselect();

    @Attribute
    public abstract String getTarget();

    @Attribute
    public abstract Integer getTrueSizeHeight();

    @Attribute
    public abstract Integer getTrueSizeWidth();

    @Attribute
    public abstract String getWidgetVar();
}

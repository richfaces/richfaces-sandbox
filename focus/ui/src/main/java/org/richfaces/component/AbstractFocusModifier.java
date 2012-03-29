package org.richfaces.component;

import org.richfaces.cdk.annotations.*;
import org.richfaces.renderkit.html.HtmlFocusModifierRenderer;

import javax.faces.component.UIComponentBase;

@JsfComponent(tag = @Tag(name = "focusModifier", type = TagType.Facelets),
        renderer = @JsfRenderer(family = AbstractFocusModifier.COMPONENT_FAMILY, type = HtmlFocusModifierRenderer.RENDERER_TYPE))
public abstract class AbstractFocusModifier extends UIComponentBase {
// ------------------------------ FIELDS ------------------------------

    public static final String COMPONENT_FAMILY = "org.richfaces.Focus";

    public static final String COMPONENT_TYPE = "org.richfaces.FocusModifier";

// -------------------------- OTHER METHODS --------------------------

    @Attribute
    public abstract Integer getPriority();

    @Attribute
    public abstract String getSuffix();

    @Attribute
    public abstract String getTargetClientId();

    @Attribute(defaultValue = "false")
    public abstract boolean isSkipped();
}

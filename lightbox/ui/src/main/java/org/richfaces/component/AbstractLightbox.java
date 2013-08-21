package org.richfaces.component;

import javax.faces.component.UIComponentBase;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.renderkit.html.LightboxRenderer;

@JsfComponent(tag = @Tag(name = "lightbox", type = TagType.Facelets),
    renderer = @JsfRenderer(family = AbstractLightbox.COMPONENT_FAMILY, type = LightboxRenderer.RENDERER_TYPE),
    attributes = {"core-props.xml"}
)
public abstract class AbstractLightbox extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.richfaces.Lightbox";

    public static final String COMPONENT_FAMILY = "org.richfaces.Lightbox";

    public static final int DEFAULT_CONTAINER_BORDER_SIZE = 10;

    public static final int DEFAULT_CONTAINER_RESIZE_SPEED = 400;

    public static final boolean DEFAULT_FIXED_NAVIGATION = false;

    public static final String DEFAULT_KEY_TO_CLOSE = "c";

    public static final String DEFAULT_KEY_TO_NEXT = "n";

    public static final String DEFAULT_KEY_TO_PREV = "p";

    public static final String DEFAULT_TXT_IMAGE = "Image";

    public static final String DEFAULT_TXT_OF = "of";

    public static final String DEFAULT_OVERLAY_BG_COLOR = "#000";

    public static final double DEFAULT_OVERLAY_OPACITY = .8;

    @Attribute(required = true)
    public abstract String getSelector();

    @Attribute
    public abstract String getOverlayBgColor();

    @Attribute
    public abstract Double getOverlayOpacity();

    @Attribute
    public abstract Boolean getFixedNavigation();

    @Attribute
    public abstract Integer getContainerBorderSize();

    @Attribute
    public abstract Integer getContainerResizeSpeed();

    @Attribute
    public abstract String getTxtImage();

    @Attribute
    public abstract String getTxtOf();

    @Attribute
    public abstract String getKeyToClose();

    @Attribute
    public abstract String getKeyToPrev();

    @Attribute
    public abstract String getKeyToNext();

    @Attribute
    public abstract String getImageBlank();

    @Attribute
    public abstract String getImageLoading();

    @Attribute
    public abstract String getImageBtnNext();

    @Attribute
    public abstract String getImageBtnPrev();

    @Attribute
    public abstract String getImageBtnClose();

}

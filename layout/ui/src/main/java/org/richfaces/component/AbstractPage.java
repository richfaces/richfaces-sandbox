package org.richfaces.component;

import javax.faces.component.UIPanel;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.renderkit.AbstractPageRenderer;
import org.richfaces.skin.SkinFactory;

@JsfComponent(tag = @Tag(name = "page", type = TagType.Facelets),
        renderer = @JsfRenderer(family = AbstractPage.COMPONENT_FAMILY, type = AbstractPageRenderer.RENDERER_TYPE),
        attributes = {"core-props.xml"}
)
public abstract class AbstractPage extends UIPanel {
// ------------------------------ FIELDS ------------------------------

    public static final String COMPONENT_FAMILY = "org.richfaces.Page";

    public static final String COMPONENT_TYPE = "org.richfaces.Page";

// --------------------- GETTER / SETTER METHODS ---------------------

    @Override
    public String getRendererType() {
        String theme = getTheme();
        String rendererType = null;
        if (null != theme && theme.length() > 0) {
            rendererType = SkinFactory.getInstance().getTheme(getFacesContext(), theme).getRendererType();
        }
        if (null == rendererType) {
            rendererType = super.getRendererType();
        }
        return rendererType;
    }

// -------------------------- OTHER METHODS --------------------------

    @Attribute
    public abstract String getContentType();

    @Attribute
    public abstract String getMarkupType();

    @Attribute
    public abstract String getNamespace();

    @Attribute(events = @EventName(value = "contextmenu", defaultEvent = true))
    public abstract String getOncontextmenu();

    @Attribute
    public abstract String getPageTitle();

    @Attribute
    public abstract LayoutPosition getSidebarPosition();

    @Attribute
    public abstract Integer getSidebarWidth();

    /**
     * Get Page theme name
     *
     * @return page theme
     */
    @Attribute
    public abstract String getTheme();

    @Attribute
    public abstract Integer getWidth();
}

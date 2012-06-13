package org.richfaces.renderkit.html;

import org.ajax4jsf.javascript.JSFunction;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractLightbox;
import org.richfaces.renderkit.HtmlConstants;
import org.richfaces.renderkit.RendererBase;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@JsfRenderer(family = AbstractLightbox.COMPONENT_FAMILY, type = LightboxRenderer.RENDERER_TYPE)
@ResourceDependencies({
    @ResourceDependency(name = "base-component.reslib", library = "org.richfaces", target = "head"),
    @ResourceDependency(name = "jquery.lightbox.js", target = "head"),
    @ResourceDependency(name = "richfaces.lightbox.js", target = "head"),
    @ResourceDependency(name = "jquery.lightbox.css", target = "head")
})
public class LightboxRenderer extends RendererBase {

    public static final String RENDERER_TYPE = "org.richfaces.Lightbox";

    private static final Map<String, Object> DEFAULTS;

    /**
     * Following defaults are be used by addOptionIfSetAndNotDefault
     */
    static {
        Map<String, Object> defaults = new HashMap<String, Object>();
        defaults.put("overlayBgColor", AbstractLightbox.DEFAULT_OVERLAY_BG_COLOR);
        defaults.put("overlayOpacity", AbstractLightbox.DEFAULT_OVERLAY_OPACITY);
        defaults.put("containerBorderSize", AbstractLightbox.DEFAULT_CONTAINER_BORDER_SIZE);
        defaults.put("containerResizeSpeed", AbstractLightbox.DEFAULT_CONTAINER_RESIZE_SPEED);
        defaults.put("fixedNavigation", AbstractLightbox.DEFAULT_FIXED_NAVIGATION);
        defaults.put("keyToClose", AbstractLightbox.DEFAULT_KEY_TO_CLOSE);
        defaults.put("keyToNext", AbstractLightbox.DEFAULT_KEY_TO_NEXT);
        defaults.put("keyToPrev", AbstractLightbox.DEFAULT_KEY_TO_PREV);
        defaults.put("txtImage", AbstractLightbox.DEFAULT_TXT_IMAGE);
        defaults.put("txtOf", AbstractLightbox.DEFAULT_TXT_OF);

        DEFAULTS = Collections.unmodifiableMap(defaults);
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractLightbox.class;
    }

    @Override
    protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        AbstractLightbox lightbox = (AbstractLightbox) component;
        final HashMap<String, Object> options = new HashMap<String, Object>();
        addOptionIfSetAndOrDefault("imageBlank", lightbox.getImageBlank(), getURL(context, "/lightbox-blank.gif"), options);
        addOptionIfSetAndOrDefault("imageLoading", lightbox.getImageLoading(), getURL(context, "/lightbox-ico-loading.gif"), options);
        addOptionIfSetAndOrDefault("imageBtnNext", lightbox.getImageBtnNext(), getURL(context, "/lightbox-btn-next.gif"), options);
        addOptionIfSetAndOrDefault("imageBtnPrev", lightbox.getImageBtnPrev(), getURL(context, "/lightbox-btn-prev.gif"), options);
        addOptionIfSetAndOrDefault("imageBtnClose", lightbox.getImageBtnClose(), getURL(context, "/lightbox-btn-close.gif"), options);
        addOptionIfSetAndNotDefault("containerBorderSize", lightbox.getContainerBorderSize(), options);
        addOptionIfSetAndNotDefault("containerResizeSpeed", lightbox.getContainerResizeSpeed(), options);
        addOptionIfSetAndNotDefault("fixedNavigation", lightbox.getFixedNavigation(), options);
        addOptionIfSetAndNotDefault("keyToClose", lightbox.getKeyToClose(), options);
        addOptionIfSetAndNotDefault("keyToNext", lightbox.getKeyToNext(), options);
        addOptionIfSetAndNotDefault("keyToPrev", lightbox.getKeyToPrev(), options);
        addOptionIfSetAndNotDefault("overlayBgColor", lightbox.getOverlayBgColor(), options);
        addOptionIfSetAndNotDefault("overlayOpacity", lightbox.getOverlayOpacity(), options);
        addOptionIfSetAndNotDefault("txtImage", lightbox.getTxtImage(), options);
        addOptionIfSetAndNotDefault("txtOf", lightbox.getTxtOf(), options);
        writer.startElement(HtmlConstants.DIV_ELEM, component);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, getUtils().clientId(context, component), "id");
        getUtils().writeScript(context, component, new JSFunction("RichFaces.Lightbox", lightbox.getSelector(), options));
        writer.endElement(HtmlConstants.DIV_ELEM);
    }

    protected void addOptionIfSetAndNotDefault(String optionName, Object value, Map<String, Object> options) {
        if (value != null && !"".equals(value) && !value.equals(DEFAULTS.get(optionName))) {
            options.put(optionName, value);
        }
    }

    protected void addOptionIfSetAndOrDefault(String optionName, Object value, Object defaultValue, Map<String, Object> options) {
        if (value != null && !"".equals(value)) {
            options.put(optionName, value);
        } else {
            options.put(optionName, defaultValue);
        }
    }

    private String getURL(FacesContext context, String path) {
        return context.getApplication().getResourceHandler().createResource(path).getRequestPath();
    }
}

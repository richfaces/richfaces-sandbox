package org.richfaces.renderkit.html;

import org.ajax4jsf.javascript.JSObject;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractImageSelectTool;
import org.richfaces.component.util.InputUtils;
import org.richfaces.log.LogFactory;
import org.richfaces.log.Logger;
import org.richfaces.renderkit.HtmlConstants;
import org.richfaces.renderkit.RendererBase;

import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

@JsfRenderer(family = AbstractImageSelectTool.COMPONENT_FAMILY, type = org.richfaces.renderkit.html.ImageSelectToolRenderer.RENDERER_TYPE)
@ResourceDependencies({@ResourceDependency(library = "javax.faces", name = "jsf.js"), @ResourceDependency(name = "jquery.js", target = "head"),
        @ResourceDependency(name = "richfaces.js", target = "head"),
        @ResourceDependency(name = "richfaces-base-component.js", target = "head"),
        @ResourceDependency(name = "jquery.Jcrop.js", target = "head"),
        @ResourceDependency(name = "richfaces.imageSelectTool.js", target = "head"),
        @ResourceDependency(name = "richfaces.imageSelectTool.ecss", target = "head")})
public class ImageSelectToolRenderer extends RendererBase {
// ------------------------------ FIELDS ------------------------------

    public static final String RENDERER_TYPE = "org.richfaces.ImageSelectToolRenderer";

    private Logger logger = LogFactory.getLogger(ImageSelectToolRenderer.class);

// -------------------------- OTHER METHODS --------------------------

    @Override
    public void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        if (!(component instanceof AbstractImageSelectTool)) {
            return;
        }
        String clientId = component.getClientId(context);
        AbstractImageSelectTool imageSelectTool = (AbstractImageSelectTool) component;
        String inputId = getInputId(context, imageSelectTool);
        writer.startElement(HtmlConstants.DIV_ELEM, null);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, clientId, HtmlConstants.ID_ATTRIBUTE);
        writer.startElement(HtmlConstants.INPUT_ELEM, component);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, inputId, HtmlConstants.ID_ATTRIBUTE);
        writer.writeAttribute(HtmlConstants.TYPE_ATTR, "hidden", HtmlConstants.TYPE_ATTR);
        writer.writeAttribute(HtmlConstants.NAME_ATTRIBUTE, inputId, HtmlConstants.NAME_ATTRIBUTE);
        writer.writeAttribute(HtmlConstants.VALUE_ATTRIBUTE, getValueAsString(context, component), HtmlConstants.NAME_ATTRIBUTE);
//        writer.endElement(HtmlConstants.INPUT_ELEM);
        writer.startElement(HtmlConstants.SCRIPT_ELEM, null);
        writer.writeAttribute(HtmlConstants.TYPE_ATTR, "text/javascript", "type");
        final Map<String, Object> options = getOptions(context, imageSelectTool);
        if (imageSelectTool.getWidgetVar() != null) {
            writer.write("var " + imageSelectTool.getWidgetVar() + "=");
        }
        writer.writeText(new JSObject("RichFaces.ui.ImageSelectTool", clientId, options), null);
        writer.writeText(";", null);
        writer.endElement(HtmlConstants.SCRIPT_ELEM);
        writer.endElement(HtmlConstants.DIV_ELEM);
    }

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object o) throws ConverterException {
        AbstractImageSelectTool uiImageSelectTool = (AbstractImageSelectTool) component;
        Converter converter = getConverter(uiImageSelectTool);
        String valueString = (String) o;
        return converter.getAsObject(context, component, valueString);
    }

    public Converter getConverter(AbstractImageSelectTool component) {
        Converter converter = component.getConverter();
        if (converter == null) {
            converter = new RectangleConverter();
        }
        return converter;
    }

    public String getInputId(FacesContext context, AbstractImageSelectTool component) {
        return component.getClientId(context) + ":input";
    }

    private void addOptionIfSet(String optionName, Object value, Map<String, Object> options) {
        if (value != null && value != "") {
            options.put(optionName, value);
        }
    }

    @Override
    protected void doDecode(FacesContext context, UIComponent component) {
        super.doDecode(context, component);
        AbstractImageSelectTool uiImageSelectTool;

        if (component instanceof AbstractImageSelectTool) {
            uiImageSelectTool = (AbstractImageSelectTool) component;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("No decoding necessary since the component " + component.getId() +
                        " is not an instance or a sub class of UIInplaceInput");
            }
            return;
        }
        if (InputUtils.isDisabled(uiImageSelectTool) || InputUtils.isReadOnly(uiImageSelectTool)) {
            if (logger.isDebugEnabled()) {
                logger.debug(("No decoding necessary since the component " + component.getId() + " is disabled"));
            }
            return;
        }
        String inputId = getInputId(context, uiImageSelectTool);
        if (inputId == null) {
            throw new NullPointerException("component client id is null");
        }
        Map<String, String> request = context.getExternalContext().getRequestParameterMap();

        String newValue = request.get(inputId);
        if (newValue != null) {
            uiImageSelectTool.setSubmittedValue(newValue);
        }
    }

    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractImageSelectTool.class;
    }

    protected Map<String, Object> getOptions(FacesContext context, AbstractImageSelectTool component) throws IOException {
        Map<String, Object> options = new HashMap<String, Object>();
        addOptionIfSet("onchange", component.getOnchange(), options);
        addOptionIfSet("onselect", component.getOnselect(), options);
        Rectangle rect = (Rectangle) component.getValue();
        if (rect != null) {
            Map<String, Object> selection = new HashMap<String, Object>();
            selection.put("x", rect.x);
            selection.put("y", rect.y);
            selection.put("width", rect.width);
            selection.put("height", rect.height);
            addOptionIfSet("selection", selection, options);
        }
        addOptionIfSet("maxWidth", component.getMaxWidth(), options);
        addOptionIfSet("maxHeight", component.getMaxHeight(), options);
        addOptionIfSet("minWidth", component.getMinWidth(), options);
        addOptionIfSet("minHeight", component.getMinHeight(), options);
        addOptionIfSet("aspectRatio", component.getAspectRatio(), options);
        addOptionIfSet("backgroundColor", component.getBackgroundColor(), options);
        addOptionIfSet("backgroundOpacity", component.getBackgroundOpacity(), options);
        addOptionIfSet("trueSizeWidth", component.getTrueSizeWidth(), options);
        addOptionIfSet("trueSizeHeight", component.getTrueSizeHeight(), options);

        String forAttribute = component.getTarget();
        UIComponent forComp;
        String forClientId;
        if (forAttribute == null || forAttribute.length() == 0) {
            forComp = component;
            while ((forComp = forComp.getParent()) != null) {
                if (forComp instanceof javax.faces.component.UIGraphic) {
                    break;
                }
            }
            /**
             * We are not interested in UIViewRoot
             */
            if (forComp != null && forComp.getParent() == null) {
                forComp = null;
            }
        } else {
            forComp = getUtils().findComponentFor(context, component.getParent(), forAttribute);
        }
        if (forComp == null) {
            throw new FacesException("could not find target for croptool " + component.getId());
        }
        forClientId = forComp.getClientId(context);
        addOptionIfSet("targetId", forClientId, options);
        addOptionIfSet("inputId", getInputId(context, component), options);
        return options;
    }

    protected String getValueAsString(FacesContext context, UIComponent component) throws IOException {
        AbstractImageSelectTool uiImageSelectTool = (AbstractImageSelectTool) component;
        String valueString = (String) uiImageSelectTool.getSubmittedValue();
        if (valueString == null) {
            Object value = uiImageSelectTool.getValue();
            if (value != null) {
                Converter converter = getConverter(uiImageSelectTool);
                valueString = converter.getAsString(context, component, value);
            }
        }
        return valueString;
    }

// -------------------------- INNER CLASSES --------------------------

    private class RectangleConverter implements Converter {
// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Converter ---------------------

        public Object getAsObject(FacesContext facesContext, UIComponent component, String s) {
            StringTokenizer tokenizer = new StringTokenizer(s, ";");
            try {
                Integer x = Integer.parseInt(tokenizer.nextToken());
                Integer y = Integer.parseInt(tokenizer.nextToken());
                Integer w = Integer.parseInt(tokenizer.nextToken());
                Integer h = Integer.parseInt(tokenizer.nextToken());
                return new Rectangle(x, y, w, h);
            } catch (Exception e) {
                throw new ConverterException("conversion failure; allowed pattern X;Y;W;H", e);
            }
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object o) {
            if (!(o instanceof Rectangle)) {
                throw new ConverterException(o + " is not instance of java.awt.Rectangle");
            }
            Rectangle r = (Rectangle) o;
            return r.x + ";" + r.y + ";" + r.width + ";" + r.height;
        }
    }
}

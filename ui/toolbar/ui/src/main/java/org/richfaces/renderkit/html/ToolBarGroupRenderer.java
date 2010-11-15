package org.richfaces.renderkit.html;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractToolBar;
import org.richfaces.component.AbstractToolBarGroup;
import org.richfaces.renderkit.HtmlConstants;

@JsfRenderer(type = ToolBarGroupRenderer.RENDERER_TYPE, family = AbstractToolBar.COMPONENT_FAMILY)
public class ToolBarGroupRenderer extends ToolBarRendererBase {

    public static final String RENDERER_TYPE = "org.richfaces.ToolBarGroupRenderer";

    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractToolBarGroup.class;
    }

    public boolean getRendersChildren() {
        return true;
    }

    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
        AbstractToolBarGroup toolBarGroup = (AbstractToolBarGroup) component;
        List<UIComponent> renderedChildren = toolBarGroup.getRenderedChildren();
        if (renderedChildren.size() <= 0) {
            return;
        }

        ResponseWriter writer = facesContext.getResponseWriter();
        renderChild(facesContext, toolBarGroup, writer, renderedChildren.get(0));
        for (int i = 1; i < renderedChildren.size(); i++) {
            insertSeparatorIfNeed(facesContext, toolBarGroup, writer);
            renderChild(facesContext, toolBarGroup, writer, renderedChildren.get(i));
        }
    }

    private void renderChild(FacesContext facesContext, AbstractToolBarGroup toolBarGroup, ResponseWriter writer,
            UIComponent child) throws IOException {
        writer.startElement(HtmlConstants.TD_ELEM, toolBarGroup);
        writeClassValue(toolBarGroup, writer);
        writeStyleValue(toolBarGroup, writer);
        encodeEventsAttributes(facesContext, toolBarGroup);
        child.encodeAll(facesContext);
        writer.endElement(HtmlConstants.TD_ELEM);
    }

    private void writeStyleValue(AbstractToolBarGroup toolBarGroup, ResponseWriter writer) throws IOException {
        String style = getStringAttribute(toolBarGroup, HtmlConstants.STYLE_ATTRIBUTE);
        String contentStyle = getStringAttribute(getParentToolBar(toolBarGroup), "contentStyle");

        String value = getCompoundStyleValue(contentStyle, style);

        if (isPropertyRendered(value)) {
            writer.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, value, null);
        }
    }

    private String getCompoundStyleValue(String... styles) {
        if (styles != null) {
            StringBuilder result = new StringBuilder();
            for (int styleIndex = 0; styleIndex < styles.length; styleIndex++) {
                result.append(styles[styleIndex]);
                if (styleIndex != styles.length - 1) {
                    result.append("; ");
                }
            }
            return result.toString();
        }
        return "";
    }

    private void writeClassValue(AbstractToolBarGroup toolBarGroup, ResponseWriter writer) throws IOException {
        String styleClass = getStringAttribute(toolBarGroup, HtmlConstants.STYLE_CLASS_ATTR);
        AbstractToolBar toolBar = getParentToolBar(toolBarGroup);
        String contentClass = null;
        
        if(toolBar != null) {
            contentClass = getStringAttribute(toolBar, "contentClass");
        }
        
        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, concatClasses("rf-tb-itm", contentClass, styleClass), null);
    }

    private String getStringAttribute(UIComponent toolBarGroup, String attribute) {
        String value = (String) toolBarGroup.getAttributes().get(attribute);
        return null == value ? "" : value;
    }
    
    public AbstractToolBar getParentToolBar(UIComponent component) {
        return (component instanceof AbstractToolBarGroup) ? ((AbstractToolBarGroup) component).getToolBar() : null;
    }    

}
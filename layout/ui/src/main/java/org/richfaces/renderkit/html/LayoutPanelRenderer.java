package org.richfaces.renderkit.html;

import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractLayoutPanel;
import org.richfaces.component.LayoutPosition;
import org.richfaces.component.LayoutStructure;
import org.richfaces.renderkit.HtmlConstants;
import org.richfaces.renderkit.RendererBase;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;

@JsfRenderer(family = AbstractLayoutPanel.COMPONENT_FAMILY, type = LayoutPanelRenderer.RENDERER_TYPE)
public class LayoutPanelRenderer extends RendererBase {
// ------------------------------ FIELDS ------------------------------

    public static final String RENDERER_TYPE = "org.richfaces.LayoutPanelRenderer";

    private static final String[] LAYOUT_EXCLUSIONS = {HtmlConstants.ID_ATTRIBUTE, HtmlConstants.STYLE_ATTRIBUTE};

// -------------------------- OTHER METHODS --------------------------

    public String layoutStyle(FacesContext context, AbstractLayoutPanel panel) {
        StringBuilder style = new StringBuilder();
        LayoutPosition position = panel.getPosition();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        Object parentLayout = requestMap.get(LayoutRenderer.LAYOUT_STRUCTURE_ATTRIBUTE);
        LayoutStructure structure;
        if (null != parentLayout && parentLayout instanceof LayoutStructure) {
            structure = (LayoutStructure) parentLayout;
        } else {
            structure = new LayoutStructure(panel);
            structure.calculateWidth();
        }
        Object componentStyle = panel.getAttributes().get(HtmlConstants.STYLE_ATTRIBUTE);
        if (null != componentStyle) {
            style.append(componentStyle).append(";");
        }
        if (!LayoutPosition.top.equals(position)
                && !LayoutPosition.bottom.equals(position)) {
            if (LayoutPosition.right.equals(position)) {
                style.append("float:right;");
            } else {
                style.append("float:left;");
            }
            // calculate real width.
            float coef = 1.0f - ((float) structure.getDeep() / 100.00f);
            String width = structure.getWidth(panel, coef);
            if (null != width) {
                style.append("width:").append(width).append(";");
                coef = coef * 0.95f;
                width = structure.getWidth(panel, coef);
                style.append("*width:").append(width).append(";");
            }
        } else {
            // top and buttom style.
        }
        return style.length() > 0 ? style.toString() : null;
    }

    @Override
    protected void doEncodeBegin(ResponseWriter writer, FacesContext context,
                                 UIComponent component) throws IOException {
        writer.startElement(HtmlConstants.DIV_ELEM, component);
        getUtils().encodeCustomId(context, component);
        getUtils().encodePassThruWithExclusionsArray(context, component, LAYOUT_EXCLUSIONS, null);
        String layoutStyle = layoutStyle(context, (AbstractLayoutPanel) component);
        if (null != layoutStyle) {
            writer.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, layoutStyle, "style");
        }
    }

    @Override
    protected void doEncodeEnd(ResponseWriter writer, FacesContext context,
                               UIComponent component) throws IOException {
        writer.endElement(HtmlConstants.DIV_ELEM);
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractLayoutPanel.class;
    }
}

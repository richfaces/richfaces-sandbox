package org.richfaces.renderkit.html;

import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractLayout;
import org.richfaces.component.LayoutStructure;
import org.richfaces.renderkit.HtmlConstants;
import org.richfaces.renderkit.RendererBase;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;

@JsfRenderer(family = AbstractLayout.COMPONENT_FAMILY, type = LayoutRenderer.RENDERER_TYPE)
public class LayoutRenderer extends
        RendererBase {
// ------------------------------ FIELDS ------------------------------

    public static final String LAYOUT_STRUCTURE_ATTRIBUTE = AbstractLayout.class.getName() + ".structure";

    public static final String RENDERER_TYPE = "org.richfaces.LayoutRenderer";
    private static final String[] LAYOUT_EXCLUSIONS = {HtmlConstants.ID_ATTRIBUTE, HtmlConstants.STYLE_ATTRIBUTE};

// -------------------------- OTHER METHODS --------------------------

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    public void renderLayout(ResponseWriter writer, FacesContext context, AbstractLayout layout)
            throws IOException {
        LayoutStructure structure = new LayoutStructure(layout);
        structure.calculateWidth();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        Object oldLayout = requestMap.get(LAYOUT_STRUCTURE_ATTRIBUTE);
        requestMap.put(LAYOUT_STRUCTURE_ATTRIBUTE, structure);
        // Detect layout content;
        if (null != structure.getTop()) {
            structure.getTop().encodeAll(context);
        }
        if (structure.getColumns() > 0) {
            // Reorder panels to fill ordeg left->center->right.
            if (null != structure.getLeft()) {
                structure.getLeft().encodeAll(context);
            }
            if (null != structure.getCenter()) {
                structure.getCenter().encodeAll(context);
            }
            if (null != structure.getRight()) {
                structure.getRight().encodeAll(context);
            }
        }
        // line separator.
        writer.startElement(HtmlConstants.DIV_ELEM, layout);
        writer.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, "display: block; height: 0;line-height:0px; font-size:0px; clear: both; visibility: hidden;", null);
        writer.writeText(".", null);
        writer.endElement(HtmlConstants.DIV_ELEM);
        if (null != structure.getBottom()) {
            renderChildren(context, structure.getBottom());
        }
        requestMap.put(LAYOUT_STRUCTURE_ATTRIBUTE, oldLayout);
    }

    @Override
    protected void doEncodeBegin(ResponseWriter writer, FacesContext context,
                                 UIComponent component) throws IOException {
        writer.startElement(HtmlConstants.DIV_ELEM, component);
        getUtils().encodeCustomId(context, component);
        getUtils().encodePassThruWithExclusionsArray(context, component, LAYOUT_EXCLUSIONS, null);
        Object style = component.getAttributes().get("style");
        writer.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, null == style ? "" : (style.toString() + ";") + "zoom:1;", "style");
    }

    @Override
    protected void doEncodeChildren(ResponseWriter writer,
                                    FacesContext context, UIComponent component) throws IOException {
        renderLayout(writer, context, (AbstractLayout) component);
    }

    @Override
    protected void doEncodeEnd(ResponseWriter writer, FacesContext context,
                               UIComponent component) throws IOException {
        writer.endElement(HtmlConstants.DIV_ELEM);
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractLayout.class;
    }
}

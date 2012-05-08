package org.richfaces.renderkit.html;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlSelectOneRadio;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractRadio;
import org.richfaces.renderkit.RenderKitUtils;
import org.richfaces.renderkit.RendererBase;

@JsfRenderer(family = AbstractRadio.COMPONENT_FAMILY, type = SingleRadioRenderer.RENDERER_TYPE)
public class SingleRadioRenderer extends RendererBase {
// ------------------------------ FIELDS ------------------------------

    public static final String RENDERER_TYPE = "org.richfaces.SingleRadioRenderer";

    private String convertToString(Object obj) {
        return (obj == null ? "" : obj.toString());
    }

    @Override
    protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent _component) throws IOException {
        AbstractRadio component = (AbstractRadio) _component;
        java.lang.String clientId = component.getClientId(context);
        final UIComponent targetComponent = getUtils().findComponentFor(component, component.getFor());
        final javax.faces.model.SelectItem item = component.getSelectItem(context, targetComponent);
        boolean checked = false;
        if (targetComponent instanceof javax.faces.component.UIOutput) {
            final Object currentValue = ((javax.faces.component.UIOutput) targetComponent).getValue();
            final Object itemValue = item.getValue();
            checked = itemValue == null ? currentValue == null : itemValue.equals(currentValue);
        }

        writer.startElement("input", component);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("name", getUtils().clientId(context, targetComponent), "name");
        writer.writeAttribute("type", "radio", "type");
        writer.writeAttribute("value", item.getValue(), "value");
        if (checked) {
            writer.writeAttribute("checked", "checked", "checked");
        }
        if (isDisabled(targetComponent) || isReadonly(targetComponent)) {
            writer.writeAttribute("disabled", "disabled", "disabled");
        }

        String targetOnchange = null;
        if (targetComponent != null) {
            targetOnchange = convertToString(RenderKitUtils.getAttributeAndBehaviorsValue(context, targetComponent,
                    RenderKitUtils.attributes().generic("onchange", "onchange", "change", "valueChange").first()));
        }
        String onchange = convertToString(RenderKitUtils.getAttributeAndBehaviorsValue(context, component,
                RenderKitUtils.attributes().generic("onchange", "onchange", "change", "valueChange").first()));
        if (targetOnchange != null && !targetOnchange.trim().isEmpty()) {
            onchange = onchange == null ? targetOnchange : targetOnchange + ";" + onchange;
        }
        if (onchange != null && onchange.trim().length() > 0) {
            writer.writeAttribute("onchange", onchange, "onchange");
        }
        getUtils().encodeAttributesFromArray(context, component,
                new String[]{"accept", "accesskey", "align", "alt", "checked", "dir", "disabled", "lang", "maxlength", "onblur", "onclick", "ondblclick", "onfocus",
                        "onkeydown", "onkeypress", "onkeyup", "onmousedown", "onmousemove", "onmouseout", "onmouseover", "onmouseup", "onselect", "readonly", "size",
                        "src", "style", "tabindex", "title", "usemap", "xml:lang"});

        writer.endElement("input");
        writer.startElement("label", component);
        writer.writeAttribute("for", clientId, "for");

        writer.writeText(convertToString(item.getLabel()), null);

        writer.endElement("label");
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractRadio.class;
    }

    private boolean isDisabled(UIComponent targetComponent) {
        if (targetComponent instanceof HtmlSelectOneRadio) {
            return ((HtmlSelectOneRadio) targetComponent).isDisabled();
        } else {
            final Object disabled = targetComponent.getAttributes().get("disabled");
            return !(disabled instanceof Boolean) || (Boolean) disabled;
        }
    }

    private boolean isReadonly(UIComponent targetComponent) {
        if (targetComponent instanceof HtmlSelectOneRadio) {
            return ((HtmlSelectOneRadio) targetComponent).isReadonly();
        } else {
            final Object readonly = targetComponent.getAttributes().get("readonly");
            return !(readonly instanceof Boolean) || (Boolean) readonly;
        }
    }
}

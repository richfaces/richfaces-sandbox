/*
 * JBoss, Home of Professional Open Source
 * Copyright , Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.richfaces.renderkit.html;

import org.ajax4jsf.javascript.JSObject;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractRating;
import org.richfaces.component.util.SelectUtils;
import org.richfaces.renderkit.HtmlConstants;
import org.richfaces.renderkit.InputRendererBase;
import org.richfaces.renderkit.RenderKitUtils;

import javax.el.ELException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@JsfRenderer(family = AbstractRating.COMPONENT_FAMILY, type = RatingRenderer.RENDERER_TYPE)
@ResourceDependencies({@ResourceDependency(library = "javax.faces", name = "jsf.js"), @ResourceDependency(name = "jquery.js", target = "head"),
    @ResourceDependency(name = "richfaces.js", target = "head"),
    @ResourceDependency(name = "richfaces-base-component.js", target = "head"),
    @ResourceDependency(name = "jquery.MetaData.js", target = "head"),
    @ResourceDependency(name = "jquery.rating.js", target = "head"),
    @ResourceDependency(name = "richfaces.rating.js", target = "head"),
    @ResourceDependency(name = "jquery.rating.css", target = "head")})
public class RatingRenderer extends InputRendererBase {
// ------------------------------ FIELDS ------------------------------

    public static final String RENDERER_TYPE = "org.richfaces.RatingRenderer";

    private static final Map<String, Object> DEFAULTS;

// -------------------------- STATIC METHODS --------------------------

    static {
        Map<String, Object> defaults = new HashMap<String, Object>();
        defaults.put("required", false);
        defaults.put("readonly", false);
        defaults.put("starWidth", 16);
        defaults.put("cancelLabel", "Cancel");
        DEFAULTS = Collections.unmodifiableMap(defaults);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        if (!(component instanceof AbstractRating)) {
            return;
        }
        AbstractRating rating = (AbstractRating) component;
        writer.startElement(HtmlConstants.DIV_ELEM, null);
        String clientId = component.getClientId(context);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, clientId, HtmlConstants.ID_ATTRIBUTE);

        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE,
            concatClasses("rf-rt", component.getAttributes().get(HtmlConstants.STYLE_CLASS_ATTR)),
            HtmlConstants.STYLE_CLASS_ATTR);
        getUtils().encodeAttributesFromArray(context, component, HtmlConstants.PASS_THRU_STYLES);

        encodeRadios(writer, context, component, rating, clientId);

        writer.startElement(HtmlConstants.SCRIPT_ELEM, null);
        writer.writeAttribute(HtmlConstants.TYPE_ATTR, "text/javascript", "type");
        final Map<String, Object> options = getOptions(context, rating);
        writer.writeText(new JSObject("RichFaces.ui.Rating", clientId, options), null);
        writer.writeText(";", null);
        writer.endElement(HtmlConstants.SCRIPT_ELEM);
        writer.endElement(HtmlConstants.DIV_ELEM);
    }

    protected void addOptionIfSetAndNotDefault(String optionName, Object value, Map<String, Object> options) {
        if (value != null && !"".equals(value) && !value.equals(DEFAULTS.get(
            optionName)) && !(value instanceof Collection && ((Collection) value).size() == 0) && !(value instanceof Map && ((Map) value).size() == 0)) {
            options.put(optionName, value);
        }
    }

    private void encodeRadios(ResponseWriter writer, FacesContext context, UIComponent component, AbstractRating rating, String clientId) throws IOException {
        Object curValue = rating.getSubmittedValue();
        if (curValue == null) {
            curValue = rating.getValue();
        }

        Class type = String.class;
        if (curValue != null) {
            type = curValue.getClass();
            if (type.isArray()) {
                curValue = ((Object[]) curValue)[0];
                if (null != curValue) {
                    type = curValue.getClass();
                }
            } else if (Collection.class.isAssignableFrom(type)) {
                Iterator valueIter = ((Collection) curValue).iterator();
                if (null != valueIter && valueIter.hasNext()) {
                    curValue = valueIter.next();
                    if (null != curValue) {
                        type = curValue.getClass();
                    }
                }
            }
        }
        Iterator<SelectItem> selectItems = SelectUtils.getSelectItems(context, component);
        int itemIndex = 0;
        final String clientIdBase = clientId + UINamingContainer.getSeparatorChar(context);
        while (selectItems.hasNext()) {
            SelectItem next = selectItems.next();
            if (next.isNoSelectionOption()) {
                continue;
            }
            writer.startElement(HtmlConstants.INPUT_ELEM, component);
            writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, clientIdBase + itemIndex++, HtmlConstants.ID_ATTRIBUTE);
            writer.writeAttribute(HtmlConstants.TYPE_ATTR, "radio", HtmlConstants.TYPE_ATTR);
            writer.writeAttribute(HtmlConstants.NAME_ATTRIBUTE, clientId, HtmlConstants.NAME_ATTRIBUTE);
            writer.writeAttribute(HtmlConstants.VALUE_ATTRIBUTE, next.getValue(), HtmlConstants.VALUE_ATTRIBUTE);
            writer.writeAttribute(HtmlConstants.TITLE_ATTRIBUTE, next.getLabel(), HtmlConstants.TITLE_ATTRIBUTE);
            writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, concatClasses("rf-rt-ro", rating.getItemStyleClass()), HtmlConstants.CLASS_ATTRIBUTE);
            if (next.isDisabled()) {
                writer.writeAttribute(HtmlConstants.DISABLED_ATTR, HtmlConstants.DISABLED_ATTR, HtmlConstants.DISABLED_ATTR);
            }

            if (isChecked(context, curValue, type, next)) {
                writer.writeAttribute("checked", Boolean.TRUE, null);
            }
            writer.endElement(HtmlConstants.INPUT_ELEM);
        }
    }

    protected Map<String, Object> getOptions(FacesContext context, AbstractRating rating) throws IOException {
        /**
         * Include only attributes that are actually set.
         */
        Map<String, Object> options = new HashMap<String, Object>();
        addOptionIfSetAndNotDefault("readonly", rating.isReadonly(), options);
        addOptionIfSetAndNotDefault("required", rating.isRequired(), options);
        addOptionIfSetAndNotDefault("split", rating.getSplit(), options);
        addOptionIfSetAndNotDefault("starWidth", rating.getStarWidth(), options);
        addOptionIfSetAndNotDefault("cancelLabel", rating.getCancelLabel(), options);
        RenderKitUtils.addToScriptHash(options, "onclick", RenderKitUtils.getAttributeAndBehaviorsValue(context, rating,
            RenderKitUtils.attributes().generic("onclick", "onclick", "click", "valueChange").first()), null, RenderKitUtils.ScriptHashVariableWrapper.eventHandler);
        RenderKitUtils.addToScriptHash(options, "onblur", RenderKitUtils.getAttributeAndBehaviorsValue(context, rating,
            RenderKitUtils.attributes().generic("onblur", "onblur", "blur", "blur").first()), null, RenderKitUtils.ScriptHashVariableWrapper.eventHandler);
        RenderKitUtils.addToScriptHash(options, "onfocus", RenderKitUtils.getAttributeAndBehaviorsValue(context, rating,
            RenderKitUtils.attributes().generic("onfocus", "onfocus", "focus", "focus").first()), null, RenderKitUtils.ScriptHashVariableWrapper.eventHandler);
        return options;
    }

    private boolean isChecked(FacesContext context, Object curValue, Class type, SelectItem item) {
        Object itemValue = item.getValue();

        Object newValue;
        try {
            newValue = context.getApplication().getExpressionFactory().
                coerceToType(itemValue, type);
        } catch (ELException ele) {
            newValue = itemValue;
        } catch (IllegalArgumentException iae) {
            // If coerceToType fails, per the docs it should throw
            // an ELException, however, GF 9.0 and 9.0u1 will throw
            // an IllegalArgumentException instead (see GF issue 1527).
            newValue = itemValue;
        }

        return null != newValue && newValue.equals(curValue);
    }
}

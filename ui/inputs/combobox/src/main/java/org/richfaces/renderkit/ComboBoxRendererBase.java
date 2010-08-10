/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.renderkit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.el.ELException;
import javax.el.MethodExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.PartialViewContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.richfaces.component.AbstractComboBox;
import org.richfaces.component.MetaComponentResolver;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;

/**
 * @author Nick Belaevski
 *
 */
@ResourceDependencies({
    @ResourceDependency(library = "javax.faces", name = "jsf.js"),
    @ResourceDependency(name = "jquery.js"),
    @ResourceDependency(name = "jquery.position.js"),
    @ResourceDependency(name = "richfaces.js"),
    @ResourceDependency(name = "richfaces-event.js"),
    @ResourceDependency(name = "richfaces-base-component.js"),
    @ResourceDependency(name = "richfaces-selection.js"),
    @ResourceDependency(library = "org.richfaces", name = "AutoCompleteBase.js"),
    @ResourceDependency(library = "org.richfaces", name = "AutoComplete.js"),
    @ResourceDependency(library = "org.richfaces", name = "AutoComplete.ecss")

})
public abstract class ComboBoxRendererBase extends InputRendererBase implements MetaComponentRenderer {

    //TODO nick - handle parameter
    private Iterator<Object> getItems(FacesContext facesContext, AbstractComboBox component) {
        Object itemsObject = null;

        MethodExpression autocompleteMethod = component.getAutocompleteMethod();
        if (autocompleteMethod != null) {
            try {
                //String value = getInputValue(facesContext, component);
                Map<String, String> requestParameters = facesContext.getExternalContext().getRequestParameterMap();
                String value = requestParameters.get(component.getClientId(facesContext) + "Value");
                itemsObject = autocompleteMethod.invoke(facesContext.getELContext(),
                    new Object[] {facesContext, component, value});
            } catch (ELException e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        } else {
            itemsObject = component.getAutocompleteList();
        }

        Iterator<Object> result;

        //TODO nick - primitive arrays support
        if (itemsObject instanceof Object[]) {
            result = Iterators.forArray((Object[]) itemsObject);
        } else if (itemsObject != null) {
            result = ((Iterable<Object>) itemsObject).iterator();
        } else {
            result = Iterators.emptyIterator();
        }

        return result;
    }

    private Object saveVar(FacesContext context, String var) {
        if (var != null) {
            Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
            return requestMap.get(var);
        }

        return null;
    }

    private void setVar(FacesContext context, String var, Object varObject) {
        if (var != null) {
            Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
            requestMap.put(var, varObject);
        }
    }

    protected void encodeItemContainerBegin(FacesContext facesContext, AbstractComboBox comboBox, Object item) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.LI_ELEMENT, comboBox);
        writer.writeAttribute(HTML.CLASS_ATTRIBUTE, "cb_option cb_font rf-ac-i", null);
    }

    protected void encodeItemContainerEnd(FacesContext facesContext, AbstractComboBox comboBox, Object item) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.endElement(HTML.LI_ELEMENT);
    }

    protected void encodeItem(FacesContext facesContext, AbstractComboBox comboBox, Object item) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.LI_ELEMENT, comboBox);
        writer.writeAttribute(HTML.CLASS_ATTRIBUTE, "cb_option cb_font rf-ac-i", null);

        if (comboBox.getChildCount() > 0) {
            for (UIComponent child: comboBox.getChildren()) {
                child.encodeAll(facesContext);
            }
        } else {
            if (item != null) {
                //TODO nick - use converter
                writer.writeText(item, null);
            }
        }

        writer.endElement(HTML.LI_ELEMENT);
    }

    protected void encodeFakeItem(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter responseWriter = context.getResponseWriter();
        responseWriter.startElement(HTML.LI_ELEMENT, component);
        responseWriter.writeAttribute(HTML.STYLE_ATTRIBUTE, "display:none", null);
        responseWriter.endElement(HTML.LI_ELEMENT);
    }

    protected void encodeItems(FacesContext facesContext, UIComponent component, List<Object> fetchValues) throws IOException {
        AbstractComboBox comboBox = (AbstractComboBox) component;

        encodeItemsContainerBegin(facesContext, component);

        boolean hasEncodedElements = false;

        Object savedVar = saveVar(facesContext, comboBox.getVar());

        for (Iterator<Object> items = getItems(facesContext, comboBox); items.hasNext(); ) {
            hasEncodedElements = true;

            Object nextItem = items.next();
            setVar(facesContext, comboBox.getVar(), nextItem);

            encodeItem(facesContext, comboBox, nextItem);
            fetchValues.add(comboBox.getFetchValue());
        }

        setVar(facesContext, comboBox.getVar(), savedVar);

        if (!hasEncodedElements) {
            //TODO nick - ?
        }

        encodeItemsContainerEnd(facesContext, component);
    }

    protected String getContainerElementId(FacesContext facesContext, UIComponent component) {
        return component.getClientId(facesContext) + "Items";
    }

    protected void encodeItemsContainerBegin(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        responseWriter.startElement(HTML.UL_ELEMENT, component);
        responseWriter.writeAttribute(HTML.ID_ATTRIBUTE, getContainerElementId(facesContext, component), null);
        responseWriter.writeAttribute(HTML.CLASS_ATTRIBUTE, "cb_list_ul", null);
    }

    protected void encodeItemsContainerEnd(FacesContext facesContext, UIComponent component) throws IOException {
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        responseWriter.endElement(HTML.UL_ELEMENT);
    }

    protected void encodeItemsContainer(FacesContext facesContext, UIComponent component) throws IOException {
        encodeItemsContainerBegin(facesContext, component);
        encodeFakeItem(facesContext, component);
        encodeItemsContainerEnd(facesContext, component);
    }

    @Override
    protected void doDecode(FacesContext context, UIComponent component) {
        super.doDecode(context, component);

        Map<String, String> requestParameters = context.getExternalContext().getRequestParameterMap();
        if (requestParameters.get(component.getClientId(context) + ".ajax") != null) {
            PartialViewContext pvc = context.getPartialViewContext();
            pvc.getRenderIds().add(component.getClientId(context) + MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR + AbstractComboBox.ITEMS_META_COMPONENT_ID);
        }
    }

    public void encodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId)
        throws IOException {
        if (AbstractComboBox.ITEMS_META_COMPONENT_ID.equals(metaComponentId)) {

            List<Object> fetchValues = new ArrayList<Object>();
            
            PartialResponseWriter partialWriter = context.getPartialViewContext().getPartialResponseWriter();
            partialWriter.startUpdate(getContainerElementId(context, component));
            encodeItems(context, component, fetchValues);
            partialWriter.endUpdate();
            
            if (!fetchValues.isEmpty() && Iterators.find(fetchValues.iterator(), Predicates.notNull()) != null) {
                Map<String, Object> dataMap = AjaxContext.getCurrentInstance(context).getResponseComponentDataMap();
                dataMap.put(component.getClientId(context), fetchValues);
            }
        } else {
            throw new IllegalArgumentException(metaComponentId);
        }
    }
}

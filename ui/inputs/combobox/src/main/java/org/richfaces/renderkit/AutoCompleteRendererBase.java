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
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
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
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.ResultDataModel;
import javax.faces.model.ResultSetDataModel;
import javax.servlet.jsp.jstl.sql.Result;

import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.javascript.ScriptUtils;
import org.ajax4jsf.renderkit.RendererUtils;
import org.ajax4jsf.util.InputUtils;
import org.richfaces.component.AbstractAutoComplete;
import org.richfaces.component.AutocompleteLayout;
import org.richfaces.component.MetaComponentResolver;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;

/**
 * @author Nick Belaevski
 * 
 */
@ResourceDependencies( {
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
public abstract class AutoCompleteRendererBase extends InputRendererBase implements
		MetaComponentRenderer {

	public String getScriptOptions(UIComponent component){
		Map<String, Object> attributes = component.getAttributes();
		Map<String, Object> options = new HashMap<String, Object>();
        RendererUtils utils = getUtils();
        utils.addToScriptHash(options, "buttonId", component.getClientId() + "Button");
        utils.addToScriptHash(options, "selectedItemClass", "cb_select");
        utils.addToScriptHash(options, "minChars", attributes.get("minChars"), "1");
        utils.addToScriptHash(options, "mode", attributes.get("mode"), "ajax");
        utils.addToScriptHash(options, "filterFunction", attributes.get("filterFunction"));
        utils.addToScriptHash(options, "autoFill", attributes.get("autoFill"), "false");
        utils.addToScriptHash(options, "disabled", attributes.get("disabled"), "false");
        utils.addToScriptHash(options, "selectFirst", attributes.get("selectFirst"), "false");
        utils.addToScriptHash(options, "onbegin", attributes.get("onbegin"));
        utils.addToScriptHash(options, "oncomplete", attributes.get("oncomplete"));
        utils.addToScriptHash(options, "onerror", attributes.get("onerror"));
        utils.addToScriptHash(options, "onbeforedomupdate", attributes.get("onbeforedomupdate"));
        utils.addToScriptHash(options, "onchange", attributes.get("onchange"));
        StringBuilder builder = new StringBuilder();
        builder.append(ScriptUtils.toScript(options));
        return builder.toString();
	}
	// TODO nick - handle parameter
	@SuppressWarnings("unchecked")
	private DataModel<Object> getItems(FacesContext facesContext,
			AbstractAutoComplete component) {
		Object itemsObject = null;

		MethodExpression autocompleteMethod = component.getAutocompleteMethod();
		if (autocompleteMethod != null) {
			Map<String, String> requestParameters = facesContext
					.getExternalContext().getRequestParameterMap();
			String value = requestParameters.get(component
					.getClientId(facesContext)
					+ "Value");
			try {
				// String value = getInputValue(facesContext, component);

				itemsObject = autocompleteMethod.invoke(facesContext
						.getELContext(), new Object[] { facesContext,
						component, value });
			} catch (ELException e) {
				try {
					autocompleteMethod = facesContext.getApplication()
							.getExpressionFactory().createMethodExpression(
									facesContext.getELContext(),
									autocompleteMethod.getExpressionString(),
									Void.class, new Class[] { String.class });
					itemsObject = autocompleteMethod.invoke(facesContext
							.getELContext(), new Object[] { value });
				} catch (ELException ee) {
					ee.printStackTrace();
				}

			}
		} else {
			itemsObject = component.getAutocompleteList();
		}

		DataModel result;

		if (itemsObject instanceof Object[]) {
			result = new ArrayDataModel((Object[]) itemsObject);
		} else if (itemsObject instanceof List) {
			result = new ListDataModel((List<Object>) itemsObject);
		} else if (itemsObject instanceof Result) {
			result = new ResultDataModel((Result) itemsObject);
		} else if (itemsObject instanceof ResultSet) {
			result = new ResultSetDataModel((ResultSet) itemsObject);
		} else if (itemsObject != null) {
			List<Object> temp = new ArrayList<Object>();
			Iterator<Object> iterator = ((Iterable<Object>) itemsObject)
					.iterator();
			while (iterator.hasNext()) {
				temp.add(iterator.next());
			}
			result = new ListDataModel(temp);
		} else {
			result = new ListDataModel(null);
		}

		return result;
	}

	private Object saveVar(FacesContext context, String var) {
		if (var != null) {
			Map<String, Object> requestMap = context.getExternalContext()
					.getRequestMap();
			return requestMap.get(var);
		}

		return null;
	}

	private void setVar(FacesContext context, String var, Object varObject) {
		if (var != null) {
			Map<String, Object> requestMap = context.getExternalContext()
					.getRequestMap();
			requestMap.put(var, varObject);
		}
	}

	protected void encodeItems(FacesContext facesContext,
			UIComponent component, List<Object> fetchValues) throws IOException {
		AbstractAutoComplete comboBox = (AbstractAutoComplete) component;
		AutoCompleteEncodeStrategy strategy = getStrategy(component);
		strategy.encodeItemsContainerBegin(facesContext, component);

		boolean hasEncodedElements = false;

		Object savedVar = saveVar(facesContext, comboBox.getVar());
		DataModel<Object> model = getItems(facesContext, comboBox);
		for (Iterator<Object> items = model.iterator(); items.hasNext();) {
			hasEncodedElements = true;

			Object nextItem = items.next();
			setVar(facesContext, comboBox.getVar(), nextItem);

			strategy.encodeItem(facesContext, comboBox, nextItem);
			if (comboBox.getFetchValue() != null) {
				fetchValues.add(comboBox.getFetchValue());
			} else {
				//TODO use converter
				fetchValues.add(nextItem);
			}
		}

		setVar(facesContext, comboBox.getVar(), savedVar);

		if (!hasEncodedElements) {
			strategy.encodeFakeItem(facesContext, component);
		}

		strategy.encodeItemsContainerEnd(facesContext, component);
	}

	protected void encodeItemsContainer(FacesContext facesContext,
			UIComponent component) throws IOException {
		AutoCompleteEncodeStrategy strategy = getStrategy(component);
		strategy.encodeItemsContainerBegin(facesContext, component);
		strategy.encodeFakeItem(facesContext, component);
		strategy.encodeItemsContainerEnd(facesContext, component);
	}

	private AutoCompleteEncodeStrategy getStrategy(UIComponent component) {
		AbstractAutoComplete comboBox = (AbstractAutoComplete) component;
		if (comboBox.getLayout() != null) {
			if (comboBox.getLayout().equals(AutocompleteLayout.div)) {
				return new AutocompleteDivLayoutStrategy();
			}
			if (comboBox.getLayout().equals(AutocompleteLayout.grid)) {
				return new AutocompleteGridLayoutStrategy();
			}
			if (comboBox.getLayout().equals(AutocompleteLayout.list)) {
				return new AutocompleteListLayoutStrategy();
			}
			if (comboBox.getLayout().equals(AutocompleteLayout.table)) {
				return new AutocompleteTableLayoutStrategy();
			}
		}
		return new AutocompleteDivLayoutStrategy();
	}

	@Override
	protected void doDecode(FacesContext context, UIComponent component) {
		if (InputUtils.isDisabled(component)) {
			return;
		}
		super.doDecode(context, component);

		Map<String, String> requestParameters = context.getExternalContext()
				.getRequestParameterMap();
		if (requestParameters.get(component.getClientId(context) + ".ajax") != null) {
			PartialViewContext pvc = context.getPartialViewContext();
			pvc
					.getRenderIds()
					.add(
							component.getClientId(context)
									+ MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR
									+ AbstractAutoComplete.ITEMS_META_COMPONENT_ID);
		}
	}

	public void encodeMetaComponent(FacesContext context,
			UIComponent component, String metaComponentId) throws IOException {
		if (AbstractAutoComplete.ITEMS_META_COMPONENT_ID.equals(metaComponentId)) {

			List<Object> fetchValues = new ArrayList<Object>();

			PartialResponseWriter partialWriter = context
					.getPartialViewContext().getPartialResponseWriter();
			partialWriter.startUpdate(getStrategy(component)
					.getContainerElementId(context, component));
			encodeItems(context, component, fetchValues);
			partialWriter.endUpdate();

			if (!fetchValues.isEmpty()
					&& Iterators.find(fetchValues.iterator(), Predicates
							.notNull()) != null) {
				Map<String, Object> dataMap = AjaxContext.getCurrentInstance(
						context).getResponseComponentDataMap();
				dataMap.put(component.getClientId(context), fetchValues);
			}
		} else {
			throw new IllegalArgumentException(metaComponentId);
		}
	}
}

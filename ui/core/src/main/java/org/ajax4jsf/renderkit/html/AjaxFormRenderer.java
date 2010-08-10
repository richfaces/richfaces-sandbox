/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package org.ajax4jsf.renderkit.html;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.component.UIAjaxForm;
import org.ajax4jsf.event.AjaxEvent;
import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.javascript.ScriptUtils;
import org.ajax4jsf.renderkit.AjaxComponentRendererBase;
import org.ajax4jsf.renderkit.AjaxRendererUtils;
import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.ajax4jsf.resource.InternetResource;

/**
 * @author shura
 * 
 */
public class AjaxFormRenderer extends AjaxComponentRendererBase {

	public static final String FORM_SUBMIT_FUNCTION_NAME = "_JSFFormSubmit";

	public static final String FORM_CLEAR_FUNCTION_NAME = "_clearJSFFormParameters";

	private static final String FORM_HAS_COMMAND_LINK_ATTR = "com.sun.faces.FORM_HAS_COMMAND_LINK_ATTR";

	private static final String NO_COMMAND_LINK_FOUND_VALUE = "com.sun.faces.NO_COMMAND_LINK_FOUND";

	public static final String CONTENT_TYPE_IS_XHTML = "com.sun.faces"
			+ "ContentTypeIsXHTML";

	public static final String HIDDEN_FIELD_SUFFIX = NamingContainer.SEPARATOR_CHAR
			+ UIViewRoot.UNIQUE_ID_PREFIX + "cl";

	private static final String HIDDEN_COMMAND_INPUTS_SET_ATTR = UIForm.class
			.getName()
			+ ".org.apache.myfaces.HIDDEN_COMMAND_INPUTS_SET";

	private static final String MYFACES_HIDDEN_FIELD_SUFFIX = NamingContainer.SEPARATOR_CHAR
			+ "_link_hidden_";

	private InternetResource[] _scripts = { getResource("/org/ajax4jsf/javascript/scripts/form.js") };

	private static final String[] exclusions = { "onsubmit" };

	public static final String AJAX_FORM_FUNCTION_NAME = "A4J.AJAX.SubmitForm";

	/*
	 * (non-Javadoc)
	 * 	
	 * @see org.ajax4jsf.renderkit.RendererBase#doDecode(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent)
	 */
	protected void doDecode(FacesContext context, UIComponent component) {
		UIForm form = (UIForm) component;
		boolean submitted = context.getExternalContext()
				.getRequestParameterMap().containsKey(
						component.getClientId(context));
		form.setSubmitted(submitted);
		if (component instanceof UIAjaxForm) {
		    UIAjaxForm ajaxForm = (UIAjaxForm) component;
			if (submitted && ajaxForm.isAjaxSubmit()) {
				component.queueEvent(new AjaxEvent(component));
			}		    
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ajax4jsf.renderkit.HeaderResourcesRendererBase#getScripts()
	 */
	protected InternetResource[] getAdditionalScripts() {
		// TODO Auto-generated method stub
		return _scripts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ajax4jsf.renderkit.RendererBase#doEncodeBegin(javax.faces.context.ResponseWriter,
	 *      javax.faces.context.FacesContext, javax.faces.component.UIComponent)
	 */
	protected void doEncodeBegin(ResponseWriter writer, FacesContext context,
			UIComponent component) throws IOException {
		String clientId = component.getClientId(context);
		writer.startElement(HTML.FORM_ELEMENT, component);
		writer.writeAttribute(HTML.id_ATTRIBUTE, clientId, null);
		writer.writeAttribute(HTML.NAME_ATTRIBUTE, clientId, null);
		writer.writeAttribute(HTML.METHOD_ATTRIBUTE, "post", null);
		getUtils().encodeAttribute(context, component, "style");
		getUtils().encodeAttribute(context, component, "class");
		getUtils().encodePassThru(context, component);
		context.getExternalContext().getRequestMap().put(
				FORM_HAS_COMMAND_LINK_ATTR, clientId);
		if (component instanceof UIAjaxForm) {
			UIAjaxForm form = (UIAjaxForm) component;
			if (form.isAjaxSubmit()) {
				StringBuffer onSubmit = new StringBuffer("javascript:");
				JSFunction ajaxFunction = AjaxRendererUtils.buildAjaxFunction(
						component, context,
						AJAX_FORM_FUNCTION_NAME);
				ajaxFunction.addParameter(AjaxRendererUtils.buildEventOptions(
						context, component, false));
				ajaxFunction.appendScript(onSubmit);
				writer.writeURIAttribute("action", onSubmit, "action");
			} else {
				encodeSubmitAction(writer, context);
			}
		} else {
			encodeSubmitAction(writer, context);
		}
	}

	/**
	 * @param writer
	 * @param context
	 * @throws IOException
	 */
	private void encodeSubmitAction(ResponseWriter writer, FacesContext context)
			throws IOException {
		String actionURL = getUtils().getActionUrl(context);
		String encodedActionURL = context.getExternalContext().encodeActionURL(
				actionURL);
		writer.writeURIAttribute("action", encodedActionURL, "action");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ajax4jsf.renderkit.RendererBase#doEncodeEnd(javax.faces.context.ResponseWriter,
	 *      javax.faces.context.FacesContext, javax.faces.component.UIComponent)
	 */
	protected void doEncodeEnd(ResponseWriter writer, FacesContext context,
			UIComponent component) throws IOException {
		// Encode form submitting flag hidden field.
		String clientId = component.getClientId(context);
		renderHiddenInputField(writer, clientId, clientId);
		renderHiddenInputField(writer, "autoScroll", null);
		// MyFaces compability - render hidden field and Script.
		// render hidden command inputs
		Set hiddenFields = new HashSet();
		hiddenFields.add(clientId + HIDDEN_FIELD_SUFFIX);
		hiddenFields.add(clientId + MYFACES_HIDDEN_FIELD_SUFFIX);
		Set set = (Set) component.getAttributes().get(
				HIDDEN_COMMAND_INPUTS_SET_ATTR);
		if (set != null) {
			hiddenFields.addAll(set);
		}
		set = (Set) context.getExternalContext().getRequestMap().get(
				getHiddenCommandInputsSetName(context, component));
		if (set != null) {
			hiddenFields.addAll(set);
		}
		renderHiddenCommandFormParams(writer, hiddenFields);
		String target;
		if (component instanceof HtmlForm) {
			target = ((HtmlForm) component).getTarget();
		} else {
			target = (String) component.getAttributes().get(
					HTML.target_ATTRIBUTE);
		}
		renderClearHiddenCommandFormParamsFunction(writer, clientId, hiddenFields, target);
		// Script
		if (component instanceof UIAjaxForm) {
			UIAjaxForm form = (UIAjaxForm) component;
			if (form.isAjaxSubmit()) {
				renderAjaxFormSetupScript(writer, clientId);
			}
		}
		// writeFormSubmitScript(context, writer);
		getUtils().encodeEndForm(context, writer);
		context.getExternalContext().getRequestMap().put(
				FORM_HAS_COMMAND_LINK_ATTR, NO_COMMAND_LINK_FOUND_VALUE);
	}

	private static String getHiddenCommandInputsSetName(
			FacesContext facesContext, UIComponent form) {
		StringBuffer buf = new StringBuffer();
		buf.append(HIDDEN_COMMAND_INPUTS_SET_ATTR);
		buf.append("_");
		buf.append(form.getClientId(facesContext));
		return buf.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ajax4jsf.renderkit.RendererBase#getComponentClass()
	 */
	protected Class getComponentClass() {
		// TODO Auto-generated method stub
		return UIForm.class;
	}

	// For MyFaces compability - create hidden firlds and script. Code got from
	// project to simulate same functionality.
	public void renderHiddenCommandFormParams(ResponseWriter writer,
			Set formParams) throws IOException {
		for (Iterator it = formParams.iterator(); it.hasNext();) {
			Object name = it.next();
			renderHiddenInputField(writer, name, "");
		}
	}

	public void renderHiddenInputField(ResponseWriter writer, Object name,
			Object value) throws IOException {
		writer.startElement(HTML.INPUT_ELEM, null);
		writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_HIDDEN, null);
		writer.writeAttribute(HTML.autocomplete_ATTRIBUTE, "off", null);
		writer.writeAttribute(HTML.NAME_ATTRIBUTE, name, null);
		writer.writeAttribute(HTML.value_ATTRIBUTE, value != null?value:"", null);
		writer.endElement(HTML.INPUT_ELEM);
	}
	
	
	public void renderAjaxFormSetupScript(ResponseWriter writer, String formName) throws IOException {
		writer.startElement(HTML.SCRIPT_ELEM, null);
		writer.writeAttribute(HTML.TYPE_ATTR, "text/javascript", null);
		writer.writeText("A4J.setupForm('"+formName+"');", null);
		writer.endElement(HTML.SCRIPT_ELEM);
	}

	/**
	 * Render the javascript function that is called on a click on a commandLink
	 * to clear the hidden inputs. This is necessary because on a browser back,
	 * each hidden input still has it's old value (browser cache!) and therefore
	 * a new submit would cause the according action once more!
	 * 
	 * @param writer
	 * @param formName
	 * @param dummyFormParams
	 * @param formTarget
	 * @throws IOException
	 */
	public void renderClearHiddenCommandFormParamsFunction(
			ResponseWriter writer, String formName, Set dummyFormParams,
			String formTarget) throws IOException {
		// render the clear hidden inputs javascript function
		String functionName = getClearHiddenCommandFormParamsFunctionName(formName);
		writer.startElement(HTML.SCRIPT_ELEM, null);
		writer.writeAttribute(HTML.TYPE_ATTR, "text/javascript", null);

		// Using writeComment instead of write with <!-- tag
		StringBuffer script = new StringBuffer();
		script.append("function ");
		script.append(functionName);
		script.append("() {\n");
		script.append(FORM_CLEAR_FUNCTION_NAME);
		script.append("('");
		script.append(formName);
		script.append("','");
		if (formTarget != null && formTarget.length() > 0) {
			script.append(formTarget);
		}
		script.append("'");
		if (dummyFormParams != null) {
			script.append(",[");
			for (Iterator it = dummyFormParams.iterator(); it.hasNext();) {
				script.append('\'');
				script.append((String) it.next());
				script.append('\'');
				if (it.hasNext()) {
					script.append(',');
				}
			}
			script.append("]");
		}
		script.append(");");
		script.append("\n}");
		script.append("\n");
		script.append("function ").append("clearFormHiddenParams_").append(
				ScriptUtils.getValidJavascriptName(formName));
		script.append("(){").append(functionName).append("();}\n");
		// MyFaces 1.1.5 clear form function name
		
		//equivalent for formName.replace("-", "$_")
		StringBuffer formNameReplaceBuffer = new StringBuffer(formName.length());
		int idx = -1;
		int idxA = 0;
		while ((idx = formName.indexOf('-', idxA)) != -1) {
			formNameReplaceBuffer.append(formName.substring(idxA, idx));
			formNameReplaceBuffer.append("$_");
			idxA = idx + 1;
		}
		formNameReplaceBuffer.append(formName.substring(idxA));
		
		script.append("function ").append("clearFormHiddenParams_").append(formNameReplaceBuffer.toString().
			replace(':', '_'));
		script.append("(){").append(functionName).append("();}\n");
		// Just to be sure we call this clear method on each load.
		// Otherwise in the case, that someone submits a form by pressing Enter
		// within a text input, the hidden inputs won't be cleared!
		script.append(functionName);
		script.append("();");

		writer.writeText(script.toString(), null);
		writer.endElement(HTML.SCRIPT_ELEM);
	}

	/**
	 * Prefixes the given String with "clear_" and removes special characters
	 * 
	 * @param formName
	 * @return String
	 */
	public String getClearHiddenCommandFormParamsFunctionName(String formName) {
		return "clear_" + ScriptUtils.getValidJavascriptName(formName);
	}

}

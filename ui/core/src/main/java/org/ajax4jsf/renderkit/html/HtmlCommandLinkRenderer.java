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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIParameter;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import org.ajax4jsf.Messages;
import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.renderkit.RendererBase;
import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.ajax4jsf.resource.InternetResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * "Wrapped" standard {@link javax.faces.component.html.HtmlCommandLink} renderer,
 * to avoid common problem - If no commandLink's exist in initial rendered page, {@link javax.faces.component.html.HtmlForm} renderer
 * don't encode nessesary hidden fields for link and it's param. Our form-link renderer can avoid this problem.
 * @author shura
 *
 */
public class HtmlCommandLinkRenderer extends RendererBase {

	private static final Log _log = LogFactory.getLog(HtmlCommandLinkRenderer.class);
	
	private static final String[] LINK_EXCLUSIONS = {"onclick","target","href"};
	
	private InternetResource[] scripts = null;
	private static final String FORM_SCRIPT = "/org/ajax4jsf/javascript/scripts/form.js";
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ajax4jsf.renderkit.RendererBase#doDecode(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent)
	 */
	protected void doDecode(FacesContext context, UIComponent component) {
		UIForm form = getUtils().getNestingForm(context,component);
		if(null != form){
			String hiddenFieldId = getHiddenFieldId(context, form, component);
			Object hiddenFieldValue;
			if(null != (hiddenFieldValue=context.getExternalContext().getRequestParameterMap().get(hiddenFieldId))){
				if(component.getClientId(context).equals(hiddenFieldValue)){
					// Link submitted !
					if(_log.isDebugEnabled()){
						_log.debug(Messages.getMessage(Messages.COMMAND_LINK_SUBMIT_INFO, component.getClientId(context)));
					}
			        ActionEvent actionEvent = new ActionEvent(component);
			        component.queueEvent(actionEvent);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.ajax4jsf.renderkit.RendererBase#doEncodeBegin(javax.faces.context.ResponseWriter, javax.faces.context.FacesContext, javax.faces.component.UIComponent)
	 */
	protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
		if(isDisabled(context,component)){
			doEncodePassiveBegin(writer, context, component);
		} else {
			doEncodeActiveBegin(writer, context, component);
		}
	}

	private String getHiddenFieldId(FacesContext context, UIForm form, UIComponent component) {
		return form.getClientId(context)+AjaxFormRenderer.HIDDEN_FIELD_SUFFIX;
	}
	
	protected void doEncodeActiveBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException{
		writer.startElement(HTML.a_ELEMENT,component);
		getUtils().encodeId(context,component);
		getUtils().encodeAttributesFromArray(context,component,HTML.PASS_THRU_STYLES);
		getUtils().encodePassThruWithExclusionsArray(context,component,LINK_EXCLUSIONS);
		writer.writeAttribute("href","#",null);
		UIForm form = getUtils().getNestingForm(context,component);
		String clientId = component.getClientId(context);
		if(null == form){
			if(_log.isWarnEnabled()){
				_log.warn(Messages.getMessage(Messages.COMMAND_LINK_NOT_IN_FORM_WARNING, clientId));
			}
			return;
		}
		// Encode onclick attribute.
		Object click = component.getAttributes().get("onclick");
		StringBuffer onclick = new StringBuffer(256);
		if(null != click){
			onclick.append(click).append(';');
		}
		JSFunction submit = new JSFunction(AjaxFormRenderer.FORM_SUBMIT_FUNCTION_NAME);
		submit.addParameter(clientId);
		submit.addParameter(form.getClientId(context));
		submit.addParameter(component.getAttributes().get("target"));
		Map parameters = new HashMap();
		for (Iterator iter = component.getChildren().iterator(); iter.hasNext();) {
			Object child =  iter.next();
			if (child instanceof UIParameter) {
				UIParameter param = (UIParameter) child;
				String name = ((UIParameter) child).getName();
				Object value = ((UIParameter) child).getValue();
				
				if (name == null) {
					throw new IllegalArgumentException(Messages.getMessage(
							Messages.UNNAMED_PARAMETER_ERROR, component.getClientId(context)));
				} 
				
				if (value == null) {
					value = "";
				}
				
				parameters.put(name,value);
			}
		}
		
		parameters.put(getHiddenFieldId(context, form, component), component.getClientId(context));
		
		submit.addParameter(parameters);
		onclick.append("return ");
		submit.appendScript(onclick);
		writer.writeAttribute(HTML.onclick_ATTRIBUTE,onclick,"onclick");
		// 
		encodeValue(writer,context,component);
	}

	protected void doEncodePassiveBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException{
		writer.startElement(HTML.SPAN_ELEM,component);
		getUtils().encodeId(context,component);
		getUtils().encodeAttributesFromArray(context,component,HTML.PASS_THRU_STYLES);
		getUtils().encodePassThru(context,component);
		encodeValue(writer,context,component);
	}

	/* (non-Javadoc)
	 * @see javax.faces.render.Renderer#encodeChildren(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
	 */
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
		// TODO Auto-generated method stub
		renderChildren(context,component);
	}

	/* (non-Javadoc)
	 * @see org.ajax4jsf.renderkit.RendererBase#doEncodeEnd(javax.faces.context.ResponseWriter, javax.faces.context.FacesContext, javax.faces.component.UIComponent)
	 */
	protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
		if(isDisabled(context,component)){
			doEncodePassiveEnd(writer, context, component);
		} else {
			doEncodeActiveEnd(writer, context, component);
		}
	}

	protected void doEncodeActiveEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException{
		writer.endElement(HTML.a_ELEMENT);
	}

	protected void doEncodePassiveEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException{
		writer.endElement(HTML.SPAN_ELEM);
	}

/**
 * Check for disabled component
 * @param context
 * @param command
 * @return true if link is disabled.
 */
private boolean isDisabled(FacesContext context, UIComponent command){
    if (command.getAttributes().get("disabled") != null) {
        if ((command.getAttributes().get("disabled")).equals(Boolean.TRUE)) {
            return true;
        }
    }
    return false;
}
	/* (non-Javadoc)
	 * @see org.ajax4jsf.renderkit.RendererBase#getComponentClass()
	 */
	protected Class getComponentClass() {
		// TODO Auto-generated method stub
		return UICommand.class;
	}

    /* (non-Javadoc)
     * @see javax.faces.render.Renderer#getRendersChildren()
     */
    public boolean getRendersChildren() {
        return true;
    }

    protected void encodeValue(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException{
        String valueString = null;
        if (component instanceof UICommand) {
            Object value = ((UICommand) component).getValue();
            if (value != null) {
                valueString = value.toString();
            }
        } else if (component instanceof ValueHolder) {
            Object value = ((ValueHolder) component).getValue();
            if (value != null) {
                valueString = getUtils().formatValue(context,component,value);
            }
        }
        	
            if (null != valueString && valueString.length()>0) {
				writer.writeText(valueString,null);
			}
        }
    
    /* (non-Javadoc)
     * @see org.ajax4jsf.renderkit.HeaderResourcesRendererBase#getScripts()
     */
    protected InternetResource[] getScripts() {
    	synchronized (this) {
        	if (scripts == null) {			
    			scripts = new InternetResource[1];				
    			scripts[0] = getResource(FORM_SCRIPT);			
    		}
		}

    	return scripts;
	}
}

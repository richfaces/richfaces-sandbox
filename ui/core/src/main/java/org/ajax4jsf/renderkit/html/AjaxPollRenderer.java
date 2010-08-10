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
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.component.UIPoll;
import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.javascript.JSFunctionDefinition;
import org.ajax4jsf.renderkit.AjaxCommandRendererBase;
import org.ajax4jsf.renderkit.AjaxRendererUtils;
import org.ajax4jsf.renderkit.RendererUtils.HTML;

/**
 * @author shura
 *
 */
public class AjaxPollRenderer extends AjaxCommandRendererBase {
	
	

	private static final String AJAX_POLL_FUNCTION = "A4J.AJAX.Poll";

	/* (non-Javadoc)
	 * @see org.ajax4jsf.renderkit.RendererBase#doEncodeEnd(javax.faces.context.ResponseWriter, javax.faces.context.FacesContext, javax.faces.component.UIComponent)
	 */
	protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
		UIPoll poll = (UIPoll) component;
		writer.startElement(HTML.SPAN_ELEM, component);
		writer.writeAttribute(HTML.style_ATTRIBUTE, "display:none;", null);
		getUtils().encodeId(context, component);
			// polling script.
				writer.startElement(HTML.SCRIPT_ELEM, component);
				writer.writeAttribute(HTML.TYPE_ATTR, "text/javascript", null);
				StringBuffer script = new StringBuffer("\n");
				if(poll.isEnabled()){
				JSFunction function = AjaxRendererUtils.buildAjaxFunction(component, context, AJAX_POLL_FUNCTION);
				Map options = AjaxRendererUtils.buildEventOptions(context, component, true);
				Integer interval = new Integer(poll.getInterval());
				options.put("pollinterval", interval);
				options.put("pollId", component.getClientId(context));
				Object onsubmit = component.getAttributes().get("onsubmit");
				if (null != onsubmit) {
					JSFunctionDefinition onsubmitFunction = new JSFunctionDefinition();
					onsubmitFunction.addToBody(onsubmit);
					options.put("onsubmit", onsubmitFunction);
				}

//				options.put("timeout", interval);
				function.addParameter(options);
				function.appendScript(script);
				} else {
					script.append("A4J.AJAX.StopPoll('").append(component.getClientId(context)).append("')");
				}
				script.append(";\n");
				writer.writeText(script.toString(),null);
				writer.endElement(HTML.SCRIPT_ELEM);				
		writer.endElement(HTML.SPAN_ELEM);
	}

	/* (non-Javadoc)
	 * @see org.ajax4jsf.renderkit.RendererBase#getComponentClass()
	 */
	protected Class getComponentClass() {
		// only poll component is allowed.
		return UIPoll.class;
	}
	
	protected boolean isSubmitted(FacesContext facesContext, UIComponent uiComponent) {
		boolean submitted = super.isSubmitted(facesContext, uiComponent);
		UIPoll poll = (UIPoll) uiComponent;
		poll.setSubmitted(submitted);
		return submitted;
	}

}

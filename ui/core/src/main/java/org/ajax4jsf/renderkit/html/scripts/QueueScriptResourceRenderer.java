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


package org.ajax4jsf.renderkit.html.scripts;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.component.QueueRegistry;
import org.ajax4jsf.component.UIQueue;
import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.javascript.JSObject;
import org.ajax4jsf.javascript.ScriptUtils;
import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.ajax4jsf.renderkit.html.QueueRendererData;
import org.ajax4jsf.resource.BaseResourceRenderer;
import org.ajax4jsf.resource.InternetResource;

/**
 * @author Nick Belaevski
 * @since 3.3.0
 */
public class QueueScriptResourceRenderer extends BaseResourceRenderer {

	public static final String QUEUE_SCRIPT_ID = "org.ajax4jsf.queue_script";

	private void encodeQueue(ResponseWriter writer, String queueName, QueueRendererData queueData) 
		throws IOException {
		
		Map<String, Object> queueAttributes = null;
		Map<String, Object> requestAttributes = null;

		if (queueData != null) {
			queueAttributes = queueData.getQueueAttributes();
			requestAttributes = queueData.getRequestAttributes();
		}
		
		writer.writeText("if (!EventQueue.getQueue(" + ScriptUtils.toScript(queueName) + ")) { EventQueue.addQueue(", null);
		writer.writeText(
			new JSObject("EventQueue", 
				queueName, 
				queueAttributes,
				requestAttributes
			).toScript(), null);
		writer.writeText(") };", null);
	}

	@Override
	protected void customEncode(InternetResource resource,
			FacesContext context, Object data) throws IOException {
		super.customEncode(resource, context, data);

		Map<String, Object> queues = (Map<String, Object>) data;
		ResponseWriter writer = context.getResponseWriter();
		
		writer.writeText("if (typeof A4J != 'undefined') { if (A4J.AJAX) { with (A4J.AJAX) {", null);
		for (Entry<String, Object> entry : queues.entrySet()) {
			encodeQueue(writer, entry.getKey(), (QueueRendererData) entry.getValue());
		}
		writer.writeText("}}};", null);
	}
	
	protected void doEncode(InternetResource resource, FacesContext context,
			Object data, Map<String, Object> attributes) throws IOException {
		
		ExternalContext externalContext = context.getExternalContext();
		Map<String, Object> requestMap = externalContext.getRequestMap();

		String resourceKey = resource.getKey();
		if (requestMap.get(resourceKey) == null) {
			requestMap.put(resourceKey, Boolean.TRUE);

			QueueRegistry queueRegistry = QueueRegistry.getInstance(context);
			if (queueRegistry.isShouldCreateDefaultGlobalQueue()) {
				String encodedGlobalQueueName = context.getExternalContext().encodeNamespace(
						UIQueue.GLOBAL_QUEUE_NAME);
				
				if (!queueRegistry.containsQueue(encodedGlobalQueueName)) {
					queueRegistry.registerQueue(context, encodedGlobalQueueName, null);
				}
			}

			if (queueRegistry.hasQueuesToEncode()) {
				super.encode(resource, context, queueRegistry.getRegisteredQueues(context), attributes);
			}
		}
	}
	
	private String encodeQueueScriptId(FacesContext context) {
		String encodedId = QUEUE_SCRIPT_ID;
		
		AjaxContext ajaxContext = AjaxContext.getCurrentInstance(context);
		if (!ajaxContext.isAjaxRequest()) {
			encodedId = context.getExternalContext().encodeNamespace(encodedId);
		}
		
		return encodedId;
	}
	
	@Override
	public void encode(InternetResource resource, FacesContext context,
			Object data, Map<String, Object> attributes) throws IOException {

		Map<String,Object> newAttributes = new LinkedHashMap<String, Object>(attributes);
		newAttributes.put(HTML.id_ATTRIBUTE, encodeQueueScriptId(context));
		
		doEncode(resource, context, data, newAttributes);
	}

	@Override
	public void encode(InternetResource resource, FacesContext context,
			Object data) throws IOException {
		
		doEncode(resource, context, data, 
			Collections.singletonMap(HTML.id_ATTRIBUTE, (Object) encodeQueueScriptId(context)));
	}
	
	@Override
	protected String[][] getCommonAttrs() {
		return new String[][] {
			{HTML.TYPE_ATTR, getContentType()}, 
		};
	}

	@Override
	protected String getHrefAttr() {
		return null;
	}

	@Override
	protected String getTag() {
		return HTML.SCRIPT_ELEM;
	}

	public String getContentType() {
		return "text/javascript";
	}

}

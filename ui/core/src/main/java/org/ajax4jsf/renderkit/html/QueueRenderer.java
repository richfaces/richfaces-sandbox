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

import org.ajax4jsf.component.QueueRegistry;
import org.ajax4jsf.component.UIQueue;
import org.ajax4jsf.javascript.JSFunctionDefinition;
import org.ajax4jsf.renderkit.AjaxRendererUtils;
import org.ajax4jsf.renderkit.RendererBase;
import org.ajax4jsf.renderkit.RendererUtils;
import org.ajax4jsf.renderkit.html.scripts.QueueScript;
import org.ajax4jsf.resource.InternetResource;
import org.richfaces.component.util.MessageUtil;

/**
 * @author Nick Belaevski
 * @since 3.3.0
 */
public class QueueRenderer extends RendererBase {

	private static final String SIZE_EXCEEDED_BEHAVIOR = "sizeExceededBehavior";

	private static final String SIZE = "size";

	private static final String QUEUE_ONSUBMIT_ATTRIBUTE = "queueonsubmit";

	private static final String QUEUE_ONBEFOREDOMUPDATE_ATTRIBUTE = "queueonbeforedomupdate";

	private static final String QUEUE_ONCOMPLETE_ATTRIBUTE = "queueoncomplete";

	private static final String QUEUE_ONERROR_ATTRIBUTE = "queueonerror";

	public static final String BEHAVIOR_DROP_NEXT = "dropNext";

	public static final String BEHAVIOR_DROP_NEW = "dropNew";
	
	public static final String BEHAVIOR_FIRE_NEXT = "fireNext";
	
	public static final String BEHAVIOR_FIRE_NEW = "fireNew";

	private static final String[] REQUEST_ATTRIBUTES = new String[] {
		"ignoreDupResponses",
		"requestDelay",
		"timeout"
	};
	
	private InternetResource[] scripts = new InternetResource[] {
		getResource(QueueScript.class.getName())
	};
	
	@Override
	protected Class<? extends UIComponent> getComponentClass() {
		return UIQueue.class;
	}

	private static final RendererUtils utils = RendererUtils.getInstance();
	
	private boolean isValidBehaviorValue(String value) {
		return BEHAVIOR_DROP_NEW.equals(value) || BEHAVIOR_DROP_NEXT.equals(value) || 
			BEHAVIOR_FIRE_NEW.equals(value) || BEHAVIOR_FIRE_NEXT.equals(value);
 	}
	
	private QueueRendererData createRendererData(FacesContext context, UIQueue queue) {
		Map<String, Object> attributes = queue.getAttributes();
		
		QueueRendererData data = new QueueRendererData();
		
		int size = queue.getSize();
		if (utils.shouldRenderAttribute(size)) {
			data.addQueueAttribute(SIZE, size);
		}
		
		String sizeExceededBehavior = (String) attributes.get(SIZE_EXCEEDED_BEHAVIOR);
		if (utils.shouldRenderAttribute(sizeExceededBehavior)) {
			if (!isValidBehaviorValue(sizeExceededBehavior)) {
				throw new IllegalArgumentException(sizeExceededBehavior + " value of " + 
					SIZE_EXCEEDED_BEHAVIOR + " attribute is not a legal one for component: " + 
					MessageUtil.getLabel(context, queue));
			}
			
			data.addQueueAttribute(SIZE_EXCEEDED_BEHAVIOR, sizeExceededBehavior);
		}
		
		String onsizeexceeded = queue.getOnsizeexceeded();
		if (utils.shouldRenderAttribute(onsizeexceeded)) {
			data.addQueueAttribute("onsizeexceeded", new JSFunctionDefinition("query", "options", "event").addToBody(onsizeexceeded));
		}

		String onrequestqueue = queue.getOnrequestqueue();
		if (utils.shouldRenderAttribute(onrequestqueue)) {
			data.addQueueAttribute("onrequestqueue", new JSFunctionDefinition("query", "options", "event").addToBody(onrequestqueue));
		}

		String onrequestdequeue = queue.getOnrequestdequeue();
		if (utils.shouldRenderAttribute(onrequestdequeue)) {
			data.addQueueAttribute("onrequestdequeue", new JSFunctionDefinition("query", "options", "event").addToBody(onrequestdequeue));
		}
		
		for (String attributeName : REQUEST_ATTRIBUTES) {
			Object value = attributes.get(attributeName);
			if (utils.shouldRenderAttribute(value)) {
				data.addRequestAttribute(attributeName, value);
			}
		}
		
		String status = AjaxRendererUtils.getAjaxStatus(queue);
		if (utils.shouldRenderAttribute(status)) {
			data.addRequestAttribute(AjaxRendererUtils.STATUS_ATTR_NAME, status);
		}
		
		String onBeforeDomUpdate = queue.getOnbeforedomupdate();
		if (utils.shouldRenderAttribute(onBeforeDomUpdate)) {
			data.addRequestAttribute(QUEUE_ONBEFOREDOMUPDATE_ATTRIBUTE, AjaxRendererUtils.buildAjaxOnBeforeDomUpdate(onBeforeDomUpdate));
		}

		String oncomplete = queue.getOncomplete();
		if (utils.shouldRenderAttribute(oncomplete)) {
			data.addRequestAttribute(QUEUE_ONCOMPLETE_ATTRIBUTE, AjaxRendererUtils.buildAjaxOncomplete(oncomplete));
		}
		
		String onsubmit = queue.getOnsubmit();
		if (utils.shouldRenderAttribute(onsubmit)) {
			JSFunctionDefinition onsubmitFunction = new JSFunctionDefinition("request");
			onsubmitFunction.addToBody(onsubmit);
			
			data.addRequestAttribute(QUEUE_ONSUBMIT_ATTRIBUTE, onsubmitFunction);
		}

		String onerror = queue.getOnerror();
		if (utils.shouldRenderAttribute(onerror)) {
			JSFunctionDefinition onerrorFunction = new JSFunctionDefinition("request", "status", "message");
			onerrorFunction.addToBody(onerror);
			
			data.addRequestAttribute(QUEUE_ONERROR_ATTRIBUTE, onerrorFunction);
		}

		return data;
	}
	
	@Override
	public void encodeBegin(FacesContext context, UIComponent component)
			throws IOException {
		super.encodeBegin(context, component);
	}
	
	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {
		super.encodeEnd(context, component);
		
		UIQueue queue = (UIQueue) component;
		if (!queue.isDisabled()) {
			QueueRegistry.getInstance(context).registerQueue(context, 
				queue.getClientName(context), 
				createRendererData(context, queue));
		}
	}
}

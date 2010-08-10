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

package org.ajax4jsf.component;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;

/**
 * @author Nick Belaevski
 * @since 3.3.0
 */
public abstract class UIQueue extends UIComponentBase  {

	public static final String GLOBAL_QUEUE_NAME = "org.richfaces.queue.global";
	
	public static final String COMPONENT_TYPE = "org.ajax4jsf.Queue";

	public static final String COMPONENT_FAMILY = "org.ajax4jsf.Queue";

	public abstract String getName();
	public abstract void setName(String name);
	
	public abstract String getOnsubmit();
	public abstract void setOnsubmit(String onsubmit);

	public abstract String getOncomplete();
	public abstract void setOncomplete(String oncomplete);
	
	public abstract String getOnbeforedomupdate();
	public abstract void setOnbeforedomupdate(String onbeforedomupdate);
	
	public abstract String getOnerror();
	public abstract void setOnerror(String onerror);
	
	public abstract String getOnsizeexceeded();
	public abstract void setOnsizeexceeded(String onsizeexceeded);

	public abstract boolean isDisabled();
	public abstract void setDisabled(boolean disabled);
	
	public abstract int getSize();
	public abstract void setSize(int size);
	
	public abstract String getSizeExceededBehavior();
	public abstract void setSizeExceededBehavior(String behavior);

	public abstract int getTimeout();
	public abstract void setTimeout(int timeout);

	public abstract int getRequestDelay();
	public abstract void setRequestDelay(int requestDelay);

	public abstract boolean isIgnoreDupResponses();
	public abstract void setIgnoreDupResponses(boolean ignoreDupResponses);

	public abstract String getOnrequestqueue();
	public abstract void setOnrequestqueue(String onrequestqueue);
	
	public abstract String getOnrequestdequeue();
	public abstract void setOnrequestdequeue(String onrequestdequeue);
	
	public abstract String getStatus();
	public abstract void setStatus(String status);
	
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	private UIComponent findParentForm() {
		UIComponent component = getParent();
		while (component != null && !(component instanceof UIForm)) {
			component = component.getParent();
		}
		
		return component;
	}
	
	public String getClientName(FacesContext context) {
		UIComponent form = findParentForm();
		String name = getName();
		String clientName;
		
		if (form != null) {
			String formClientId = form.getClientId(context);

			if (name != null && name.length() != 0) {
				clientName = formClientId + NamingContainer.SEPARATOR_CHAR + name;
			} else {
				clientName = formClientId;
			}
		} else {
			if (name == null || name.length() == 0) {
				name = GLOBAL_QUEUE_NAME;
			}
			
			clientName = context.getExternalContext().encodeNamespace(name);
		}
		
		return clientName;
	}
}

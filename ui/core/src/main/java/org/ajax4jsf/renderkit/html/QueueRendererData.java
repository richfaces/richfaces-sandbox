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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Denis Morozov
 * @since 3.3.0
 */
public class QueueRendererData {

	private Map<String, Object> queueAttributes;

	private Map<String, Object> requestAttributes;

	public void addQueueAttribute(String key, Object value) {
		if (queueAttributes == null) {
			queueAttributes = new LinkedHashMap<String, Object>();
		}
		
		queueAttributes.put(key, value);
	}

	public void addRequestAttribute(String key, Object value) {
		if (requestAttributes == null) {
			requestAttributes = new LinkedHashMap<String, Object>();
		}
		
		requestAttributes.put(key, value);
	}
	
	public Map<String, Object> getQueueAttributes() {
		return queueAttributes;
	}
	
	public Map<String, Object> getRequestAttributes() {
		return requestAttributes;
	}
}

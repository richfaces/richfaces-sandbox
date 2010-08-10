/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
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

package org.richfaces.component.xml;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Nick Belaevski - nbelaevski@exadel.com
 * created 16.11.2006
 * 
 */
public class XmlNodeData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8320619478974873168L;

	private String namespace;
	
	private String name = "";
	
	private String text;
	
	private Map<String, Object> attributes = new HashMap<String, Object>();

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttribute(String key, Object value) {
		this.attributes.put(key, value);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text != null ? text : "";
	}
	
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
	public String getNamespace() {
		return namespace;
	}
	
	public String toString() {
		return getName() + "{" + attributes.toString() + " <" + getText().trim() + ">" + "}";
	}
}

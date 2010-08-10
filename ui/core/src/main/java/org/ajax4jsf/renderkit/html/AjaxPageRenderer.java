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
import java.util.Locale;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.component.AjaxContainer;
import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.renderkit.AjaxContainerRenderer;
import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.ajax4jsf.webapp.BaseFilter;

/**
 * @author shura
 * 
 * Render full Html page for AJAX view. Facet "head" rendered in &lthead&gt; of
 * page.
 */
public class AjaxPageRenderer extends AjaxContainerRenderer {

	public static final String RENDERER_TYPE = "org.ajax4jsf.components.AjaxPageRenderer";

	private static final Map doctypes ;

	static {
		// Fill doctype, content-type and namespace map for different formats.
		doctypes = new HashMap();
		doctypes
				.put(
						"html-transitional",
						new String[] {
								"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n",
								"text/html", null });
		doctypes.put("html", new String[] {
				"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"\n"
						+ "\"http://www.w3.org/TR/html4/strict.dtd\">\n",
				"text/html", null });
		doctypes.put("html-frameset", new String[] {
				"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Frameset//EN\"\n"
						+ "\"http://www.w3.org/TR/html4/frameset.dtd\">\n",
				"text/html", null });
		doctypes
				.put(
						"xhtml",
						new String[] {
								"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n"
										+ "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n",
								"application/xhtml+xml",
								"http://www.w3.org/1999/xhtml" });
		doctypes
				.put(
						"xhtml-transitional",
						new String[] {
								"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n"
										+ "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n",
								"application/xhtml+xml",
								"http://www.w3.org/1999/xhtml" });
		doctypes
				.put(
						"xhtml-frameset",
						new String[] {
								"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Frameset//EN\"\n"
										+ "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd\">\n",
								"application/xhtml+xml",
								"http://www.w3.org/1999/xhtml" });
		doctypes.put("html-3.2", new String[] {
				"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2 Final//EN\">\n",
				"text/html", null });
	}

	// private PreparedTemplate pageStyles =
	// HtmlCompiler.compileResource("com/exadel/vcp/renderers/templates/ajax/page-styles.xml");
	protected void preEncodeBegin(FacesContext context, UIComponent component)
			throws IOException {
		// Scripts must be encoded in component, not before html element
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.render.Renderer#encodeBegin(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent)
	 */
	public void doEncodeBegin(ResponseWriter out, FacesContext context,
			UIComponent component) throws IOException {
		AjaxContainer ajax = (AjaxContainer) component;
		// ServletResponse response = (ServletResponse)
		// context.getExternalContext().getResponse();
		Map attributes = component.getAttributes();
		String format = (String) attributes.get("format");
		String contentType = null;
		String namespace = null;
		// String characterEncoding = out.getCharacterEncoding();
		String[] docType = null;
		if (null != format) {
			docType = (String[]) doctypes.get(format);
		} else {
			contentType = out.getContentType();
			for (Iterator iterator = doctypes.values().iterator(); iterator.hasNext();) {
				String[] types = (String[]) iterator.next();
				if(types[1].equals(contentType)){
					docType = types;
					break;
				}
			}
		}
		if (null != docType) {
			contentType = docType[1];
			namespace = docType[2];
			out.write(docType[0]);
		}
		if (null == contentType) {
			contentType = (String) attributes.get("contentType");
		}
		if (null != contentType) {
			// response.setContentType(contentType /*+ ";charset=" +
			// characterEncoding*/);
		}
		// TODO - create "format" attribute and insert properly DOCTYPE
		// declaratiom
		out.startElement("html", component);
		if (null == namespace) {
			namespace = (String) attributes.get("namespace");
		}
		if (null != namespace) {
			out.writeAttribute("xmlns", namespace, "namespace");
		}
		// TODO - html attributes. lang - from current locale ?
		Locale locale = context.getViewRoot().getLocale();
		out.writeAttribute(HTML.lang_ATTRIBUTE, locale.toString(), "lang");
		if (!AjaxContext.getCurrentInstance(context).isAjaxRequest()) {
			out.startElement("head", component);
			// Out title - requied html element.
			Object title = attributes.get("pageTitle");
			String viewId = context.getViewRoot().getViewId();
			if (null == title) {
				title = viewId;// use viewId for empty title
			}
			out.startElement(HTML.title_ELEM, component);
			out.writeText(title, "pageTitle");
			out.endElement(HTML.title_ELEM);
			// Page base - set to current view action url.
			// String pageBase =
			// context.getApplication().getViewHandler().getActionURL(context,
			// viewId);
			// out.startElement("base", component);
			// out.writeURIAttribute("href", pageBase, "pageBase");
			// out.endElement("base");
			// pageStyles.encode(this, context, component);
			UIComponent headFacet = component.getFacet("head");
			if (headFacet != null) {
				// context.getExternalContext().log("Render head facet in AJAX
				// Page");
				renderChild(context, headFacet);
			}
			if (null != contentType) {
				out.startElement("meta", component);
				out.writeAttribute("http-equiv", "Content-Type", null);
				out.writeAttribute("content",
						contentType /* + ";charset=" + characterEncoding */,
						null);
				out.endElement("meta");
			}
			if ((null == context.getExternalContext().getRequestMap().get(
					BaseFilter.RESPONSE_WRAPPER_ATTRIBUTE))) {
				// Filter not used - encode scripts and CSS before component.
				encodeResourcesArray(context, component, getScripts());
				encodeResourcesArray(context, component, getStyles());
			}
			out.endElement("head");
		}
		out.startElement("body", component);
		getUtils().encodePassThru(context, component);
		getUtils().encodeAttributesFromArray(context, component,
				HTML.PASS_THRU_STYLES);
		// TODO - special body attributes :
		getUtils().encodeAttribute(context, component, "onload");
		getUtils().encodeAttribute(context, component, "onunload");
		// onload, onunload
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.render.Renderer#encodeEnd(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent)
	 */
	public void doEncodeEnd(ResponseWriter out, FacesContext context,
			UIComponent component) throws IOException {
		out.endElement("body");
		out.endElement("html");
		// DebugUtils.traceView("ViewRoot in AJAX Page encode end");
	}

	/*
	 * (non-Javadoc) For ajax requests, ViewRoot render childrens directly.
	 * 
	 * @see org.ajax4jsf.renderkit.AjaxContainerRenderer#getRendersChildren()
	 */
	public boolean getRendersChildren() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (AjaxContext.getCurrentInstance(context).isAjaxRequest()) {
			// Ajax Request. Control all output.
			return true;
		}
		// For non Ajax request, view root not render children
		return false;
	}

}

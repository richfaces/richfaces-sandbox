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

package org.ajax4jsf.taglib.html.jsp;

import java.io.IOException;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.ajax4jsf.component.UIInclude;
import org.ajax4jsf.webapp.taglib.UIComponentTagBase;

/**
 * @author shura
 * 
 */
public class IncludeTag extends UIComponentTagBase {

	/**
	 * @author shura
	 *
	 */
	private static final class NullJspWriter extends JspWriter {
		
		
		/**
		 * @param arg0
		 * @param arg1
		 */
		private NullJspWriter() {
			super(1024, false);
		}

		public void clear() throws IOException {
		}

		public void clearBuffer() throws IOException {
		}

		public void close() throws IOException {
		}

		public void flush() throws IOException {
		}

		public int getRemaining() {
			return 0;
		}

		public void newLine() throws IOException {
		}

		public void print(boolean arg0) throws IOException {
		}

		public void print(char arg0) throws IOException {
		}

		public void print(int arg0) throws IOException {
		}

		public void print(long arg0) throws IOException {
		}

		public void print(float arg0) throws IOException {
		}

		public void print(double arg0) throws IOException {
		}

		public void print(char[] arg0) throws IOException {
		}

		public void print(String arg0) throws IOException {
		}

		public void print(Object arg0) throws IOException {
		}

		public void println() throws IOException {
		}

		public void println(boolean arg0) throws IOException {
		}

		public void println(char arg0) throws IOException {
		}

		public void println(int arg0) throws IOException {
		}

		public void println(long arg0) throws IOException {
		}

		public void println(float arg0) throws IOException {
		}

		public void println(double arg0) throws IOException {
		}

		public void println(char[] arg0) throws IOException {
		}

		public void println(String arg0) throws IOException {
		}

		public void println(Object arg0) throws IOException {
		}

		public void write(char[] cbuf, int off, int len) throws IOException {
		}
	}

	private ValueExpression viewId;

	/**
	 * @param viewId
	 *            the viewId to set
	 */
	public void setViewId(ValueExpression viewId) {
		this.viewId = viewId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ajax4jsf.webapp.taglib.UIComponentTagBase#release()
	 */
	public void release() {
		super.release();
		viewId = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ajax4jsf.webapp.taglib.UIComponentTagBase#setProperties(javax.faces.component.UIComponent)
	 */
	protected void setProperties(UIComponent component) {
		super.setProperties(component);
		UIInclude include = (UIInclude) component;
		if (this.viewId != null) {
			if (this.viewId.isLiteralText()) {
				try {

					String value = (String) getFacesContext().getApplication()
							.getExpressionFactory().coerceToType(
									this.viewId.getExpressionString(),
									String.class);

					include.setViewId(value);
				} catch (ELException e) {
					throw new FacesException(e);
				}
			} else {
				component.setValueExpression("viewId", this.viewId);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.webapp.UIComponentTag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		int i = super.doEndTag();
		return i;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.webapp.UIComponentTag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		int i = super.doStartTag();
		String resourcePath;
		UIComponent component = getComponentInstance();
		UIInclude includeComponent = null;
		if (component instanceof UIInclude) {
			includeComponent = (UIInclude) component;
			resourcePath = includeComponent.getViewId();
			 if(includeComponent.isWasNavigation()){
			 component.getChildren().clear();
			 }
		} else {
			resourcePath = (String) component.getAttributes().get("viewId");
		}
		JspWriter out;
		if (component.isRendered()) {
			out = this.pageContext.getOut();
		} else {
			// All content of response will be skip.
			out = new NullJspWriter();
		}
		ServletRequest request = this.pageContext.getRequest();
		ServletResponse response = this.pageContext.getResponse();
		RequestDispatcher rd = request.getRequestDispatcher(resourcePath);
		FacesContext facesContext = getFacesContext();
		if(null == rd){
			throw new JspException("UIInclude component "+component.getClientId(facesContext)+ " could't include page with path "+resourcePath);
		}
		try {
			ServletResponseWrapperInclude responseWrapper = new ServletResponseWrapperInclude(
					response, out);
			rd.include(request, responseWrapper);
			// Write buffered data, if any;
			responseWrapper.flushBuffer();
		} catch (Exception e) {
			throw new JspException(e);
		}
		return i;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.webapp.UIComponentTag#getComponentType()
	 */
	public String getComponentType() {
		return UIInclude.COMPONENT_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.webapp.UIComponentTag#getRendererType()
	 */
	public String getRendererType() {
		return null;
	}

}

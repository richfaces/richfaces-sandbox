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

package org.ajax4jsf.taglib.html.facelets;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.FaceletException;

/**
 * @author shura
 *
 */
public class IncludeFaceletContext extends FaceletContext {
	
	private FaceletContext defaultContext;
	
	private Map ids;

	/**
	 * @param defaultContext
	 */
	public IncludeFaceletContext(FaceletContext defaultContext) {
		this.defaultContext = defaultContext;
		this.ids = new HashMap();
	}

	//TODO JSF 2.0 analog?
//	/**
//	 * @param client
//	 * @see com.sun.facelets.FaceletContext#extendClient(com.sun.facelets.TemplateClient)
//	 */
//	public void extendClient(TemplateClient client) {
//		this.defaultContext.extendClient(client);
//	}

	/**
	 * @param base
	 * @return
	 * @see com.sun.facelets.FaceletContext#generateUniqueId(java.lang.String)
	 */
    public String generateUniqueId(String base) {
        Integer cnt = (Integer) this.ids.get(base);
        if (cnt == null) {
            this.ids.put(base, new Integer(0));
            return base;
        } else {
            int i = cnt.intValue() + 1;
            this.ids.put(base, new Integer(i));
            return base + "_" + i;
        }
    }

	/**
	 * @param name
	 * @return
	 * @see com.sun.facelets.FaceletContext#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String name) {
		return this.defaultContext.getAttribute(name);
	}

	/**
	 * @param key
	 * @return
	 * @see javax.el.ELContext#getContext(java.lang.Class)
	 */
	public Object getContext(Class key) {
		return this.defaultContext.getContext(key);
	}

	/**
	 * @return
	 * @see javax.el.ELContext#getELResolver()
	 */
	public ELResolver getELResolver() {
		return this.defaultContext.getELResolver();
	}

	/**
	 * @return
	 * @see com.sun.facelets.FaceletContext#getExpressionFactory()
	 */
	public ExpressionFactory getExpressionFactory() {
		return this.defaultContext.getExpressionFactory();
	}

	/**
	 * @return
	 * @see com.sun.facelets.FaceletContext#getFacesContext()
	 */
	public FacesContext getFacesContext() {
		return this.defaultContext.getFacesContext();
	}

	/**
	 * @return
	 * @see javax.el.ELContext#getFunctionMapper()
	 */
	public FunctionMapper getFunctionMapper() {
		return this.defaultContext.getFunctionMapper();
	}

	/**
	 * @return
	 * @see javax.el.ELContext#getLocale()
	 */
	public Locale getLocale() {
		return this.defaultContext.getLocale();
	}

	/**
	 * @return
	 * @see javax.el.ELContext#getVariableMapper()
	 */
	public VariableMapper getVariableMapper() {
		return this.defaultContext.getVariableMapper();
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return this.defaultContext.hashCode();
	}

	//TODO JSF 2.0 analog?
//	/**
//	 * @param parent
//	 * @param name
//	 * @return
//	 * @throws IOException
//	 * @throws FaceletException
//	 * @throws FacesException
//	 * @throws ELException
//	 * @see com.sun.facelets.FaceletContext#includeDefinition(javax.faces.component.UIComponent, java.lang.String)
//	 */
//	public boolean includeDefinition(UIComponent parent, String name) throws IOException, FaceletException, FacesException, ELException {
//		return this.defaultContext.includeDefinition(parent, name);
//	}

	/**
	 * @param parent
	 * @param relativePath
	 * @throws IOException
	 * @throws FaceletException
	 * @throws FacesException
	 * @throws ELException
	 * @see com.sun.facelets.FaceletContext#includeFacelet(javax.faces.component.UIComponent, java.lang.String)
	 */
	public void includeFacelet(UIComponent parent, String relativePath) throws IOException, FaceletException, FacesException, ELException {
		this.defaultContext.includeFacelet(parent, relativePath);
	}

	/**
	 * @param parent
	 * @param absolutePath
	 * @throws IOException
	 * @throws FaceletException
	 * @throws FacesException
	 * @throws ELException
	 * @see com.sun.facelets.FaceletContext#includeFacelet(javax.faces.component.UIComponent, java.net.URL)
	 */
	public void includeFacelet(UIComponent parent, URL absolutePath) throws IOException, FaceletException, FacesException, ELException {
		this.defaultContext.includeFacelet(parent, absolutePath);
	}

	/**
	 * @return
	 * @see javax.el.ELContext#isPropertyResolved()
	 */
	public boolean isPropertyResolved() {
		return this.defaultContext.isPropertyResolved();
	}

	//TODO JSF 2.0 analog?
//	/**
//	 * @param client
//	 * @see com.sun.facelets.FaceletContext#popClient(com.sun.facelets.TemplateClient)
//	 */
//	public void popClient(TemplateClient client) {
//		this.defaultContext.popClient(client);
//	}
//
//	/**
//	 * @param client
//	 * @see com.sun.facelets.FaceletContext#pushClient(com.sun.facelets.TemplateClient)
//	 */
//	public void pushClient(TemplateClient client) {
//		this.defaultContext.pushClient(client);
//	}

	/**
	 * @param key
	 * @param contextObject
	 * @see javax.el.ELContext#putContext(java.lang.Class, java.lang.Object)
	 */
	public void putContext(Class key, Object contextObject) {
		this.defaultContext.putContext(key, contextObject);
	}

	/**
	 * @param name
	 * @param value
	 * @see com.sun.facelets.FaceletContext#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String name, Object value) {
		this.defaultContext.setAttribute(name, value);
	}

	/**
	 * @param fnMapper
	 * @see com.sun.facelets.FaceletContext#setFunctionMapper(javax.el.FunctionMapper)
	 */
	public void setFunctionMapper(FunctionMapper fnMapper) {
		this.defaultContext.setFunctionMapper(fnMapper);
	}

	/**
	 * @param locale
	 * @see javax.el.ELContext#setLocale(java.util.Locale)
	 */
	public void setLocale(Locale locale) {
		this.defaultContext.setLocale(locale);
	}

	/**
	 * @param resolved
	 * @see javax.el.ELContext#setPropertyResolved(boolean)
	 */
	public void setPropertyResolved(boolean resolved) {
		this.defaultContext.setPropertyResolved(resolved);
	}

	/**
	 * @param varMapper
	 * @see com.sun.facelets.FaceletContext#setVariableMapper(javax.el.VariableMapper)
	 */
	public void setVariableMapper(VariableMapper varMapper) {
		this.defaultContext.setVariableMapper(varMapper);
	}

}

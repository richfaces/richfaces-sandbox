/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.cdk.faces;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.faces.context.ExternalContext;

import com.google.common.collect.Sets;

/**
 * @author Nick Belaevski
 * 
 */
public class ExternalContextImpl extends ExternalContext {

    private String webRoot;
    
    private Map<String, String> initParamsMap = new HashMap<String, String>();

    protected void setWebRoot(String webroot) {
        this.webRoot = webroot;
    }
    
    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#dispatch(java.lang.String)
     */
    @Override
    public void dispatch(String path) throws IOException {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#encodeActionURL(java.lang.String)
     */
    @Override
    public String encodeActionURL(String url) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#encodeNamespace(java.lang.String)
     */
    @Override
    public String encodeNamespace(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#encodeResourceURL(java.lang.String)
     */
    @Override
    public String encodeResourceURL(String url) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#getApplicationMap()
     */
    @Override
    public Map<String, Object> getApplicationMap() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#getAuthType()
     */
    @Override
    public String getAuthType() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#getContext()
     */
    @Override
    public Object getContext() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#getInitParameter(java.lang.String)
     */
    @Override
    public String getInitParameter(String name) {
        // TODO Auto-generated method stub
        return initParamsMap.get(name);
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#getInitParameterMap()
     */
    @Override
    public Map getInitParameterMap() {
        // TODO Auto-generated method stub
        return initParamsMap;
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#getRemoteUser()
     */
    @Override
    public String getRemoteUser() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#getRequest()
     */
    @Override
    public Object getRequest() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#getRequestContextPath()
     */
    @Override
    public String getRequestContextPath() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#getRequestCookieMap()
     */
    @Override
    public Map<String, Object> getRequestCookieMap() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#getRequestHeaderMap()
     */
    @Override
    public Map<String, String> getRequestHeaderMap() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#getRequestHeaderValuesMap()
     */
    @Override
    public Map<String, String[]> getRequestHeaderValuesMap() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#getRequestLocale()
     */
    @Override
    public Locale getRequestLocale() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#getRequestLocales()
     */
    @Override
    public Iterator<Locale> getRequestLocales() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#getRequestMap()
     */
    @Override
    public Map<String, Object> getRequestMap() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#getRequestParameterMap()
     */
    @Override
    public Map<String, String> getRequestParameterMap() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#getRequestParameterNames()
     */
    @Override
    public Iterator<String> getRequestParameterNames() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#getRequestParameterValuesMap()
     */
    @Override
    public Map<String, String[]> getRequestParameterValuesMap() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#getRequestPathInfo()
     */
    @Override
    public String getRequestPathInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#getRequestServletPath()
     */
    @Override
    public String getRequestServletPath() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#getResource(java.lang.String)
     */
    @Override
    public URL getResource(String path) throws MalformedURLException {
        return new URL("file:" + webRoot + path);
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#getResourceAsStream(java.lang.String)
     */
    @Override
    public InputStream getResourceAsStream(String path) {
        File file = new File(webRoot, path);
        if (file.exists()) {
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        return null;
    }

    @Override
    public Set<String> getResourcePaths(String path) {
        File root = new File(webRoot, path);
        
        Set<String> result = Sets.newHashSet();

        if (root.exists()) {
            String[] list = root.list();
            for (String childName : list) {
                String name = childName;
                File child = new File(root, name);
                if (child.isDirectory()) {
                    name += '/';
                }
                
                result.add(path + '/' + name);
            }
        }
        
        return result;
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#getResponse()
     */
    @Override
    public Object getResponse() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#getSession(boolean)
     */
    @Override
    public Object getSession(boolean create) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#getSessionMap()
     */
    @Override
    public Map<String, Object> getSessionMap() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#getUserPrincipal()
     */
    @Override
    public Principal getUserPrincipal() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#isUserInRole(java.lang.String)
     */
    @Override
    public boolean isUserInRole(String role) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#log(java.lang.String)
     */
    @Override
    public void log(String message) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#log(java.lang.String, java.lang.Throwable)
     */
    @Override
    public void log(String message, Throwable exception) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see javax.faces.context.ExternalContext#redirect(java.lang.String)
     */
    @Override
    public void redirect(String url) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getResponseCharacterEncoding() {
        return "UTF-8";
    }
}
